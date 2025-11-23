package com.example;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "User Service API",
                version = "1.0",
                description = "Документация CRUD сервиса пользователей. Включает HATEOAS-ссылки для навигации между ресурсами."
        )
)
public class OpenApiConfig {}
