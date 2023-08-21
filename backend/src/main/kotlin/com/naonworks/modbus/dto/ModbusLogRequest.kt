package com.naonworks.modbus.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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