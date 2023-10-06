package com.naonworks.module.modbus.mapstruct

import com.naonworks.entity.log.tables.pojos.ServerLogPojo
import com.naonworks.entity.log.tables.records.ServerLogRecord
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface ServerLogMapper {
    companion object {
        var INSTANCE: ServerLogMapper = Mappers.getMapper(ServerLogMapper::class.java)
    }

    fun recordToPojo(record: ServerLogRecord?): ServerLogPojo?
}