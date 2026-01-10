package com.szl.szlx23api.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "post_tag", schema = "public")
@Getter
@Setter
public class PostTag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String slug;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "post_count", nullable = false)
    private Integer postCount = 0;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();
}
