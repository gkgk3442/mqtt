package com.naonworks.test

import com.naonworks.common.config.jooq.JooqProperties
import com.naonworks.common.config.jooq.JooqR2dbcConfig
import com.naonworks.entity.log.tables.pojos.ServerLogPojo
import com.naonworks.module.modbus.ModbusController
import com.naonworks.module.mqtt.MqttProperties
import com.naonworks.module.mqtt.MqttService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@ExtendWith(value = [SpringExtension::class])
@EnableConfigurationProperties(value = [JooqProperties::class, MqttProperties::class])
@Import(value = [R2dbcAutoConfiguration::class, JooqR2dbcConfig::class, MqttService::class])
@WebFluxTest(ModbusController::class)
class ApplicationTests {

    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun contextLoads() {
        val res = webClient.get().uri("/api/modbus/log").exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(ServerLogPojo::class.java).returnResult()

        log.error("{}", res.responseBody)

        val res2 = webClient.get().uri("/api/modbus/getClientId").exchange()
            .expectStatus().is2xxSuccessful
            .expectBodyList(String::class.java).returnResult()

        log.warn("{}", res2.responseBody)
    }

}
