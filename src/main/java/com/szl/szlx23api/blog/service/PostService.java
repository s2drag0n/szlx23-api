package com.szl.szlx23api.blog.service;

import com.szl.szlx23api.blog.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {

    PageDto<PostListDto> searchPosts(String categorySlug, List<String> tagSlugs, String keyword, Pageable pageable);

    PostDto findBySlug(String slug);

    List<PostCategoryDto> findAllCategories();

    List<PostTagDto> findAllTags();

}
