package com.reactive.sse.model

data class WeatherInfoEvent(val stationId: Long, val temperature: Int)