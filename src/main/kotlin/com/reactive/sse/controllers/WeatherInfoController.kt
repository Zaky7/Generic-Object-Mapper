package com.reactive.sse.controllers

import com.reactive.sse.model.WeatherInfoEvent
import com.reactive.sse.services.WeatherInfoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
class WeatherInfoController(private val weatherInfoService: WeatherInfoService) {


    @GetMapping(value = ["/get/weather/single"])
    @ResponseStatus(HttpStatus.CREATED)
    fun getWeatherSingleInfoData(@RequestParam key: String): Mono<WeatherInfoEvent> {
        return weatherInfoService.getWeatherSingleDataFromRedis(key)
    }

    @GetMapping(value = ["/get/weather"])
    @ResponseStatus(HttpStatus.CREATED)
    fun getWeatherInfoData(@RequestParam key: String): Mono<List<WeatherInfoEvent>> {
        return weatherInfoService.getWeatherDataFromRedis(key)
    }


    @GetMapping(value = ["/get/weather/list"])
    @ResponseStatus(HttpStatus.CREATED)
    fun getWeatherInfoListData(@RequestParam keys: List<String>): Flux<WeatherInfoEvent> {
        return weatherInfoService.getWeatherDataListFromRedis(keys)
    }
}