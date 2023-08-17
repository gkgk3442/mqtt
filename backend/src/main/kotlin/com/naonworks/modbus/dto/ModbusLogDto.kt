package com.naonworks.modbus.dto

data class ModbusLogDto(
    var datetime: String?,
    var level: EModbusLogLevel?,
    var description: String?
)