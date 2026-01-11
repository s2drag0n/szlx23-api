package com.szl.szlx23api.common.handler;

import com.szl.szlx23api.common.ApiResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(basePackages = "com.szl.szlx23api") // 扩大范围
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除掉已经是 ApiResult 的情况，以及 swagger 等资源请求
        return !returnType.getParameterType().isAssignableFrom(ApiResult.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        // 关键：如果返回是 String，需要特殊处理，否则会报类型转换异常
        if (body instanceof String) {
            // 可以手动转 JSON 字符串，或者让 Spring 重新选转换器
            // 这里建议简单处理：
            return ApiResult.success(body);
        }

        if (body instanceof ApiResult) {
            return body;
        }

        return ApiResult.success(body);
    }
}