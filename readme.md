博客系统API接口列表

文章接口

GET /api/v1/posts - 获取文章列表

GET /api/v1/posts/{slug} - 获取文章详情

POST /api/v1/posts - 创建文章（需认证）

PUT /api/v1/posts/{id} - 更新文章（需认证）

DELETE /api/v1/posts/{id} - 删除文章（需认证）

分类接口

GET /api/v1/categories - 获取所有分类

GET /api/v1/categories/{slug} - 获取分类详情

POST /api/v1/categories - 创建分类（需认证）

PUT /api/v1/categories/{id} - 更新分类（需认证）

DELETE /api/v1/categories/{id} - 删除分类（需认证）

标签接口

GET /api/v1/tags - 获取所有标签

GET /api/v1/tags/{slug} - 获取标签详情

POST /api/v1/tags - 创建标签（需认证）

PUT /api/v1/tags/{id} - 更新标签（需认证）

DELETE /api/v1/tags/{id} - 删除标签（需认证）

评论接口

GET /api/v1/posts/{post_id}/comments - 获取文章评论

POST /api/v1/posts/{post_id}/comments - 创建评论

PUT /api/v1/comments/{id}/approve - 批准评论（需认证）

DELETE /api/v1/comments/{id} - 删除评论（需认证）

统计接口

POST /api/v1/posts/{id}/view - 记录文章浏览

POST /api/v1/posts/{id}/like - 点赞文章（需认证）

DELETE /api/v1/posts/{id}/like - 取消点赞（需认证）

POST /api/v1/posts/{id}/bookmark - 收藏文章（需认证）

DELETE /api/v1/posts/{id}/bookmark - 取消收藏（需认证）

搜索接口

GET /api/v1/search - 搜索文章