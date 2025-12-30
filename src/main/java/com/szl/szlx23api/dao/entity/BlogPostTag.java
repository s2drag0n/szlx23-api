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
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Table(name = "blog_post_tags")
public class BlogPostTag implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Id
    @Column(name = "tag_id", nullable = false)
    private Long tagId;

}
