BEGIN;

-- 按依赖关系顺序清空
TRUNCATE TABLE post_views;
TRUNCATE TABLE comments;
TRUNCATE TABLE post_tags;
TRUNCATE TABLE posts;
TRUNCATE TABLE tags;
TRUNCATE TABLE categories RESTART IDENTITY;

-- 验证是否清空成功
DO $$
BEGIN
    IF (SELECT COUNT(*) FROM posts) > 0 THEN
        RAISE EXCEPTION '清空数据失败，posts表仍有数据';
END IF;

    IF (SELECT COUNT(*) FROM categories) > 0 THEN
        RAISE EXCEPTION '清空数据失败，categories表仍有数据';
END IF;
END
$$;

COMMIT;

SELECT '所有测试数据已清空完成' as result;