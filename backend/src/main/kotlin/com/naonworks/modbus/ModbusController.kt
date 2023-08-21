package com.naonworks.modbus

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.naonworks.config.rest.RestError
import com.naonworks.modbus.dto.ModbusEthernetDto
import com.naonworks.modbus.dto.ModbusEthernetRequest
import com.naonworks.modbus.dto.ModbusLogDto
import com.naonworks.modbus.dto.ModbusLogRequest
import com.naonworks.mqtt.MqttService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange

@Tag(name = "MODBUS")
@RestController
@RequestMapping("/api/modbus")
class ModbusController(
    private val service: MqttService,
    private val objectMapper: ObjectMapper,
    private val validator: SmartValidator,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    private val MODBUS_ETHERNET_TOPIC = "modbus_ethernet"
    private val MODBUS_LOG_TOPIC = "modbus_log"

    @Operation(summary = "req-rep")
    @PostMapping(
        path = ["/req-rep"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun reqRep(
        exchange: ServerWebExchange,

        @RequestBody
        @JsonFormat
        body: String,
    ): ResponseEntity<Any> {
        log.debug("body : {}", body)
        
        val topic = service.genReqTopic()

        val flowDto = service.subscribe(topic)
            .map {
                String(it.second.payload)
            }

        service.publish(topic, body.toByteArray())

        val dto = flowDto.firstOrNull()

        return dto?.let { ResponseEntity.ok(it) } ?: ResponseEntity.internalServerError().build()
    }

    @Operation(summary = "publish - 이더넷 설정")
    @PostMapping(
        path = ["/ethernet"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun publishEthernet(
        exchange: ServerWebExchange,

        @RequestBody
        @JsonFormat
        body: ModbusEthernetRequest,
    ): ResponseEntity<Any> {
        val e = BeanPropertyBindingResult(body, "")

        log.debug("body : {}", body)

        validator.validate(body, e)

        if (!e.hasErrors()) {
            val json = objectMapper.writeValueAsString(body.toDto())

            if (!service.publish(MODBUS_ETHERNET_TOPIC, json.toByteArray()))
                return ResponseEntity.internalServerError().build()
        }

        if (e.hasErrors())
            return ResponseEntity.badRequest().body(RestError(e))

        return ResponseEntity.ok().build()
    }

    @Operation(summary = "subscribe - 이더넷 설정")
    @GetMapping(path = ["/ethernet"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun subscribeEthernet(
        exchange: ServerWebExchange,
    ): Flow<ServerSentEvent<ModbusEthernetDto>> {
        return service.subscribe(MODBUS_ETHERNET_TOPIC)
            .map {
                val payloadString = String(it.second.payload)
                val dto = objectMapper.readValue<ModbusEthernetDto>(payloadString)
                ServerSentEvent.builder(dto).build()
            }
    }

    @Operation(summary = "publish - 로그")
    @PostMapping(
        path = ["/log"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun publishLog(
        exchange: ServerWebExchange,

        @RequestBody
        @JsonFormat
        body: ModbusLogRequest,
    ): ResponseEntity<Any> {
        val e = BeanPropertyBindingResult(body, "")

        log.debug("body : {}", body)

        validator.validate(body, e)

        if (!e.hasErrors()) {
            val json = objectMapper.writeValueAsString(body.toDto())

            if (!service.publish(MODBUS_LOG_TOPIC, json.toByteArray()))
                return ResponseEntity.internalServerError().build()
        }

        if (e.hasErrors())
            return ResponseEntity.badRequest().body(RestError(e))

        return ResponseEntity.ok().build()
    }

    @Operation(summary = "subscribe - 로그")
    @GetMapping(path = ["/log"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun subscribeLog(
        exchange: ServerWebExchange,
    ): Flow<ServerSentEvent<ModbusLogDto>> {
        return service.subscribe(MODBUS_LOG_TOPIC)
            .map {
                val payloadString = String(it.second.payload)
                val dto = objectMapper.readValue<ModbusLogDto>(payloadString)
                ServerSentEvent.builder(dto).build()
            }
    }
}