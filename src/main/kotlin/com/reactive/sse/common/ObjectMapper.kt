package com.reactive.sse.common

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component


@Component
final class ObjectMapper {
    val jacksonObjectMapper: ObjectMapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())

    init {
        jacksonObjectMapper.enable(DeserializationFeature.USE_LONG_FOR_INTS)
        jacksonObjectMapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        jacksonObjectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }

    fun serialize(obj: Any): String {
        return jacksonObjectMapper.writeValueAsString(obj)
    }


    inline fun <reified T> deserialize(obj: String): T {
        return jacksonObjectMapper.readValue(obj, object : TypeReference<T>() {})
    }

    fun deserialize(obj: String, type: JavaType): Any {
        return jacksonObjectMapper.readValue(obj, type)
    }

}
