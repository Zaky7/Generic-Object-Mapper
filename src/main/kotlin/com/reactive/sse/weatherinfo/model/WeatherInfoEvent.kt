package com.reactive.sse.weatherinfo.model

data class WeatherInfoEvent(val stationId: Long, val temperature: Int)