package com.szl.szlx23api.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "post_category", schema = "public")
@Getter
@Setter
public class PostCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String slug;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "post_count", nullable = false)
    private Integer postCount = 0;

    @OneToMany(mappedBy = "category")
    private List<Post> posts;
}