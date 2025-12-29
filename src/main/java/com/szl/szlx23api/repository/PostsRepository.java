package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostsRepository extends JpaRepository<Posts, String>, JpaSpecificationExecutor<Posts> {

}