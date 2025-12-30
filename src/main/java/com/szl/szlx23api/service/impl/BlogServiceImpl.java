package com.szl.szlx23api.service.impl;

import com.szl.szlx23api.dao.entity.BlogPost;
import com.szl.szlx23api.dao.repository.BlogPostRepository;
import com.szl.szlx23api.service.BlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

    private final BlogPostRepository blogPostRepository;

    @Override
    public Page<BlogPost> getPostsByPage(int page, int size, Long category) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<BlogPost> posts;

        if (category == null) {
            posts = blogPostRepository.findAll(pageable);
        } else {
            posts = blogPostRepository.findByCategoryId(category, pageable);
        }

        return posts;
    }

}
