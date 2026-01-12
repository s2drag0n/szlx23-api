package com.szl.szlx23api.blog.repository;

import com.szl.szlx23api.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID>, JpaSpecificationExecutor<Post> {

    @Query(value = """
            SELECT p.* FROM post p
            WHERE 1=1
              -- 1. 分类过滤 (利用 category_id 索引)
              AND (CAST(:categorySlug AS text) IS NULL OR EXISTS (
                  SELECT 1 FROM post_category c 
                  WHERE c.id = p.category_id AND c.slug = :categorySlug
              ))
                  
              -- 2. 标签交集过滤 (AND 逻辑：只有同时拥有所有传入标签的文章才会被选中)
              AND (CAST(:tagSlugs AS text[]) IS NULL OR (
                  SELECT COUNT(*) FROM post_tag_rel ptr 
                  JOIN post_tag t ON ptr.tag_id = t.id 
                  WHERE ptr.post_id = p.id AND t.slug = ANY(CAST(:tagSlugs AS text[]))
              ) = array_length(CAST(:tagSlugs AS text[]), 1))
            
              -- 3. 全文检索 (利用 GIN 索引)
              AND (
                  CAST(:searchTerm AS text) IS NULL OR CAST(:searchTerm AS text) = '' OR 
                  p.search_vector @@ plainto_tsquery('simple', 
                      regexp_replace(CAST(:searchTerm AS text), '([\\u4e00-\\u9fff])', '\\1 ', 'g')
                  )
              )
            ORDER BY p.publish_time DESC
            """,
            countQuery = """
                    SELECT count(*) FROM post p
                    WHERE 1=1
                      AND (CAST(:categorySlug AS text) IS NULL OR EXISTS (
                          SELECT 1 FROM post_category c WHERE c.id = p.category_id AND c.slug = :categorySlug
                      ))
                      AND (CAST(:tagSlugs AS text[]) IS NULL OR (
                          SELECT COUNT(*) FROM post_tag_rel ptr 
                          JOIN post_tag t ON ptr.tag_id = t.id 
                          WHERE ptr.post_id = p.id AND t.slug = ANY(CAST(:tagSlugs AS text[]))
                      ) = array_length(CAST(:tagSlugs AS text[]), 1))
                      AND (
                          CAST(:searchTerm AS text) IS NULL OR CAST(:searchTerm AS text) = '' OR 
                          p.search_vector @@ plainto_tsquery('simple', 
                              regexp_replace(CAST(:searchTerm AS text), '([\\u4e00-\\u9fff])', '\\1 ', 'g')
                          )
                      )
                    """,
            nativeQuery = true)
    Page<Post> searchPosts(
            @Param("categorySlug") String categorySlug,
            @Param("tagSlugs") String[] tagSlugs,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    Post findBySlug(String slug);

    // 获取加精文章
    List<Post> findByFeaturedTrueAndStatusOrderByPublishTimeDesc(Integer status);

    // 增加阅读量（原子操作）
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") UUID id);
}
