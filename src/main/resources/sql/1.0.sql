-- 创建数据库扩展（用于UUID和全文搜索）
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- 生成UUID
CREATE EXTENSION IF NOT EXISTS "pg_trgm"; -- 提供模糊搜索和相似度匹配

-- 博客分类表
CREATE TABLE blog_categories
(
    id          SERIAL PRIMARY KEY, -- 主键，自增ID
    name        VARCHAR(50) UNIQUE NOT NULL, -- 分类名称（唯一）
    slug        VARCHAR(50) UNIQUE NOT NULL, -- 分类URL别名（唯一）
    description TEXT, -- 分类描述
    color       VARCHAR(7)               DEFAULT '#3B82F6', -- 分类显示颜色（十六进制）
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 最后更新时间
);

-- 博客标签表
CREATE TABLE blog_tags
(
    id         SERIAL PRIMARY KEY, -- 主键，自增ID
    name       VARCHAR(50) UNIQUE NOT NULL, -- 标签名称（唯一）
    slug       VARCHAR(50) UNIQUE NOT NULL, -- 标签URL别名（唯一）
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 创建时间
);

-- 博客文章表
CREATE TABLE blog_posts
(
    id            UUID PRIMARY KEY         DEFAULT uuid_generate_v4(), -- 主键，UUID
    title         VARCHAR(200)        NOT NULL, -- 文章标题
    slug          VARCHAR(200) UNIQUE NOT NULL, -- 文章URL别名（唯一）
    excerpt       TEXT, -- 文章摘要
    content       TEXT                NOT NULL, -- 文章正文内容
    cover_image   VARCHAR(500), -- 封面图片URL
    category_id   INTEGER             REFERENCES blog_categories (id) ON DELETE SET NULL, -- 关联分类ID
    status        VARCHAR(20)              DEFAULT 'draft' CHECK (status IN ('draft', 'published', 'archived')), -- 文章状态
    is_featured   BOOLEAN                  DEFAULT FALSE, -- 是否置顶/推荐
    view_count    INTEGER                  DEFAULT 0, -- 浏览次数
    read_time     INTEGER, -- 预计阅读时间（分钟）
    published_at  TIMESTAMP WITH TIME ZONE, -- 发布时间
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 最后更新时间
    search_vector tsvector -- 全文搜索向量
);

-- 博客文章-标签关联表
CREATE TABLE blog_post_tags
(
    post_id UUID REFERENCES blog_posts (id) ON DELETE CASCADE, -- 关联文章ID
    tag_id  INTEGER REFERENCES blog_tags (id) ON DELETE CASCADE, -- 关联标签ID
    PRIMARY KEY (post_id, tag_id) -- 联合主键
);

-- 博客评论表
CREATE TABLE blog_comments
(
    id           UUID PRIMARY KEY         DEFAULT uuid_generate_v4(), -- 主键，UUID
    post_id      UUID REFERENCES blog_posts (id) ON DELETE CASCADE, -- 关联文章ID
    parent_id    UUID REFERENCES blog_comments (id) ON DELETE CASCADE, -- 父评论ID
    author_name  VARCHAR(100) NOT NULL, -- 评论者姓名
    author_email VARCHAR(100), -- 评论者邮箱
    content      TEXT NOT NULL, -- 评论内容
    status       VARCHAR(20)              DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'spam')), -- 评论状态
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- 创建时间
    updated_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 最后更新时间
);

-- 博客浏览历史表
CREATE TABLE blog_post_views
(
    id         UUID PRIMARY KEY         DEFAULT uuid_generate_v4(), -- 主键，UUID
    post_id    UUID REFERENCES blog_posts (id) ON DELETE CASCADE, -- 关联文章ID
    ip_address INET, -- 访问者IP地址
    user_agent TEXT, -- 访问者浏览器信息
    viewed_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP -- 访问时间
);

-- 创建博客相关索引
CREATE INDEX idx_blog_posts_category ON blog_posts (category_id);
CREATE INDEX idx_blog_posts_status ON blog_posts (status);
CREATE INDEX idx_blog_posts_published ON blog_posts (published_at DESC);
CREATE INDEX idx_blog_posts_featured ON blog_posts (is_featured) WHERE is_featured = TRUE;
CREATE INDEX idx_blog_posts_search ON blog_posts USING GIN (search_vector);
CREATE INDEX idx_blog_comments_post ON blog_comments (post_id);
CREATE INDEX idx_blog_comments_status ON blog_comments (status);
CREATE INDEX idx_blog_post_tags_post ON blog_post_tags (post_id);
CREATE INDEX idx_blog_post_tags_tag ON blog_post_tags (tag_id);
CREATE INDEX idx_blog_post_views_post ON blog_post_views (post_id);
CREATE INDEX idx_blog_post_views_date ON blog_post_views (viewed_at DESC);

-- 创建触发器函数：更新updated_at字段
CREATE OR REPLACE FUNCTION update_updated_at_column()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 为博客表添加updated_at触发器
CREATE TRIGGER update_blog_categories_updated_at
    BEFORE UPDATE
    ON blog_categories
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_blog_posts_updated_at
    BEFORE UPDATE
    ON blog_posts
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_blog_comments_updated_at
    BEFORE UPDATE
    ON blog_comments
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

-- 创建触发器函数：自动生成搜索向量
CREATE OR REPLACE FUNCTION update_blog_post_search_vector()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.search_vector :=
            setweight(to_tsvector('simple', coalesce(NEW.title, '')), 'A') ||
            setweight(to_tsvector('simple', coalesce(NEW.excerpt, '')), 'B') ||
            setweight(to_tsvector('simple', coalesce(NEW.content, '')), 'C');
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 博客文章表全文搜索触发器
CREATE TRIGGER blog_tsvector_update
    BEFORE INSERT OR UPDATE
    ON blog_posts
    FOR EACH ROW
EXECUTE FUNCTION update_blog_post_search_vector();

-- 创建触发器函数：自动计算阅读时间
CREATE OR REPLACE FUNCTION calculate_blog_read_time()
    RETURNS TRIGGER AS
$$
DECLARE
    word_count INTEGER;
BEGIN
    IF NEW.content IS NOT NULL THEN
        word_count := array_length(regexp_split_to_array(NEW.content, '\s+'), 1);
        NEW.read_time := CEIL(word_count::FLOAT / 200.0);
    END IF;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- 博客文章表阅读时间计算触发器
CREATE TRIGGER calculate_blog_read_time_trigger
    BEFORE INSERT OR UPDATE
    ON blog_posts
    FOR EACH ROW
EXECUTE FUNCTION calculate_blog_read_time();