package com.naonworks.common.config.jooq

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "web.r2dbc.init")
data class JooqProperties(
    val enabled: Boolean = false,

    val schemaLocations: List<String> = emptyList(),

    val dataLocations: List<String> = emptyList(),
)