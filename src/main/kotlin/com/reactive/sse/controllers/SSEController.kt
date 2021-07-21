package com.reactive.sse.controllers

import com.reactive.sse.model.WeatherInfoEvent
import com.reactive.sse.model.WebsocketEventListenerInterface
import com.reactive.sse.services.WeatherEventRedisConsumer
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink


@RestController
@RequestMapping(path = ["/weather/sse/"])
class SSEController(
    private val consumerMarketData: WeatherEventRedisConsumer
) {
    lateinit var realTimeWeatherDataBridge: Flux<WeatherInfoEvent?>
    private val logger = KotlinLogging.logger {}


    init {
        realTimeWeatherDataBridge = getMarketDataTicksBridge().publish().autoConnect()
    }


    private fun getMarketDataTicksBridge(): Flux<WeatherInfoEvent?> {
        return Flux.create { sink: FluxSink<WeatherInfoEvent?> ->  // (2)
            consumerMarketData.register(object : WebsocketEventListenerInterface {
                override fun processComplete() {
                    sink.complete()
                }

                override fun onData(event: WeatherInfoEvent?) {
                    sink.next(event!!)
                }
            })
        }
    }



    @GetMapping(value = ["/info"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun getTicksStream(): Flux<WeatherInfoEvent?>? {
        return realTimeWeatherDataBridge.doOnError { e ->
            //TODO(UN-189): update and return Internal server Error
            logger.error(e) { "Error getting realtime weather streams" }
        }
    }


}
