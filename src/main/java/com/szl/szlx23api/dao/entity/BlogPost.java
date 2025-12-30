package com.szl.szlx23api.dao.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Table(name = "blog_posts")
public class BlogPost implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "excerpt")
    private String excerpt;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "status")
    private String status;

    @Column(name = "is_featured")
    private Boolean featured;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "read_time")
    private Long readTime;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "search_vector")
    private String searchVector;

    @Getter
    public enum Status {
        DRAFT("draft"),
        PUBLISHED("published"),
        ARCHIVED("archived");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public static Status fromValue(String value) {
            for (Status status : values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("未知的状态: " + value);
        }
    }

}
