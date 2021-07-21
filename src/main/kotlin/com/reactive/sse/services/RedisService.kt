package com.reactive.sse.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.reactive.sse.common.ObjectMapper
import com.reactive.sse.model.WeatherInfoEvent
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
final class RedisService(
    val redisTemplate: ReactiveRedisTemplate<String, String>,
    val objectMapper: ObjectMapper
) {
    inline fun <reified T : Any> put(key: String, value: T): Mono<Boolean> {
        return redisTemplate.opsForValue().set(key, objectMapper.serialize(value))
    }


    inline fun <reified T : Any> get(key: String, type: TypeReference<T> = jacksonTypeRef()): Mono<T> {
        return redisTemplate.opsForValue().get(key).map { data ->
            val value = objectMapper.jacksonObjectMapper.readValue(data, type)
            value
        }
    }


    inline fun <reified T : Any> getMany(keys: List<String>, type: TypeReference<Iterable<T>>): Flux<T> {
        return redisTemplate.opsForValue().multiGet(keys)
            .flatMapMany { data: List<String> -> Flux.fromIterable(data) }
            .filter { data: String -> data.isNotEmpty() }
            .flatMapIterable { data: String ->
                val iterableData = objectMapper.jacksonObjectMapper.readValue(data, type)
                iterableData
            }
    }

    fun delete(key: String): Mono<Boolean> {
        return redisTemplate.opsForValue().delete(key)
    }
}

