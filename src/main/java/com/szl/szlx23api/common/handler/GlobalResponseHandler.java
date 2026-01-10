package com.szl.szlx23api.common.handler;

import com.szl.szlx23api.common.ApiResult;
import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.szl.szlx23api.controller")
public class GlobalResponseHandler implements ResponseBodyAdvice<@NonNull Object> {

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 不包装已经是ApiResult类型的响应
        return !returnType.getParameterType().equals(ApiResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
                                  MediaType selectedContentType, Class<?
                    extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        // 处理String类型返回值
        if (body instanceof String) {
            return body;
        }

        // 处理void类型返回值
        if (body == null && returnType.getParameterType().equals(Void.TYPE)) {
            return ApiResult.success(null);
        }

        // 统一包装为ApiResult
        ApiResult<Object> success = ApiResult.success(body);
        System.out.println(success);
        return success;
    }
}