package com.szl.szlx23api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 方式1：通过 WebMvcConfigurer 配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // 所有接口
                .allowedOriginPatterns("*")  // Spring Boot 2.4+ 用 allowedOriginPatterns
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Authorization", "Content-Disposition")  // 暴露自定义header
                .allowCredentials(true)  // 允许携带cookie
                .maxAge(3600);  // 预检请求缓存时间（秒）
    }

    /**
     * 方式2：通过 CorsFilter 配置（更灵活）
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // 设置允许的域名
        config.addAllowedOriginPattern("*");  // 生产环境建议设置具体域名

        // 设置允许的请求头
        config.addAllowedHeader("*");

        // 设置允许的请求方法
        config.addAllowedMethod("*");

        // 是否允许携带cookie
        config.setAllowCredentials(true);

        // 暴露的响应头
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Disposition");

        // 预检请求的缓存时间
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}