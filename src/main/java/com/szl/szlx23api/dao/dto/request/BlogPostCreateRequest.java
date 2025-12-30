package com.szl.szlx23api.dao.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BlogPostCreateRequest {

    @NotBlank(message = "文章标题不能为空")
    @Size(min = 2, max = 200, message = "文章标题长度必须在2-200个字符之间")
    private String title;

    @NotBlank(message = "文章内容不能为空")
    @Size(min = 10, message = "文章内容至少需要10个字符")
    private String content;

    @Pattern(regexp = "^$|^(https?|ftp)://[^\\s/$.?#].\\S*$",
            message = "封面图片URL格式不正确")
    @Size(max = 500, message = "封面图片URL不能超过500个字符")
    private String coverImage;

    @NotNull(message = "文章分类不能为空")
    @Positive(message = "分类ID必须为正数")
    private Long categoryId;

    @Pattern(
            regexp = "^(draft|published|archived)$",
            message = "状态必须是 draft、published 或 archived 中的一个"
    )
    private String status;

    public BlogPostCreateRequest() {
        this.status = "draft"; // 默认草稿状态
    }
}
