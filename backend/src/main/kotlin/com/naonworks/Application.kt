package com.naonworks

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication(proxyBeanMethods = false)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
