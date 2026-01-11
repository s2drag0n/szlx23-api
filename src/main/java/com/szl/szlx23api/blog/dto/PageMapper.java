package com.szl.szlx23api.blog.dto;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.function.Function;

@Mapper(componentModel = "spring")
public interface PageMapper {

    default <T, R> PageDto<R> toDto(Page<T> page, Function<T, R> mapper) {

        Page<R> mapedPage = page.map(mapper);

        PageDto<R> dto = new PageDto<>();
        dto.setContent(mapedPage.getContent());
        dto.setPage(mapedPage.getNumber());
        dto.setSize(mapedPage.getSize());
        dto.setTotalElements(mapedPage.getTotalElements());
        dto.setTotalPages(mapedPage.getTotalPages());

        return dto;
    }

    default <T> PageDto<T> toDto(Page<T> page) {
        return toDto(page, Function.identity());
    }
}
