package com.szl.szlx23api.common.handler;

import com.szl.szlx23api.common.ApiResult;
import com.szl.szlx23api.common.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResult<Object> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {}, URL: {}", e.getMessage(), request.getRequestURI(), e);
        return ApiResult.error(e.getMessage());
    }

    /**
     * 处理参数校验异常 (MethodArgumentNotValidException)
     * 用于@Validated注解的参数校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        log.warn("参数校验失败: {}, URL: {}", e.getMessage(), request.getRequestURI());

        // 提取所有字段错误
        Map<String, String> errors =
                e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() :
                                "参数错误"));

        return ApiResult.error("参数校验失败：" + errors);
    }

    /**
     * 处理参数绑定异常 (BindException)
     * 用于非@RequestBody的参数绑定校验
     */
    @ExceptionHandler(BindException.class)
    public ApiResult<Object> handleBindException(BindException e, HttpServletRequest request) {
        log.warn("参数绑定失败: {}, URL: {}", e.getMessage(), request.getRequestURI());

        Map<String, String> errors =
                e.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() :
                                "参数错误"));

        return ApiResult.error("参数绑定失败：" + errors);
    }

    /**
     * 处理约束违反异常 (ConstraintViolationException)
     * 用于方法级别的参数校验
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResult<Object> handleConstraintViolationException(ConstraintViolationException e,
                                                                HttpServletRequest request) {
        log.warn("约束校验失败: {}, URL: {}", e.getMessage(), request.getRequestURI());

        Map<String, String> errors =
                e.getConstraintViolations().stream().collect(Collectors.toMap(violation ->
                        violation.getPropertyPath().toString(), ConstraintViolation::getMessage));

        return ApiResult.error("约束校验失败：" + errors);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResult<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                                       HttpServletRequest request) {
        log.warn("参数类型不匹配: {}, 参数: {}, 值: {}, URL: {}", e.getMessage(), e.getName(), e.getValue(),
                request.getRequestURI());

        String message = String.format("参数 '%s' 类型不匹配，期望类型: %s", e.getName(), e.getRequiredType() != null ?
                e.getRequiredType().getSimpleName() : "未知");

        return ApiResult.error(message);
    }

    /**
     * 处理缺少必要参数异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResult<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                           HttpServletRequest request) {
        log.warn("缺少必要参数: {}, URL: {}", e.getMessage(), request.getRequestURI());

        String message = String.format("缺少必要参数: %s", e.getParameterName());
        return ApiResult.error(message);
    }

    /**
     * 处理HTTP消息不可读异常 (如JSON解析错误)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResult<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e,
                                                                   HttpServletRequest request) {
        log.warn("HTTP消息不可读: {}, URL: {}", e.getMessage(), request.getRequestURI());

        return ApiResult.error("请求体格式错误，请检查JSON格式");
    }

    /**
     * 处理HTTP方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResult<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                          HttpServletRequest request) {
        log.warn("HTTP方法不支持: {}, URL: {}", e.getMessage(), request.getRequestURI());

        String message = String.format("请求方法 '%s' 不支持，支持的请求方法: %s", e.getMethod(), String.join(", ",
                e.getSupportedMethods()));

        return ApiResult.error(message);
    }

    /**
     * 处理404资源未找到异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<Object> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("接口不存在: {}, URL: {}", e.getMessage(), request.getRequestURI());

        return ApiResult.error("接口不存在: " + e.getRequestURL());
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public ApiResult<Object> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("数据库操作异常: {}, URL: {}", e.getMessage(), request.getRequestURI(), e);

        // 根据SQL状态码返回不同的错误信息
        String errorCode = "DATABASE_ERROR";
        String errorMessage = "数据库操作异常";

        // 这里可以根据SQLState进行更精细的错误处理
        if (e.getSQLState() != null) {
            if (e.getSQLState().startsWith("23")) { // 完整性约束违反
                errorCode = "DATA_INTEGRITY_VIOLATION";
                errorMessage = "数据完整性约束违反";
            } else if (e.getSQLState().startsWith("40")) { // 事务相关错误
                errorCode = "TRANSACTION_ERROR";
                errorMessage = "事务处理异常";
            }
        }

        return ApiResult.error(errorMessage);
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ApiResult<Object> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {}, URL: {}", e.getMessage(), request.getRequestURI(), e);

        // 生产环境可以隐藏详细的错误信息
        String message = "系统内部错误";
        if (isDevelopmentEnvironment()) {
            message = e.getMessage();
        }

        return ApiResult.error(message);
    }

    /**
     * 判断是否为开发环境
     */
    private boolean isDevelopmentEnvironment() {
        // 这里可以根据你的环境配置来判断
        // 例如: return "dev".equals(environment.getProperty("spring.profiles.active"));
        return true; // 默认返回true，实际项目中需要根据环境配置
    }
}