package com.estsoft13.matdori.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    // img 렌더링 관련
    //정적 리소스 요청에 대해 기본 경로를 설정해주는 방법
    //addResourceHandler에 정의한 루트로 들어오는 모든 정적 리소스 요청을 addResourceLocations에서 정의한 경로에서 찾는다는 의미
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("file:src/main/resources/static/");
    }

    /* 배포시 사용할 코드(위 메소드 주석처리)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:/home/ec2-user/files/");
    }
     */
}
