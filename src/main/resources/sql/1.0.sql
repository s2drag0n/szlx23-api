-- 创建数据库扩展（用于UUID和全文搜索）
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- 生成UUID
CREATE EXTENSION IF NOT EXISTS "pg_trgm"; -- 提供模糊搜索和相似度匹配

-- 分类表
CREATE TABLE categories
(
    id          SERIAL PRIMARY KEY, -- 主键，自增ID
    name        VARCHAR(50) UNIQUE NOT NULL, -- 分类名称（唯一）
    slug        VARCHAR(50) UNIQUE NOT NULL, -- 分类URL别名（唯一）
    description TEXT, -- 分类描述
    color       VARCHAR(7)               DEFAULT '#3B82F6', -- 分类显示颜色（十六进制）
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 最后更新时间
);

-- 标签表
CREATE TABLE tags
(
    id         SERIAL PRIMARY KEY, -- 主键，自增ID
    name       VARCHAR(50) UNIQUE NOT NULL, -- 标签名称（唯一）
    slug       VARCHAR(50) UNIQUE NOT NULL, -- 标签URL别名（唯一）
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 创建时间
);

-- 文章表（简化版，去掉作者关联）
CREATE TABLE posts
(
    id            UUID PRIMARY KEY         DEFAULT uuid_generate_v4(), -- 主键，UUID
    title         VARCHAR(200)        NOT NULL, -- 文章标题
    slug          VARCHAR(200) UNIQUE NOT NULL, -- 文章URL别名（唯一）
    excerpt       TEXT, -- 文章摘要
    content       TEXT                NOT NULL, -- 文章正文内容
    cover_image   VARCHAR(500), -- 封面图片URL
    category_id   INTEGER             REFERENCES categories (id) ON DELETE SET NULL, -- 关联分类ID（删除分类后设为NULL）
    status        VARCHAR(20)              DEFAULT 'draft' CHECK (status IN ('draft', 'published', 'archived')), -- 文章状态：草稿/已发布/已归档
    is_featured   BOOLEAN                  DEFAULT FALSE, -- 是否置顶/推荐
    view_count    INTEGER                  DEFAULT 0, -- 浏览次数
    read_time     INTEGER, -- 预计阅读时间（分钟）
    published_at  TIMESTAMP WITH TIME ZONE, -- 发布时间（为空表示未发布）
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 最后更新时间

    -- 全文搜索字段
    search_vector tsvector -- 用于全文搜索的向量数据
);

-- 文章-标签关联表（多对多关系）
CREATE TABLE post_tags
(
    post_id UUID REFERENCES posts (id) ON DELETE CASCADE, -- 关联文章ID（级联删除）
    tag_id  INTEGER REFERENCES tags (id) ON DELETE CASCADE, -- 关联标签ID（级联删除）
    PRIMARY KEY (post_id, tag_id) -- 联合主键
);

-- 评论表（简化版，去掉用户关联，只保留基础信息）
CREATE TABLE comments
(
    id           UUID PRIMARY KEY         DEFAULT uuid_generate_v4(), -- 主键，UUID
    post_id      UUID REFERENCES posts (id) ON DELETE CASCADE, -- 关联文章ID（级联删除）
    parent_id    UUID REFERENCES comments (id) ON DELETE CASCADE, -- 父评论ID（支持嵌套评论，级联删除）
    author_name  VARCHAR(100) NOT NULL, -- 评论者姓名
    author_email VARCHAR(100), -- 评论者邮箱（可选）
    content      TEXT NOT NULL, -- 评论内容
    status       VARCHAR(20)              DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'spam')), -- 评论状态：待审核/已通过/垃圾评论
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 最后更新时间
);

-- 浏览历史表（用于统计，去掉用户关联）
CREATE TABLE post_views
(
    id         UUID PRIMARY KEY         DEFAULT uuid_generate_v4(), -- 主键，UUID
    post_id    UUID REFERENCES posts (id) ON DELETE CASCADE, -- 关联文章ID（级联删除）
    ip_address INET, -- 访问者IP地址
    user_agent TEXT, -- 访问者浏览器信息
    viewed_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 访问时间
);

-- 创建索引以提高查询性能
CREATE INDEX idx_posts_category ON posts (category_id); -- 文章分类索引
CREATE INDEX idx_posts_status ON posts (status); -- 文章状态索引
CREATE INDEX idx_posts_published ON posts (published_at DESC); -- 文章发布时间索引（降序）
CREATE INDEX idx_posts_featured ON posts (is_featured) WHERE is_featured = TRUE; -- 置顶文章条件索引
CREATE INDEX idx_posts_search ON posts USING GIN (search_vector); -- 全文搜索GIN索引
CREATE INDEX idx_comments_post ON comments (post_id); -- 评论文章ID索引
CREATE INDEX idx_comments_status ON comments (status); -- 评论状态索引
CREATE INDEX idx_post_tags_post ON post_tags (post_id); -- 文章标签关联的文章ID索引
CREATE INDEX idx_post_tags_tag ON post_tags (tag_id); -- 文章标签关联的标签ID索引
CREATE INDEX idx_post_views_post ON post_views (post_id); -- 浏览历史的文章ID索引
CREATE INDEX idx_post_views_date ON post_views (viewed_at DESC); -- 浏览历史时间索引（降序）

-- 创建触发器函数：更新updated_at字段
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP; -- 将updated_at字段设置为当前时间
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 为需要的表添加updated_at触发器
CREATE TRIGGER update_categories_updated_at
    BEFORE UPDATE
    ON categories
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column(); -- 分类表更新前触发器

CREATE TRIGGER update_posts_updated_at
    BEFORE UPDATE
    ON posts
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column(); -- 文章表更新前触发器

CREATE TRIGGER update_comments_updated_at
    BEFORE UPDATE
    ON comments
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column(); -- 评论表更新前触发器

-- 创建触发器函数：自动生成搜索向量
CREATE OR REPLACE FUNCTION update_post_search_vector()
    RETURNS TRIGGER AS
$$
BEGIN
    -- 将标题、摘要、内容合并生成搜索向量，并设置不同权重
    NEW.search_vector :=
            setweight(to_tsvector('simple', coalesce(NEW.title, '')), 'A') || -- 标题权重最高（A）
            setweight(to_tsvector('simple', coalesce(NEW.excerpt, '')), 'B') || -- 摘要权重中等（B）
            setweight(to_tsvector('simple', coalesce(NEW.content, '')), 'C'); -- 内容权重最低（C）
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 文章表全文搜索触发器
CREATE TRIGGER tsvector_update
    BEFORE INSERT OR UPDATE
    ON posts
    FOR EACH ROW
EXECUTE FUNCTION update_post_search_vector(); -- 插入或更新文章前自动生成搜索向量

-- 创建触发器函数：自动计算阅读时间
CREATE OR REPLACE FUNCTION calculate_read_time()
    RETURNS TRIGGER AS
$$
DECLARE
    word_count INTEGER; -- 单词计数变量
BEGIN
    -- 假设平均阅读速度为每分钟200字
    IF NEW.content IS NOT NULL THEN
        -- 通过空格分割计算单词数量
        word_count := array_length(regexp_split_to_array(NEW.content, '\s+'), 1);
        -- 计算阅读时间（向上取整）
        NEW.read_time := CEIL(word_count::FLOAT / 200.0);
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 文章表阅读时间计算触发器
CREATE TRIGGER calculate_read_time_trigger
    BEFORE INSERT OR UPDATE
    ON posts
    FOR EACH ROW
EXECUTE FUNCTION calculate_read_time(); -- 插入或更新文章前自动计算阅读时间