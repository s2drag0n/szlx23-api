-- 这会删除 public 模式下的所有表、视图、索引、序列等
DO $$
BEGIN
    RAISE NOTICE '开始删除所有表...';

    -- 禁用触发器避免外键约束问题
    SET session_replication_role = 'replica';

    -- 删除整个模式并重新创建
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

-- 重新启用触发器
SET session_replication_role = 'origin';

    -- 重新授权
    GRANT ALL ON SCHEMA public TO postgres;
    GRANT ALL ON SCHEMA public TO public;
    COMMENT ON SCHEMA public IS '标准公共模式';

    RAISE NOTICE '已删除所有表，public 模式已重新创建';

    -- 重新创建扩展
    CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
    CREATE EXTENSION IF NOT EXISTS "pg_trgm";

    RAISE NOTICE '已重新创建扩展';
END
$$;
