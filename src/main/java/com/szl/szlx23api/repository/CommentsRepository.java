package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentsRepository extends JpaRepository<Comments, String>, JpaSpecificationExecutor<Comments> {

}