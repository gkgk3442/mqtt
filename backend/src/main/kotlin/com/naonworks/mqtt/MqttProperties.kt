package com.naonworks.mqtt

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.eclipse.paho.client.mqttv3.MqttClient
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "mqtt")
data class MqttProperties(
    @field:NotBlank
    val serverUri: String,

    val clientId: String = MqttClient.generateClientId(),

    @field:NotNull
    val qos: Int
)