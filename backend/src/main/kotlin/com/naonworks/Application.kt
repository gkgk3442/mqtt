package com.naonworks

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication(proxyBeanMethods = false)
@OpenAPIDefinition(info = Info(title = "MQTT", description = "Documentation APIs"))
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
