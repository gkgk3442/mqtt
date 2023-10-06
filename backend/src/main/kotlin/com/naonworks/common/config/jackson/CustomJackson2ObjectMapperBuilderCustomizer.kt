package com.naonworks.common.config.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.OffsetDateTime


@Configuration(proxyBeanMethods = false)
class CustomJackson2ObjectMapperBuilderCustomizer : Jackson2ObjectMapperBuilderCustomizer {
    override fun customize(builder: Jackson2ObjectMapperBuilder) {
        builder.featuresToEnable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
        builder.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
        // builder.propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        // builder.serializationInclusion(Include.NON_EMPTY);
        builder.serializationInclusion(JsonInclude.Include.NON_NULL)

        builder.deserializerByType(Number::class.java, CustomNumberDeserializer())
        builder.deserializerByType(OffsetDateTime::class.java, CustomOffsetDateTimeDeserializer())
        builder.deserializerByType(String::class.java, StringWithoutSpaceDeserializer())
    }
}
