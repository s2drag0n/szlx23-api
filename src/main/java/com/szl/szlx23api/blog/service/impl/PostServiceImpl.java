package com.szl.szlx23api.blog.service.impl;

import com.szl.szlx23api.blog.dto.*;
import com.szl.szlx23api.blog.entity.Post;
import com.szl.szlx23api.blog.entity.PostCategory;
import com.szl.szlx23api.blog.entity.PostTag;
import com.szl.szlx23api.blog.repository.PostCategoryRepository;
import com.szl.szlx23api.blog.repository.PostRepository;
import com.szl.szlx23api.blog.repository.PostTagRepository;
import com.szl.szlx23api.blog.service.PostService;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapper postMapper;

    private final PostCategoryRepository postCategoryRepository;
    private final PostCategoryMapper postCategoryMapper;
    private final PostTagRepository postTagRepository;
    private final PostTagMapper postTagMapper;
    private final PageMapper pageMapper;

    @Transactional(readOnly = true)
    public PageDto<PostListDto> searchPosts(String categorySlug, List<String> tagSlugs, String keyword,
                                            Pageable pageable) {
//        String processedKeyword = null;
//
//        if (keyword != null && !keyword.isBlank()) {
//            processedKeyword = keyword.trim().replaceAll("\\s+", " & ");
//        }
//
//        String[] tags = (tagSlugs != null && !tagSlugs.isEmpty()) ? tagSlugs.toArray(new String[0]) : null;
//
//        Page<Post> postPage = postRepository.searchPosts(categorySlug, tags, processedKeyword, pageable);
//
//        return pageMapper.toDto(postPage, postMapper::toListDto);

        Specification<Post> spec = withDynamicFilters(categorySlug, tagSlugs, keyword);
        return pageMapper.toDto(postRepository.findAll(spec, pageable), postMapper::toListDto);


    }

    public Specification<Post> withDynamicFilters(String categorySlug, List<String> tagSlugs, String searchTerm) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 1. 分类过滤 (ManyToOne)
            // 只有当 categorySlug 有值时才增加过滤条件
            if (categorySlug != null && !categorySlug.trim().isEmpty()) {
                Join<Post, PostCategory> categoryJoin = root.join("category", JoinType.INNER);
                predicates.add(cb.equal(categoryJoin.get("slug"), categorySlug));
            }

            // 2. 标签交集过滤 (ManyToMany)
            // 逻辑：Post 必须同时拥有 tagSlugs 列表中的每一个标签
            if (tagSlugs != null && !tagSlugs.isEmpty()) {
                for (String slug : tagSlugs) {
                    // 为每一个传入的标签创建一个子查询，确保 Post 关联了该 slug
                    Subquery<UUID> subquery = query.subquery(UUID.class);
                    Root<Post> subRoot = subquery.from(Post.class);
                    Join<Post, PostTag> tagJoin = subRoot.join("tags");

                    subquery.select(subRoot.get("id"))
                            .where(cb.and(
                                    cb.equal(subRoot.get("id"), root.get("id")),
                                    cb.equal(tagJoin.get("slug"), slug)
                            ));
                    predicates.add(cb.exists(subquery));
                }
            }

            // 3. 全文检索 (PostgreSQL TSVector)
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                // 1. 中文补空格处理
                String processedTerm = searchTerm.replaceAll("([\\u4e00-\\u9fff])", "$1 ");

                // 2. 前缀搜索处理：将每个单词后面加上 :*
                // 例如 "java spr" 变成 "java:* & spr:*"
                // 注意：这里用 & 连接是因为通常搜索多个词是 AND 关系
                String prefixTerm = processedTerm.trim().replaceAll("([^\\s]+)", "$1:*");
                prefixTerm = prefixTerm.replaceAll("\\s+", " & ");

                // 调用我们在数据库中定义的函数
                // 建议使用 to_tsquery 而不是 plainto_tsquery，因为后者会转义掉 :*
                Expression<Boolean> tsMatch = cb.function(
                        "fts_match", // 建议起个新名或更新原函数
                        Boolean.class,
                        root.get("searchVector"),
                        cb.literal(prefixTerm)
                );
                predicates.add(cb.isTrue(tsMatch));
            }

            // 默认按发布时间倒序 (如果 query 对象允许)
            query.orderBy(cb.desc(root.get("publishTime")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
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
