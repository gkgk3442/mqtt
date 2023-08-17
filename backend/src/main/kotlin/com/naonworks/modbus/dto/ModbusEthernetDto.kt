package com.naonworks.modbus.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ModbusEthernetDto(
    @field:NotNull
    @field:JsonFormat(shape = JsonFormat.Shape.NUMBER)
    var protocol: EModbusProtocol?,

    @field:NotBlank
    var ip: String?,

    @field:NotNull
    @field:Min(0)
    @field:Max(65535)
    var port: Int?,

    @field:NotNull
    @field:Min(10)
    @field:Max(100)
    var transactionDelay: Int?,

    @field:NotNull
    @field:Min(1000)
    @field:Max(3000)
    var timeout: Int?,
)