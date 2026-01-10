package com.szl.szlx23api.blog.dto;

import java.util.UUID;

public record PostCategoryDto(
        UUID id,
        String slug,
        String name,
        Integer post_count
) {
}
