package com.szl.szlx23api.blog.service;

import com.szl.szlx23api.blog.dto.PostCategoryDto;
import com.szl.szlx23api.blog.dto.PostDto;
import com.szl.szlx23api.blog.dto.PostListDto;
import com.szl.szlx23api.blog.dto.PostTagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    Page<PostListDto> searchPosts(String categorySlug, List<String> tagSlugs, String keyword, Pageable pageable);

    PostDto findBySlug(String slug);

    List<PostCategoryDto> findAllCategories();

    List<PostTagDto> findAllTags();

}
