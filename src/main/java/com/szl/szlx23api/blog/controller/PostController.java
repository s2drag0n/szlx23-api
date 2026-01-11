package com.szl.szlx23api.blog.controller;

import com.szl.szlx23api.blog.dto.*;
import com.szl.szlx23api.blog.service.PostService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/szlx23-api/blog/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public PageDto<PostListDto> searchPosts(
            @RequestParam(required = false) String categorySlug,
            @RequestParam(required = false) List<String> tagSlugs,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);
        return postService.searchPosts(categorySlug, tagSlugs, keyword, pageable);
    }

    @GetMapping("/{slug}")
    public PostDto findById(@NotBlank @PathVariable String slug) {
        return postService.findBySlug(slug);
    }

    @GetMapping("/categories")
    public List<PostCategoryDto> findAllCategories() {
        return postService.findAllCategories();
    }

    @GetMapping("/tags")
    public List<PostTagDto> findAllTags() {
        return postService.findAllTags();
    }


}
