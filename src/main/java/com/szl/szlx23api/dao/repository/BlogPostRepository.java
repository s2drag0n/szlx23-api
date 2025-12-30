package com.szl.szlx23api.dao.repository;

import com.szl.szlx23api.dao.entity.BlogPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BlogPostRepository extends JpaRepository<BlogPost, UUID>, JpaSpecificationExecutor<BlogPost> {

    Page<BlogPost> findByCategoryId(Long categoryId, Pageable pageable);

}