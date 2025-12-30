package com.szl.szlx23api.dao.dto.converter;

import com.szl.szlx23api.dao.dto.request.BlogPostCreateRequest;
import com.szl.szlx23api.dao.dto.response.BlogPostResponse;
import com.szl.szlx23api.dao.dto.response.PageResponse;
import com.szl.szlx23api.dao.entity.BlogPost;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogPostConverter {

    public BlogPost toEntity(BlogPostCreateRequest request) {
        BlogPost blogPost = new BlogPost();

        BeanUtils.copyProperties(request, blogPost);

        return blogPost;
    }

    public BlogPostResponse toResponse(BlogPost blogPost) {
        BlogPostResponse blogPostResponse = new BlogPostResponse();

        BeanUtils.copyProperties(blogPost, blogPostResponse);

        return blogPostResponse;
    }

    public PageResponse<BlogPostResponse> toPageResponse(Page<BlogPost> page) {
        List<BlogPostResponse> content = page.getContent().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());

        PageResponse<BlogPostResponse> response = new PageResponse<>();
        response.setContent(content);
        response.setPage(page.getNumber() + 1);
        response.setSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        return response;
    }
}
