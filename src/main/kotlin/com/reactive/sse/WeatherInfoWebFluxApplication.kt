package com.reactive.sse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class WeatherInfoWebFluxApplication

fun main(args: Array<String>) {
    runApplication<WeatherInfoWebFluxApplication>(*args)
}

