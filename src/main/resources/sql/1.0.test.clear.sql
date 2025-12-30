BEGIN;

-- 按依赖关系顺序清空（外键约束从叶子节点到根节点）
TRUNCATE TABLE blog_post_views;
TRUNCATE TABLE blog_comments;
TRUNCATE TABLE blog_post_tags;
TRUNCATE TABLE blog_posts;
TRUNCATE TABLE blog_tags;
TRUNCATE TABLE blog_categories RESTART IDENTITY;

-- 验证是否清空成功
DO $$
    BEGIN
        IF (SELECT COUNT(*) FROM blog_posts) > 0 THEN
            RAISE EXCEPTION '清空数据失败，blog_posts表仍有数据';
        END IF;

        IF (SELECT COUNT(*) FROM blog_categories) > 0 THEN
            RAISE EXCEPTION '清空数据失败，blog_categories表仍有数据';
        END IF;
    END
$$;

COMMIT;

SELECT '所有测试数据已清空完成' as result;