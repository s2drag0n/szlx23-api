package com.szl.szlx23api.controller;

import com.szl.szlx23api.dao.dto.converter.BlogPostConverter;
import com.szl.szlx23api.dao.dto.request.BlogPostCreateRequest;
import com.szl.szlx23api.dao.dto.response.BlogPostResponse;
import com.szl.szlx23api.dao.dto.response.PageResponse;
import com.szl.szlx23api.dao.entity.BlogPost;
import com.szl.szlx23api.service.BlogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/szlx23-api/blog")
public class BlogController {

    private final BlogService blogService;

    BlogPostConverter blogPostConverter = new BlogPostConverter();

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/posts/page")
    public PageResponse<BlogPostResponse> getPostsByPage(@RequestParam @Min(1) int page, @RequestParam @Min(1) int size,
                                                         @RequestParam(required = false) Long category) {
        Page<BlogPost> postsByPage = blogService.getPostsByPage(page, size, category);

        return blogPostConverter.toPageResponse(postsByPage);
    }

    @GetMapping("/posts/{slug}")
    BlogPostResponse getPostBySlug(@NotBlank @PathVariable String slug) {
        return null;
    }

    @PostMapping("/posts")
    @PreAuthorize("hasRole('Admin')")
    void createPost(@Valid @RequestBody BlogPostCreateRequest request) {
        BlogPost blogPost = blogPostConverter.toEntity(request);
    }

    @PutMapping("/posts/{id}")
    @PreAuthorize("hasRole('Admin')")
    void updatePost(@NotBlank @PathVariable String id) {
    }

    @DeleteMapping("/posts/{id}")
    @PreAuthorize("hasRole('Admin')")
    void deletePost(@NotBlank @PathVariable String id) {
    }


}
