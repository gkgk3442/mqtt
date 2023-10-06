package com.naonworks.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.web.reactive.config.ResourceHandlerRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer


@Configuration(proxyBeanMethods = false)
class WebFluxConfig : WebFluxConfigurer {
    private val CLASSPATH_RESOURCE_LOCATIONS = arrayOf(
        "classpath:/META-INF/resources/",
        "classpath:/resources/", "classpath:/static/", "classpath:/public/"
    )

    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        configurer.defaultCodecs().maxInMemorySize(-1)
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry
            .setOrder(0)
            .addResourceHandler("", "/**")
            .addResourceLocations(* CLASSPATH_RESOURCE_LOCATIONS)
            .resourceChain(true)
            .addResolver(CustomEncodedResourceResolver(ClassPathResource("/static/index.html")))
    }
}