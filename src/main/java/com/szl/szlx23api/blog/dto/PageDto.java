package com.szl.szlx23api.blog.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class PageDto<T> {

    private List<T> content;

    private int page;

    private int size;

    private long totalElements;

    private int totalPages;

}
