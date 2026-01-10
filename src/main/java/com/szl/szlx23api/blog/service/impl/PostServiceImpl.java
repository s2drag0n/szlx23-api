package com.szl.szlx23api.blog.service.impl;

import com.szl.szlx23api.blog.dto.*;
import com.szl.szlx23api.blog.entity.Post;
import com.szl.szlx23api.blog.entity.PostCategory;
import com.szl.szlx23api.blog.entity.PostTag;
import com.szl.szlx23api.blog.repository.PostCategoryRepository;
import com.szl.szlx23api.blog.repository.PostRepository;
import com.szl.szlx23api.blog.repository.PostTagRepository;
import com.szl.szlx23api.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final PostCategoryRepository postCategoryRepository;
    private final PostCategoryMapper postCategoryMapper;
    private final PostTagRepository postTagRepository;
    private final PostTagMapper postTagMapper;

    @Transactional(readOnly = true)
    public Page<PostListDto> searchPosts(String categorySlug, List<String> tagSlugs, String keyword, Pageable pageable) {
        String processedKeyword = null;

        if (keyword != null && !keyword.isBlank()) {
            processedKeyword = keyword.trim().replaceAll("\\s+", " & ");
        }

        String[] tags = (tagSlugs != null && !tagSlugs.isEmpty()) ? tagSlugs.toArray(new String[0]) : null;

        Page<Post> postPage = postRepository.searchPosts(categorySlug, tags, processedKeyword, pageable);
        return postPage.map(postMapper::toListDto);
    }

    @Override
    public PostDto findBySlug(String slug) {
        return postMapper.toDto(postRepository.findBySlug(slug));
    }

    @Override
    public List<PostCategoryDto> findAllCategories() {
        List<PostCategory> postCategories = postCategoryRepository.findAll();
        return postCategoryMapper.toDtoList(postCategories);
    }

    @Override
    public List<PostTagDto> findAllTags() {
        List<PostTag> postTags = postTagRepository.findAll();
        return postTagMapper.toDtoList(postTags);
    }
}
