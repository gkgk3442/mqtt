package com.naonworks.modbus.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ModbusLogDto(
    @field:NotBlank
    var datetime: String?,

    @field:NotNull
    @field:JsonFormat(shape = JsonFormat.Shape.NUMBER)
    var level: EModbusLogLevel?,

    @field:NotBlank
    var description: String?
)