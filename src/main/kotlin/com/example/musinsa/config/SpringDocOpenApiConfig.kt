package com.example.musinsa.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Pageable

@Configuration
class SpringDocOpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        SpringDocUtils.getConfig().replaceWithClass(
            Pageable::class.java,
            org.springdoc.core.converters.models.Pageable::class.java
        )

        return OpenAPI()
            .components(
                Components().addSecuritySchemes(
                    "basicScheme",
                    SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")
                )
            )
            .info(
                Info()
                    .title("Musinsa API")
                    .version("1.0.0")
                    .description("무신사 과제 REST API 입니다.")
            )
    }
}
