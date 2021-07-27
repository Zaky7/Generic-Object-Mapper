package com.reactive.sse.weatherinfo.controllers

import com.reactive.sse.weatherinfo.model.WeatherInfoEvent
import com.reactive.sse.weatherinfo.services.WeatherInfoService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/weather")
class WeatherInfoController(private val weatherInfoService: WeatherInfoService) {


    @GetMapping(value = ["/single"])
    @ResponseStatus(HttpStatus.CREATED)
    fun getWeatherSingleInfoData(@RequestParam key: String): Mono<WeatherInfoEvent> {
        return weatherInfoService.getWeatherSingleDataFromRedis(key)
    }


    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun getWeatherInfoData(@RequestParam key: String): Mono<List<WeatherInfoEvent>> {
        return weatherInfoService.getWeatherDataFromRedis(key)
    }


    @GetMapping(value = ["/list"])
    @ResponseStatus(HttpStatus.CREATED)
    fun getWeatherInfoListData(@RequestParam keys: List<String>): Flux<WeatherInfoEvent> {
        return weatherInfoService.getWeatherDataListFromRedis(keys)
    }


    @PostMapping(value = ["/list"])
    @ResponseStatus(HttpStatus.CREATED)
    fun getWeatherInfoListDataPost(@RequestParam keys: List<String>): Flux<WeatherInfoEvent> {
        return weatherInfoService.getWeatherDataListFromRedis(keys)
    }
}