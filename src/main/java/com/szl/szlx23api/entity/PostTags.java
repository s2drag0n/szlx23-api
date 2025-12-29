package com.szl.szlx23api.entity;

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

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Table(name = "post_tags")
public class PostTags implements Serializable {

    @Serial
    @Id
    private static final long serialVersionUID = 1L;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "tag_id", nullable = false)
    private Long tagId;

}
