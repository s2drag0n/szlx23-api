package com.szl.szlx23api.blog.dto;

import com.szl.szlx23api.blog.entity.Post;
import com.szl.szlx23api.blog.entity.PostTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "tagNames", source = "tags", qualifiedByName = "mapTags")
    PostListDto toListDto(Post post);

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "tagNames", source = "tags", qualifiedByName = "mapTags")
    PostDto toDto(Post post); // 这里的 PostDto 也是 record

    @Named("mapTags")
    default List<String> mapTags(Set<PostTag> tags) {
        if (tags == null) return null;
        return tags.stream().map(PostTag::getName).toList();
    }
}