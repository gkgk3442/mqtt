package com.naonworks.modbus.dto

import com.fasterxml.jackson.annotation.JsonFormat

data class ModbusEthernetDto(
    @field:JsonFormat(shape = JsonFormat.Shape.NUMBER)
    var protocol: EModbusProtocol?,
    var ip: String?,
    var port: Int?,
    var transactionDelay: Int?,
    var timeout: Int?,
)