package com.szl.szlx23api.dao.repository;

import com.szl.szlx23api.dao.entity.BlogTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogTagRepository extends JpaRepository<BlogTag, Long>, JpaSpecificationExecutor<BlogTag> {

}