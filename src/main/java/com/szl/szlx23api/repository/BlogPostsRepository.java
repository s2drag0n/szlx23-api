package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.BlogPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogPostsRepository extends JpaRepository<BlogPosts, String>, JpaSpecificationExecutor<BlogPosts> {

}