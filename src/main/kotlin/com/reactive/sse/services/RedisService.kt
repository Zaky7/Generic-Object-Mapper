package com.reactive.sse.services

import com.fasterxml.jackson.core.type.TypeReference
import com.reactive.sse.common.ObjectMapper
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

    inline fun <reified T> jacksonTypeRef(): TypeReference<T> = object : TypeReference<T>() {}


    inline fun <reified T : Any> get(key: String): Mono<T> {
        return redisTemplate.opsForValue().get(key).map { data ->
            val value = objectMapper.jacksonObjectMapper.readValue(data, T::class.java)
            value
        }
    }

    inline fun <reified T : Any> getList(key: String): Mono<List<T>> {
        return redisTemplate.opsForValue().get(key).map { data ->
            val type = objectMapper.jacksonObjectMapper.constructType(T::class.java)
            val listType = objectMapper.jacksonObjectMapper.typeFactory.constructCollectionType(List::class.java, type)
            val listData = objectMapper.deserialize(data, listType)

            @Suppress("UNCHECKED_CAST")
            listData as List<T>
        }
    }


    inline fun <reified T : Any> getMany(keys: List<String>): Flux<T> {
        return redisTemplate.opsForValue().multiGet(keys)
            .flatMapMany { data: List<String> -> Flux.fromIterable(data) }
            .filter { data: String -> data.isNotEmpty() }
            .flatMapIterable { data: String ->
                val type = objectMapper.jacksonObjectMapper.constructType(T::class.java)
                val iterableType =
                    objectMapper.jacksonObjectMapper.typeFactory.constructCollectionType(Collection::class.java, type)
                val iterableData = objectMapper.deserialize(data, iterableType)

                @Suppress("UNCHECKED_CAST")
                iterableData as Iterable<T>
                iterableData
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