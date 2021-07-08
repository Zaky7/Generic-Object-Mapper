package com.reactive.sse.services

import com.reactive.sse.model.WeatherInfoEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@Service
class WeatherInfoService(
    private val redisService: RedisService
) {

    private val logger: Logger = LoggerFactory.getLogger(WeatherInfoService::class.java)


    fun getWeatherDataFromRedis(key: String): Mono<List<WeatherInfoEvent>> {
        val data =  redisService.get<List<WeatherInfoEvent>>(key)
        return data
    }


    fun getWeatherDataListFromRedis(keys: List<String>): Flux<WeatherInfoEvent> {
        return redisService.getMany(keys)
    }
}