package com.szl.szlx23api.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "post", schema = "public")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 200)
    private String slug;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "text", nullable = false)
    private String excerpt;

    @Column(columnDefinition = "text", nullable = false)
    private String content;

    @Column(name = "cover_image", nullable = false, length = 500)
    private String coverImage = "https://images.unsplash.com/photo-1555066931-4365d14bab8c";

    @Column(nullable = false)
    private Integer status = 0;

    @Column(nullable = false)
    private Boolean featured = false;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "read_time")
    private Integer readTime; // 由数据库 Trigger 自动计算

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private PostCategory category;

    @ManyToMany
    @JoinTable(
            name = "post_tag_rel",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<PostTag> tags = new HashSet<>();

    @Column(name = "publish_time", insertable = false, updatable = false)
    private OffsetDateTime publishTime;

    @Column(name = "create_time", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime createTime;

    @Column(name = "update_time", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime updateTime;

    // 搜索向量通常不需要在 Java 应用层维护，由数据库 Trigger 处理
    @Column(name = "search_vector", insertable = false, updatable = false)
    private String searchVector;
}
