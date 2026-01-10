package com.szl.szlx23api.blog.repository;

import com.szl.szlx23api.blog.entity.PostCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, UUID> {
    Optional<PostCategory> findBySlug(String slug);
}
