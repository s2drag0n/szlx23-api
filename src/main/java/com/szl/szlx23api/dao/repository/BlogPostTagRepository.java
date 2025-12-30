package com.szl.szlx23api.dao.repository;

import com.szl.szlx23api.dao.entity.BlogPostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogPostTagRepository extends JpaRepository<BlogPostTag, Long>, JpaSpecificationExecutor<BlogPostTag> {

}