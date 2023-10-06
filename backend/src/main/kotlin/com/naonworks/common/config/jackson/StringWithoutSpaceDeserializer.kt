package com.naonworks.common.config.jackson

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer


class StringWithoutSpaceDeserializer : JsonDeserializer<String>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): String? {
        p ?: return null

        return p.text.trim()
    }
}