package com.naonworks.common.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.OffsetDateTime

class CustomOffsetDateTimeDeserializer : JsonDeserializer<OffsetDateTime>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): OffsetDateTime? {
        p ?: return null

        if (p.text.isNullOrBlank()) return null

        return OffsetDateTime.parse(p.text)
    }
}