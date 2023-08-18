package com.naonworks.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.naonworks.config.rest.RestError
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

//@ControllerAdvice
@RestControllerAdvice
class RestControllerExceptionHandler(
    private val objMapper: ObjectMapper,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(Exception::class)
    fun anyException(
        exchange: ServerWebExchange?,
        ex: Exception
    ): Mono<Void> = mono {
        log.error("미정의 에러", ex)

        process(exchange, ex).awaitSingleOrNull()
    }.then()

    @ExceptionHandler(value = [javax.net.ssl.SSLHandshakeException::class, java.nio.channels.ClosedChannelException::class, reactor.netty.channel.AbortedException::class])
    fun exception500(exchange: ServerWebExchange?, ex: Exception): Mono<Void> = mono {
        process(exchange, ex, HttpStatus.INTERNAL_SERVER_ERROR).awaitSingleOrNull()
    }.then()

//    @ExceptionHandler(value = [MethodNotAllowedException::class])
//    fun exception405(exchange: ServerWebExchange?, ex: MethodNotAllowedException): Mono<Void> = mono {
//        process(exchange, ex, HttpStatus.METHOD_NOT_ALLOWED).awaitSingleOrNull()
//    }.then()
//
//    @ExceptionHandler(value = [ServerWebInputException::class, BindException::class])
//    fun exception404(exchange: ServerWebExchange?, ex: Exception): Mono<Void> = mono {
//        process(exchange, ex, HttpStatus.BAD_REQUEST).awaitSingleOrNull()
//    }.then()

    private fun process(
        exchange: ServerWebExchange?,
        ex: Exception,
        status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR
    ): Mono<Void> = mono {
        log.error("{} - {}", ex::class.qualifiedName, ex.message)

        if (exchange != null) {
            val error = RestError(ex)
            val bytes = objMapper.writeValueAsBytes(error)

            val buffer = exchange.response.bufferFactory().wrap(bytes)

            exchange.response.headers.contentType = MediaType.APPLICATION_JSON
            exchange.response.statusCode = status
            exchange.response.writeWith(mono { buffer }).awaitSingleOrNull()
        }
    }.then()
}