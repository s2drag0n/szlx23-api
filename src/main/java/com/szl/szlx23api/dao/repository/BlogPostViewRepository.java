package com.szl.szlx23api.dao.repository;

import com.szl.szlx23api.dao.entity.BlogPostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogPostViewRepository extends JpaRepository<BlogPostView, String>, JpaSpecificationExecutor<BlogPostView> {

}