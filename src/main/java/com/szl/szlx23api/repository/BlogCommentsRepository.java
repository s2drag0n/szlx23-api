package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.BlogComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogCommentsRepository extends JpaRepository<BlogComments, String>, JpaSpecificationExecutor<BlogComments> {

}