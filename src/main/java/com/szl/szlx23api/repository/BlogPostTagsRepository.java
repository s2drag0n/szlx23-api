package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.BlogPostTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogPostTagsRepository extends JpaRepository<BlogPostTags, Long>, JpaSpecificationExecutor<BlogPostTags> {

}