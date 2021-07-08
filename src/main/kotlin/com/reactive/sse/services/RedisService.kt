package com.reactive.sse.services

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

    inline fun <reified T> get(key: String): Mono<T> {
        return redisTemplate.opsForValue().get(key).map { data ->
            val value = objectMapper.deserialize<T>(data)
            value
        }
    }


    inline fun <reified T : Any> getMany(keys: List<String>): Flux<T> {
        return redisTemplate.opsForValue().multiGet(keys)
            .flatMapMany { data: List<String> -> Flux.fromIterable(data) }
            .filter { data: String -> data.isNotEmpty() }
            .flatMapIterable { data: String ->
                val value = objectMapper.deserialize<Iterable<T>>(data)
                value
            }
    }

    fun delete(key: String): Mono<Boolean> {
        return redisTemplate.opsForValue().delete(key)
    }
}

//class GenericTest {
//    val objectMapper = ObjectMapper()
//
//    inline fun <reified T> deserialize(str: String): T {
//            val value = objectMapper.deserialize<T>(str)
//            return value
//    }
//}
//
//fun main() {
//    val test = GenericTest()
//    val listStr = "[{\"stationId\":145,\"temperature\":256}]"
//    val weatherInfoList = test.deserialize<List<WeatherInfoEvent>>(listStr)
//    println(weatherInfoList is List<WeatherInfoEvent>)
//
//
//    val str = "{\"stationId\":145,\"temperature\":256}"
//    val weatherInfo = test.deserialize<WeatherInfoEvent>(str)
//    println(weatherInfo is WeatherInfoEvent)
//}