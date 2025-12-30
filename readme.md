# 博客系统 API 接口 Controller 方法命名

## 文章接口 (PostController)
• `GET /api/v1/posts` → `getPosts()`

• `GET /api/v1/posts/{slug}` → `getPostBySlug()`

• `POST /api/v1/posts` → `createPost()`

• `PUT /api/v1/posts/{id}` → `updatePost()`

• `DELETE /api/v1/posts/{id}` → `deletePost()`


## 分类接口 (CategoryController)
• `GET /api/v1/categories` → `getCategories()`

• `GET /api/v1/categories/{slug}` → `getCategoryBySlug()`

• `POST /api/v1/categories` → `createCategory()`

• `PUT /api/v1/categories/{id}` → `updateCategory()`

• `DELETE /api/v1/categories/{id}` → `deleteCategory()`


## 标签接口 (TagController)
• `GET /api/v1/tags` → `getTags()`

• `GET /api/v1/tags/{slug}` → `getTagBySlug()`

• `POST /api/v1/tags` → `createTag()`

• `PUT /api/v1/tags/{id}` → `updateTag()`

• `DELETE /api/v1/tags/{id}` → `deleteTag()`


## 评论接口 (CommentController)
• `GET /api/v1/posts/{post_id}/comments` → `getPostComments()`

• `POST /api/v1/posts/{post_id}/comments` → `createComment()`

• `PUT /api/v1/comments/{id}/approve` → `approveComment()`

• `DELETE /api/v1/comments/{id}` → `deleteComment()`


## 统计接口 (StatController)
• `POST /api/v1/posts/{id}/view` → `recordPostView()`

• `POST /api/v1/posts/{id}/like` → `likePost()`

• `DELETE /api/v1/posts/{id}/like` → `unlikePost()`

• `POST /api/v1/posts/{id}/bookmark` → `bookmarkPost()`

• `DELETE /api/v1/posts/{id}/bookmark` → `unbookmarkPost()`


## 搜索接口 (SearchController)
• `GET /api/v1/search` → `searchPosts()`