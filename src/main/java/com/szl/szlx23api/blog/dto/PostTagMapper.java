package com.szl.szlx23api.blog.dto;

import com.szl.szlx23api.blog.entity.PostTag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostTagMapper {

    PostTagDto toDto(PostTag postTag);

    List<PostTagDto> toDtoList(List<PostTag> postTags);

}
