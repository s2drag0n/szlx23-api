-- =========================
-- init_blog.sql（修正版）
-- =========================
BEGIN;

CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- ---------- drop triggers first ----------
DROP TRIGGER IF EXISTS trg_sync_tag_count ON public.post_tag_rel;
DROP TRIGGER IF EXISTS trg_sync_category_count ON public.post;
DROP TRIGGER IF EXISTS trg_post_search_vector ON public.post;
DROP TRIGGER IF EXISTS trg_post_read_time ON public.post;
DROP TRIGGER IF EXISTS trg_update_post_time ON public.post;

-- ---------- drop functions ----------
DROP FUNCTION IF EXISTS public.sync_post_tag_count();
DROP FUNCTION IF EXISTS public.sync_post_category_count();
DROP FUNCTION IF EXISTS public.update_post_search_vector();
DROP FUNCTION IF EXISTS public.calculate_post_read_time();
DROP FUNCTION IF EXISTS public.update_update_time_column();

-- ---------- drop tables ----------
DROP TABLE IF EXISTS public.post_tag_rel;
DROP TABLE IF EXISTS public.post;
DROP TABLE IF EXISTS public.post_tag;
DROP TABLE IF EXISTS public.post_category;

-- ---------- functions ----------
CREATE OR REPLACE FUNCTION public.update_update_time_column()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    NEW.update_time := CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.calculate_post_read_time()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
DECLARE
    word_count INTEGER;
BEGIN
    IF NEW.content IS NOT NULL THEN
        word_count := array_length(regexp_split_to_array(NEW.content, '\s+'), 1);
        NEW.read_time := CEIL(word_count::FLOAT / 200.0);
    END IF;
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.update_post_search_vector()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    NEW.search_vector :=
            setweight(
                    to_tsvector(
                            'simple',
                            regexp_replace(coalesce(NEW.title, ''),
                                           '([\u4e00-\u9fff])',
                                           '\1 ',
                                           'g')
                    ),
                    'A'
            ) ||
            setweight(
                    to_tsvector(
                            'simple',
                            regexp_replace(coalesce(NEW.excerpt, ''),
                                           '([\u4e00-\u9fff])',
                                           '\1 ',
                                           'g')
                    ),
                    'B'
            ) ||
            setweight(
                    to_tsvector(
                            'simple',
                            regexp_replace(coalesce(NEW.content, ''),
                                           '([\u4e00-\u9fff])',
                                           '\1 ',
                                           'g')
                    ),
                    'C'
            );
    RETURN NEW;
END;
$$;

CREATE OR REPLACE FUNCTION public.sync_post_category_count()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        IF NEW.status = 1 THEN
            UPDATE post_category SET post_count = post_count + 1 WHERE id = NEW.category_id;
        END IF;
        RETURN NEW;
    END IF;

    IF TG_OP = 'DELETE' THEN
        IF OLD.status = 1 THEN
            UPDATE post_category SET post_count = post_count - 1 WHERE id = OLD.category_id;
        END IF;
        RETURN OLD;
    END IF;

    IF TG_OP = 'UPDATE' THEN
        IF OLD.status <> NEW.status THEN
            IF OLD.status <> 1 AND NEW.status = 1 THEN
                UPDATE post_category SET post_count = post_count + 1 WHERE id = NEW.category_id;
            ELSIF OLD.status = 1 AND NEW.status <> 1 THEN
                UPDATE post_category SET post_count = post_count - 1 WHERE id = OLD.category_id;
            END IF;
        END IF;

        IF OLD.category_id <> NEW.category_id AND NEW.status = 1 THEN
            UPDATE post_category SET post_count = post_count - 1 WHERE id = OLD.category_id;
            UPDATE post_category SET post_count = post_count + 1 WHERE id = NEW.category_id;
        END IF;

        RETURN NEW;
    END IF;
END;
$$;

CREATE OR REPLACE FUNCTION public.sync_post_tag_count()
    RETURNS trigger
AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        UPDATE post_tag SET post_count = post_count + 1 WHERE id = NEW.tag_id;
        RETURN NEW;
    END IF;

    IF TG_OP = 'DELETE' THEN
        UPDATE post_tag SET post_count = post_count - 1 WHERE id = OLD.tag_id;
        RETURN OLD;
    END IF;
END;
$$;LANGUAGE plpgsql

CREATE OR REPLACE FUNCTION fn_update_published_time()
    RETURNS TRIGGER AS $$
BEGIN
    -- 当状态变为 1 (已发布) 且当前发布时间为空时，自动填充
    IF NEW.status = 1 AND NEW.published_time IS NULL THEN
        NEW.published_time := CURRENT_TIMESTAMP;
    END IF;

    -- 顺便处理 update_time (如果有这个字段的话)
    NEW.update_time := CURRENT_TIMESTAMP;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ---------- tables ----------
CREATE TABLE public.post_category (
                                      id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                      slug varchar(50) NOT NULL UNIQUE,
                                      name varchar(50) NOT NULL UNIQUE,
                                      post_count int4 NOT NULL DEFAULT 0
);

CREATE TABLE public.post_tag (
                                 id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                                 slug varchar(50) NOT NULL UNIQUE,
                                 name varchar(50) NOT NULL UNIQUE,
                                 post_count int4 NOT NULL DEFAULT 0
);

CREATE TABLE public.post (
                             id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
                             slug varchar(200) NOT NULL UNIQUE,
                             title varchar(200) NOT NULL,
                             excerpt text NOT NULL,
                             content text NOT NULL,
                             cover_image varchar(500) NOT NULL
                                                 DEFAULT 'https://images.unsplash.com/photo-1555066931-4365d14bab8c',
                             status int4 NOT NULL DEFAULT 0,
                             featured boolean NOT NULL DEFAULT false,
                             view_count int8 NOT NULL DEFAULT 0,
                             read_time int4,
                             category_id uuid NOT NULL,
                             published_time timestamptz,
                             create_time timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             update_time timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             search_vector tsvector
);

ALTER TABLE public.post
    ADD CONSTRAINT fk_post_category
        FOREIGN KEY (category_id)
            REFERENCES public.post_category(id)
            ON DELETE RESTRICT;

CREATE TABLE public.post_tag_rel (
                                     post_id uuid NOT NULL,
                                     tag_id uuid NOT NULL,
                                     PRIMARY KEY (post_id, tag_id),
                                     CONSTRAINT fk_ptr_post
                                         FOREIGN KEY (post_id)
                                             REFERENCES public.post(id)
                                             ON DELETE CASCADE,
                                     CONSTRAINT fk_ptr_tag
                                         FOREIGN KEY (tag_id)
                                             REFERENCES public.post_tag(id)
                                             ON DELETE CASCADE
);

-- ---------- indexes ----------
CREATE INDEX idx_post_search_vector ON public.post USING gin (search_vector);
CREATE INDEX idx_post_category ON public.post (category_id);
CREATE INDEX idx_post_tag_rel_tag ON public.post_tag_rel (tag_id);

CREATE INDEX idx_post_published_time ON public.post (published_time DESC);
CREATE INDEX idx_post_tag_rel_ids ON post_tag_rel (post_id, tag_id);
CREATE UNIQUE INDEX idx_post_category_slug ON post_category (slug);
CREATE UNIQUE INDEX idx_post_tag_slug ON post_tag (slug);


-- ---------- triggers ----------
CREATE TRIGGER trg_update_post_time
    BEFORE UPDATE ON public.post
    FOR EACH ROW
EXECUTE FUNCTION update_update_time_column();

CREATE TRIGGER trg_post_read_time
    BEFORE INSERT OR UPDATE ON public.post
    FOR EACH ROW
EXECUTE FUNCTION calculate_post_read_time();

CREATE TRIGGER trg_post_search_vector
    BEFORE INSERT OR UPDATE ON public.post
    FOR EACH ROW
EXECUTE FUNCTION update_post_search_vector();

CREATE TRIGGER trg_sync_category_count
    AFTER INSERT OR UPDATE OR DELETE ON public.post
    FOR EACH ROW
EXECUTE FUNCTION sync_post_category_count();

CREATE TRIGGER trg_sync_tag_count
    AFTER INSERT OR DELETE ON public.post_tag_rel
    FOR EACH ROW
EXECUTE FUNCTION sync_post_tag_count();

CREATE TRIGGER trg_post_before_upsert
    BEFORE INSERT OR UPDATE ON post
    FOR EACH ROW
EXECUTE FUNCTION fn_update_published_time();

COMMIT;
