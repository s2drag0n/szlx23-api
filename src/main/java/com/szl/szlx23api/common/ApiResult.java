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
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResult<T> {
    private Integer code;
    private String message;
    private T data;
    private Long timestamp;

    // 成功响应
    public static <T> ApiResult<T> success(T data) {
        return ApiResult.<T>builder()
                .code(CodeEnum.OK.code)
                .message("OK")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResult<T> success(String message, T data) {
        return ApiResult.<T>builder()
                .code(CodeEnum.OK.code)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    // 失败响应
    public static <T> ApiResult<T> error(String message) {
        return ApiResult.<T>builder()
                .code(CodeEnum.GENERAL_ERROR.code)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResult<T> error(CodeEnum codeEnum, String message) {
        return ApiResult.<T>builder()
                .code(codeEnum.code)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @AllArgsConstructor
    public enum CodeEnum {
        OK(200),
        GENERAL_ERROR(500);

        private final Integer code;
    }

}