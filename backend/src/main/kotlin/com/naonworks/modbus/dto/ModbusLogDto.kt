package com.naonworks.modbus.dto

import com.fasterxml.jackson.annotation.JsonFormat

data class ModbusLogDto(
    var datetime: String?,
    @field:JsonFormat(shape = JsonFormat.Shape.NUMBER)
    var level: EModbusLogLevel?,
    var description: String?
)