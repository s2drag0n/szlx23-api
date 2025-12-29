package com.szl.szlx23api.repository;

import com.szl.szlx23api.entity.PostTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostTagsRepository extends JpaRepository<PostTags, Long>, JpaSpecificationExecutor<PostTags> {

}