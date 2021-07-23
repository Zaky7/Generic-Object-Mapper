package com.reactive.sse.weatherinfo.services

import com.fasterxml.jackson.module.kotlin.jacksonTypeRef
import com.reactive.sse.common.services.RedisService
import com.reactive.sse.weatherinfo.model.WeatherInfoEvent
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class WeatherInfoService(
    private val redisService: RedisService
) {

    fun getWeatherDataFromRedis(key: String): Mono<List<WeatherInfoEvent>> {
        val data = redisService.get<List<WeatherInfoEvent>>(key)
        return data
    }


    fun getWeatherSingleDataFromRedis(key: String): Mono<WeatherInfoEvent> {
        val data = redisService.get<WeatherInfoEvent>(key)
        return data
    }


    fun getWeatherDataListFromRedis(keys: List<String>): Flux<WeatherInfoEvent> {
        return redisService.getMany(keys, jacksonTypeRef())
    }
}