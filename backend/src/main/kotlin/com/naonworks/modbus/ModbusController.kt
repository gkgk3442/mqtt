package com.naonworks.modbus

import com.fasterxml.jackson.databind.ObjectMapper
import com.naonworks.mqtt.MqttService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.util.*
import kotlin.random.Random

@Tag(name = "MODBUS")
@RestController
@RequestMapping("/api/modbus")
class ModbusController(
    private val mqttService: MqttService,
    private val objectMapper: ObjectMapper,
    private val validator: SmartValidator,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @Operation(summary = "sse system subscribe")
    @GetMapping(path = ["/sse/system"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun systemSubscribe(
        exchange: ServerWebExchange,
    ): Flow<ServerSentEvent<String>> =
        mqttService.subscribe("/log/system").map { ServerSentEvent.builder<String>().data(String(it.payload)).build() }

    @Operation(summary = "sse trans subscribe")
    @GetMapping(path = ["/sse/trans"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun transSubscribe(
        exchange: ServerWebExchange,
    ): Flow<ServerSentEvent<String>> =
        mqttService.subscribe("/log/trans").map { ServerSentEvent.builder<String>().data(String(it.payload)).build() }

    @Operation(summary = "get client id")
    @GetMapping(path = ["/getClientId"], produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun clientId(
        exchange: ServerWebExchange,
    ): ResponseEntity<String> {
        val byteArr = ByteArray(16)

        Random.nextBytes(byteArr)

        val body = HexFormat.of().formatHex(byteArr)

        return ResponseEntity.ok(body)
    }

    @Operation(summary = "publish id")
    @PostMapping(
        path = ["/id"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postId(
        exchange: ServerWebExchange,

        @RequestBody
        body: String,
    ): ResponseEntity<String> {
        mqttService.publish("/id", body.toByteArray())

        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish exit")
    @PostMapping(
        path = ["/exit"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postExit(
        exchange: ServerWebExchange,

        @RequestBody
        body: String,
    ): ResponseEntity<String> {
        mqttService.publish("/modbus/exit", body.toByteArray())

        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish reader")
    @PostMapping(
        path = ["/reader"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postReader(
        exchange: ServerWebExchange,

        @RequestBody
        body: String,
    ): ResponseEntity<String> {
        mqttService.publish("/modbus/master/read", body.toByteArray())

        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish writer")
    @PostMapping(
        path = ["/writer"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postWriter(
        exchange: ServerWebExchange,

        @RequestBody
        body: String,
    ): ResponseEntity<String> {
        mqttService.publish("/modbus/master/write", body.toByteArray())

        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish MES start")
    @PostMapping(
        path = ["/MSE/start"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postMESStart(
        exchange: ServerWebExchange,

        @RequestBody
        body: String,
    ): ResponseEntity<String> {
        mqttService.publish("/modbus/slave", body.toByteArray())

        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "로그?")
    @GetMapping(path = ["/log"], produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getLog(
        exchange: ServerWebExchange,
    ): ResponseEntity<String> {
//        SELECT * FROM Server_Log

        return ResponseEntity.ok("테스트")
    }


//    @PostMapping(
//        path = ["/req-rep"],
//        consumes = [MediaType.APPLICATION_JSON_VALUE],
//        produces = [MediaType.APPLICATION_JSON_VALUE]
//    )
//    suspend fun reqRep(
//        exchange: ServerWebExchange,
//
//        @RequestBody
//        @JsonFormat
//        body: Any,
//    ): ResponseEntity<Any> {
//        log.debug("body : {}", body)
//
//        val topic = service.genReqTopic()
//
//        val json = objectMapper.writeValueAsString(body)
//
//        val flowDto = service.subscribe(topic)
//            .map {
//                val payloadString = String(it.second.payload)
//                objectMapper.readValue<Any>(payloadString)
//            }
//
//        service.publish(topic, json.toByteArray())
//
//        val dto = flowDto.firstOrNull()
//
//        return dto?.let { ResponseEntity.ok(it) } ?: ResponseEntity.internalServerError().build()
//    }
}