package com.szl.szlx23api.blog.dto;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record PostDto(
        UUID id,
        String title,
        String slug,
        String excerpt,
        String coverImage,
        Long viewCount,
        Integer readTime,
        OffsetDateTime publishedTime,
        String categoryName,
        List<String> tagNames,
        String content
) {
}
