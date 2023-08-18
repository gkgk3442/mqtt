package com.naonworks.modbus.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class ModbusLogRequest(
    @field:NotNull
    var datetime: OffsetDateTime?,

    @field:NotNull
    var level: EModbusLogLevel?,

    @field:NotBlank
    var description: String?
) {
    fun toDto() = ModbusLogDto(
        datetime
            ?.atZoneSameInstant(ZoneId.systemDefault())
            ?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        level,
        description
    )
}