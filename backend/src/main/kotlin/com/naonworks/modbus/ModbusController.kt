package com.naonworks.modbus

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.naonworks.config.rest.RestError
import com.naonworks.modbus.dto.ModbusEthernetDto
import com.naonworks.modbus.dto.ModbusLogDto
import com.naonworks.mqtt.MqttService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@RestController
@RequestMapping("/api/modbus")
class ModbusController(
    private val service: MqttService,
    private val objectMapper: ObjectMapper,
    private val validator: SmartValidator,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @PostMapping(
        path = ["/ethernet"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun ethernet(
        exchange: ServerWebExchange,

        @RequestBody
        @JsonFormat
        body: ModbusEthernetDto,
    ): ResponseEntity<Any> {
        val e = BeanPropertyBindingResult(body, "")

        log.debug("body : {}", body)

        validator.validate(body, e)

        if (!e.hasErrors()) {
            val topic = "modbus_ethernet"

            val json = objectMapper.writeValueAsString(body)

            log.debug("publish payload : ${json}")

            if (!service.publish(topic, json))
                return ResponseEntity.internalServerError().build()
        }

        if (e.hasErrors())
            return ResponseEntity.badRequest().body(RestError(e))

        return ResponseEntity.ok().build()
    }

    @GetMapping(path = ["/log"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun streamLog(
        exchange: ServerWebExchange,
    ): Flow<ServerSentEvent<ModbusLogDto>> {
        val topic = "log"

        return service.subscribe(topic)
            .map {
                val payloadString = String(it.payload)
                log.debug("subscribe payload : ${payloadString}")

                val dto = objectMapper.readValue<ModbusLogDto>(payloadString)
                ServerSentEvent.builder(dto).build()
            }
    }
}