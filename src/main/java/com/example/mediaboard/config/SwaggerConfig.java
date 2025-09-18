package com.example.mediaboard.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI mediaboardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Media Board API")
                        .description("미디어 중심의 게시판 백엔드 API 문서입니다. " +
                                   "사진과 동영상을 중심으로 한 소셜 미디어 플랫폼의 핵심 기능들을 제공합니다.")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Media Board Team")
                                .email("contact@mediaboard.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("로컬 개발 서버"),
                        new Server()
                                .url("https://api.mediaboard.com")
                                .description("프로덕션 서버 (예시)")
                ));
    }
}
