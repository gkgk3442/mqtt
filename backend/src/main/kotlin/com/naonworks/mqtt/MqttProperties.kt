package com.naonworks.mqtt

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "mqtt")
data class MqttProperties(
    @field:NotBlank
    val serverUri: String,

    @field:NotBlank
    val clientId: String,

    @field:NotNull
    val qos: Int
)