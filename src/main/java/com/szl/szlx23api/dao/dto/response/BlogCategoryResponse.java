package com.szl.szlx23api.dao.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BlogCategoryResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private String color;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
