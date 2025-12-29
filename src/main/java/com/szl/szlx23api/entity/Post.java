package com.szl.szlx23api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "slug", nullable = false, length = 200)
    private String slug;

    @Column(name = "excerpt", length = Integer.MAX_VALUE)
    private String excerpt;

    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "cover_image", length = 500)
    private String coverImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "category_id")
    private Category category;

    @ColumnDefault("'draft'")
    @Column(name = "status", length = 20)
    private String status;

    @ColumnDefault("false")
    @Column(name = "is_featured")
    private Boolean isFeatured;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private Integer viewCount;

    @Column(name = "read_time")
    private Integer readTime;

    @Column(name = "published_at")
    private OffsetDateTime publishedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

/*
 TODO [逆向工程]会创建字段来映射 'search_vector' 列
 可用操作: 定义目标 Java 类型 | 按原样取消注释 | 移除列映射
    @Column(name = "search_vector", columnDefinition = "tsvector")
    private Object searchVector;
*/
}