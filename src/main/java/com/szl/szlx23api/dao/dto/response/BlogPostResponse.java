package com.szl.szlx23api.dao.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class BlogPostResponse {

    private UUID id;

    private String title;

    private String slug;

    private String excerpt;

    private String content;

    private String coverImage;

    private Long categoryId;

    private String status;

    private Boolean featured;

    private Long viewCount;

    private Long readTime;

    private OffsetDateTime publishedAt;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

}
