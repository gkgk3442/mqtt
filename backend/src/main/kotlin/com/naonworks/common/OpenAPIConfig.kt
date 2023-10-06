package com.naonworks.common

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class OpenAPIConfig {

    @Bean
    fun group0(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("0. ETC")
            .pathsToMatch("/api/**")
            .build()
    }

}