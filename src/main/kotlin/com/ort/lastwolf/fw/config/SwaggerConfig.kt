package com.ort.lastwolf.fw.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.core.jackson.ModelResolver
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    open fun modelResolver(objectMapper: ObjectMapper): ModelResolver {
        return ModelResolver(objectMapper)
    }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Lastwolf API")
                    .description("人狼ゲームのバックエンドAPI")
                    .version("1.0.0")
                    .contact(
                        Contact().name("Lastwolf API Support")
                    )
            )
            .servers(
                listOf(
                    Server()
                        .url("/")
                        .description("Current Server")
                )
            )
    }
}
