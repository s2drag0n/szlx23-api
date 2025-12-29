package com.szl.szlx23api.common;


import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> {
    private Boolean success;
    private String code;
    private String message;
    private T data;
    private Long timestamp;

    // 成功响应
    public static <T> ApiResult<T> success(T data) {
        return ApiResult.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message("操作成功")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return ApiResult.<T>builder()
                .success(true)
                .code("SUCCESS")
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    // 失败响应
    public static <T> ApiResult<T> error(String code, String message) {
        return ApiResult.<T>builder()
                .success(false)
                .code(code)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResult<T> error(String code, String message, T data) {
        return ApiResult.<T>builder()
                .success(false)
                .code(code)
                .data(data)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}