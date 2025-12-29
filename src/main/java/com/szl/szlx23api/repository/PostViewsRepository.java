package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.PostViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostViewsRepository extends JpaRepository<PostViews, String>, JpaSpecificationExecutor<PostViews> {

}