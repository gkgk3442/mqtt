package com.naonworks.module.modbus.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@JsonIgnoreProperties(ignoreUnknown = true)
data class ModbusLogDto(
    var datetime: String?,

    @field:JsonFormat(shape = JsonFormat.Shape.NUMBER)
    var level: EModbusLogLevel?,

    var description: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ModbusLogRequest(
    @field:NotNull
    var date: LocalDate,

    @field:NotNull
    var time: LocalTime,

    @field:NotNull
    var level: EModbusLogLevel?,

    @field:NotBlank
    var description: String?
) {
    fun toDto() = ModbusLogDto(
        LocalDateTime.of(date, time)?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        level,
        description
    )
}

enum class EModbusLogLevel {
    TRACE, DEBUG, INFO, WARN, ERROR, FATAL
}