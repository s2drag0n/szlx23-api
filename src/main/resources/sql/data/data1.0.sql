-- =========================
-- seed_blog.sql
-- =========================
BEGIN;

INSERT INTO post_category (slug, name) VALUES
                                           ('database', '数据库'),
                                           ('backend', '后端开发'),
                                           ('architecture', '系统架构')
ON CONFLICT DO NOTHING;

INSERT INTO post_tag (slug, name) VALUES
                                      ('postgresql', 'PostgreSQL'),
                                      ('mysql', 'MySQL'),
                                      ('performance', '性能优化'),
                                      ('fulltext', '全文检索'),
                                      ('design', '系统设计')
ON CONFLICT DO NOTHING;

INSERT INTO post (slug, title, excerpt, content, status, category_id, publish_time)
SELECT
    'postgres-fulltext-search',
    'PostgreSQL 中文全文检索实践',
    '详细介绍 PostgreSQL 在中文场景下的全文检索实现方式。',
    'PostgreSQL 自带全文检索能力，通过 tsvector 与 tsquery 可以实现高效搜索。',
    1,
    id,
    NOW()
FROM post_category WHERE slug = 'database'
ON CONFLICT DO NOTHING;

INSERT INTO post (slug, title, excerpt, content, status, category_id, publish_time)
SELECT
    'postgres-index-optimization',
    'PostgreSQL 索引与性能优化',
    '索引是数据库性能优化的核心手段之一。',
    '合理设计索引可以显著提升数据库查询性能。',
    1,
    id,
    NOW()
FROM post_category WHERE slug = 'database'
ON CONFLICT DO NOTHING;

INSERT INTO post (slug, title, excerpt, content, status, category_id)
SELECT
    'backend-service-design',
    '后端服务设计原则',
    '讨论后端服务在设计阶段需要关注的关键点。',
    '一个良好的后端服务需要具备高内聚、低耦合和可扩展性。',
    0,
    id
FROM post_category WHERE slug = 'backend'
ON CONFLICT DO NOTHING;

INSERT INTO post (slug, title, excerpt, content, status, category_id, publish_time)
SELECT
    'system-architecture-overview',
    '现代系统架构设计概览',
    '从宏观角度理解现代系统架构。',
    '现代系统通常采用微服务架构。',
    1,
    id,
    NOW()
FROM post_category WHERE slug = 'architecture'
ON CONFLICT DO NOTHING;

INSERT INTO post (slug, title, excerpt, content, status, category_id, publish_time)
SELECT
    'performance-tuning-guide',
    '数据库性能调优指南',
    '数据库性能问题的常见分析思路。',
    '数据库性能调优需要从多个方面综合考虑。',
    1,
    id,
    NOW()
FROM post_category WHERE slug = 'database'
ON CONFLICT DO NOTHING;

INSERT INTO post_tag_rel (post_id, tag_id)
SELECT p.id, t.id
FROM post p JOIN post_tag t ON t.slug IN ('postgresql', 'fulltext', 'performance')
WHERE p.slug = 'postgres-fulltext-search'
ON CONFLICT DO NOTHING;

INSERT INTO post_tag_rel (post_id, tag_id)
SELECT p.id, t.id
FROM post p JOIN post_tag t ON t.slug IN ('postgresql', 'performance')
WHERE p.slug = 'postgres-index-optimization'
ON CONFLICT DO NOTHING;

INSERT INTO post_tag_rel (post_id, tag_id)
SELECT p.id, t.id
FROM post p JOIN post_tag t ON t.slug = 'design'
WHERE p.slug = 'backend-service-design'
ON CONFLICT DO NOTHING;

INSERT INTO post_tag_rel (post_id, tag_id)
SELECT p.id, t.id
FROM post p JOIN post_tag t ON t.slug = 'design'
WHERE p.slug = 'system-architecture-overview'
ON CONFLICT DO NOTHING;

INSERT INTO post_tag_rel (post_id, tag_id)
SELECT p.id, t.id
FROM post p JOIN post_tag t ON t.slug IN ('postgresql', 'performance')
WHERE p.slug = 'performance-tuning-guide'
ON CONFLICT DO NOTHING;

COMMIT;
