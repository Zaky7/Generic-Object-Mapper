package com.reactive.sse.model

interface WebsocketEventListenerInterface {
    fun onData(event: WeatherInfoEvent?)
    fun processComplete()
}