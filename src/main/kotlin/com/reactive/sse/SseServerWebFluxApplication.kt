package com.reactive.sse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class SseServerWebFluxApplication

fun main(args: Array<String>) {
    runApplication<SseServerWebFluxApplication>(*args)
}

