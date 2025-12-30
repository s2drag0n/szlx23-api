package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.BlogTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogTagsRepository extends JpaRepository<BlogTags, Long>, JpaSpecificationExecutor<BlogTags> {

}