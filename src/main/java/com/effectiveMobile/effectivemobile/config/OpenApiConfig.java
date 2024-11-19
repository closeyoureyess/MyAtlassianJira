package com.effectiveMobile.effectivemobile.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(
        title = "Task Management System",
        description = "API таск-трекинговой системы",
        version = "1.0.0",
        contact = @Contact(
                name = "Sirik Vadim",
                email = "vadimsbeller@gmail.com",
                url = "https://t.me/rrazeww"
        )
))
@SecurityScheme(
        name = "JWT",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {
}
