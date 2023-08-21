package com.naonworks.modbus.dto

import jakarta.validation.constraints.*

data class ModbusEthernetRequest(
    @field:NotNull
    var protocol: EModbusProtocol?,

    @field:NotBlank
    @field:Size(max = 64)
    var ip: String?,

    @field:NotNull
    @field:Min(0)
    @field:Max(65535)
    var port: Number?,

    @field:NotNull
    @field:Min(10)
    @field:Max(100)
    var transactionDelay: Number?,

    @field:NotNull
    @field:Min(1000)
    @field:Max(3000)
    var timeout: Number?,
) {
    fun toDto() = ModbusEthernetDto(protocol, ip, port?.toInt(), transactionDelay?.toInt(), timeout?.toInt())
}