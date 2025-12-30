package com.szl.szlx23api.dao.repository;

import com.szl.szlx23api.dao.entity.BlogComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogCommentRepository extends JpaRepository<BlogComment, String>, JpaSpecificationExecutor<BlogComment> {

}