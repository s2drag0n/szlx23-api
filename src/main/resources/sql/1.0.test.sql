-- 插入初始分类
INSERT INTO blog_categories (name, slug, description, color)
VALUES ('Vue.js', 'vuejs', 'Vue.js框架相关文章', '#42B883'),
       ('TypeScript', 'typescript', 'TypeScript编程语言', '#3178C6'),
       ('云计算', 'cloud', '云计算和云原生技术', '#FF9900'),
       ('CSS', 'css', 'CSS样式和布局', '#264DE4'),
       ('架构设计', 'architecture', '软件架构设计', '#795548'),
       ('JavaScript', 'javascript', 'JavaScript编程语言', '#F7DF1E');

-- 插入初始标签
INSERT INTO blog_tags (name, slug)
VALUES ('Vue 3', 'vue-3'),
       ('组合式API', 'composition-api'),
       ('TypeScript', 'typescript'),
       ('最佳实践', 'best-practices'),
       ('云原生', 'cloud-native'),
       ('Docker', 'docker'),
       ('Kubernetes', 'kubernetes'),
       ('CSS Grid', 'css-grid'),
       ('Flexbox', 'flexbox'),
       ('Serverless', 'serverless'),
       ('ES2023', 'es2023');

-- 插入示例文章
INSERT INTO blog_posts (title, slug, excerpt, content, cover_image, category_id, status, is_featured, published_at)
VALUES ('使用Vue 3构建现代Web应用',
        'building-modern-web-applications-with-vue-3',
        '探索Vue 3的最新特性，了解如何构建更高效、可扩展的Web应用程序。',
        '# 使用Vue 3构建现代Web应用

Vue 3引入了许多令人兴奋的特性，彻底改变了我们构建Web应用的方式。在这份全面的指南中，我们将探讨关键的改进和最佳实践。

## 组合式API

组合式API提供了更灵活的方式来组织组件逻辑...',
        '/images/vue3-cover.jpg',
        (SELECT id FROM blog_categories WHERE slug = 'vuejs'),
        'published',
        TRUE,
        '2024-01-15 10:00:00'),

       ('大型TypeScript项目的最佳实践',
        'typescript-best-practices-for-large-projects',
        '学习如何构建TypeScript代码库以实现最大的可维护性和开发效率。',
        '# 大型TypeScript项目的最佳实践

在大型TypeScript项目中，遵循最佳实践对于保持代码质量和开发效率至关重要。',
        '/images/typescript-cover.jpg',
        (SELECT id FROM blog_categories WHERE slug = 'typescript'),
        'published',
        TRUE,
        '2024-01-10 10:00:00'),

       ('云原生架构设计指南',
        'cloud-native-architecture-guide',
        '深入了解云原生架构的核心概念和实践方法。',
        '# 云原生架构设计指南

云原生架构是现代应用开发的重要范式...',
        '/images/cloud-cover.jpg',
        (SELECT id FROM blog_categories WHERE slug = 'cloud'),
        'published',
        FALSE,
        '2024-01-20 14:30:00');

-- 为文章关联标签
INSERT INTO blog_post_tags (post_id, tag_id)
VALUES
-- 第一篇文章关联Vue相关标签
((SELECT id FROM blog_posts WHERE slug = 'building-modern-web-applications-with-vue-3'),
 (SELECT id FROM blog_tags WHERE slug = 'vue-3')),
((SELECT id FROM blog_posts WHERE slug = 'building-modern-web-applications-with-vue-3'),
 (SELECT id FROM blog_tags WHERE slug = 'composition-api')),

-- 第二篇文章关联TypeScript相关标签
((SELECT id FROM blog_posts WHERE slug = 'typescript-best-practices-for-large-projects'),
 (SELECT id FROM blog_tags WHERE slug = 'typescript')),
((SELECT id FROM blog_posts WHERE slug = 'typescript-best-practices-for-large-projects'),
 (SELECT id FROM blog_tags WHERE slug = 'best-practices')),

-- 第三篇文章关联云原生相关标签
((SELECT id FROM blog_posts WHERE slug = 'cloud-native-architecture-guide'),
 (SELECT id FROM blog_tags WHERE slug = 'cloud-native')),
((SELECT id FROM blog_posts WHERE slug = 'cloud-native-architecture-guide'),
 (SELECT id FROM blog_tags WHERE slug = 'docker'));

-- 插入示例评论
INSERT INTO blog_comments (post_id, author_name, author_email, content, status, created_at)
VALUES ((SELECT id FROM blog_posts WHERE slug = 'building-modern-web-applications-with-vue-3'),
        '张三', 'zhangsan@example.com', '这篇文章非常实用，Vue 3的组合式API确实让代码组织更清晰了！', 'approved',
        '2024-01-16 09:20:00'),

       ((SELECT id FROM blog_posts WHERE slug = 'typescript-best-practices-for-large-projects'),
        '李四', 'lisi@example.com', '类型定义的最佳实践部分写得很好，对我们项目很有帮助。', 'approved',
        '2024-01-11 15:45:00'),

       ((SELECT id FROM blog_posts WHERE slug = 'cloud-native-architecture-guide'),
        '王五', 'wangwu@example.com', '期待更多关于微服务架构的深入内容！', 'approved', '2024-01-21 11:30:00');

-- 插入浏览记录（模拟一些访问数据）
INSERT INTO blog_post_views (post_id, ip_address, user_agent, viewed_at)
VALUES ((SELECT id FROM blog_posts WHERE slug = 'building-modern-web-applications-with-vue-3'),
        '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', '2024-01-16 10:00:00'),

       ((SELECT id FROM blog_posts WHERE slug = 'typescript-best-practices-for-large-projects'),
        '192.168.1.101', 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36', '2024-01-12 14:20:00');