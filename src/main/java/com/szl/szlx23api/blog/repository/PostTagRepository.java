package com.szl.szlx23api.blog.repository;

import com.szl.szlx23api.blog.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, UUID> {
    Optional<PostTag> findBySlug(String slug);

    @Query("SELECT t FROM PostTag t WHERE t.postCount > 0 ORDER BY t.postCount DESC")
    List<PostTag> findActiveTags();
}
