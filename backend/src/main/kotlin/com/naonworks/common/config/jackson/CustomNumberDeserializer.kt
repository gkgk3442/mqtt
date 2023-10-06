package com.naonworks.common.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class CustomNumberDeserializer : JsonDeserializer<Number>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Number? {
        p ?: return null

        if (p.text.isNullOrBlank()) return null

        return try {
            p.text.toLong()
        } catch (_: Exception) {
            try {
                p.text.toDouble()
            } catch (_: Exception) {
                null
            }
        }
    }
}