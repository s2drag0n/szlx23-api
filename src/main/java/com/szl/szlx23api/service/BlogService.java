package com.szl.szlx23api.service;

import com.szl.szlx23api.dao.entity.BlogPost;
import org.springframework.data.domain.Page;

public interface BlogService {

    Page<BlogPost> getPostsByPage(int page, int size, Long category);

}
