package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.BlogPostViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogPostViewsRepository extends JpaRepository<BlogPostViews, String>, JpaSpecificationExecutor<BlogPostViews> {

}