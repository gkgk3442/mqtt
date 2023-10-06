package com.naonworks.common.config

import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.core.io.Resource
import org.springframework.web.reactive.resource.EncodedResourceResolver
import org.springframework.web.reactive.resource.ResourceResolverChain
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class CustomEncodedResourceResolver(private val defaultResource: Resource) : EncodedResourceResolver() {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    override fun resolveResource(
        exchange: ServerWebExchange?,
        requestPath: String,
        locations: MutableList<out Resource>,
        chain: ResourceResolverChain
    ): Mono<Resource> = mono {
        var resource = super.resolveResource(exchange, requestPath, locations, chain).awaitSingleOrNull()

        if (resource == null) {
            log.warn("not found request path : {}, return {}", requestPath, defaultResource)

            resource = defaultResource
        }

        resource
    }

}