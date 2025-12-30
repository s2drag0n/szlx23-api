package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.BlogCategories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogCategoriesRepository extends JpaRepository<BlogCategories, Long>, JpaSpecificationExecutor<BlogCategories> {

}