package com.szl.szlx23api.blog.dto;

import com.szl.szlx23api.blog.entity.PostCategory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostCategoryMapper {

    PostCategoryDto toDto(PostCategory postCategory);

    List<PostCategoryDto> toDtoList(List<PostCategory> postCategories);

}
