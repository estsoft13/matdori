package com.estsoft13.matdori.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Tag(name = "블로그 CRUD")
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("Matdori API") // API 제목
                .description("맛도리 API") // API 설명
                .version("1.0.0"); // API 버전
    }
}
