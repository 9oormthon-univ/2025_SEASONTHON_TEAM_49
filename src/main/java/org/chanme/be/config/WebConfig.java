package org.chanme.be.config;

import org.chanme.be.auth.CookieAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CookieAuthInterceptor cookieAuthInterceptor;

    public WebConfig(CookieAuthInterceptor cookieAuthInterceptor) {
        this.cookieAuthInterceptor = cookieAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(cookieAuthInterceptor)
                .addPathPatterns("/**")
                // 여긴 접속 편의 위해 예외 처리(원하면 조정 가능)
                .excludePathPatterns(
                        "/health",
                        "/error",
                        "/actuator/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/swagger-ui.html"
                );
    }
}
