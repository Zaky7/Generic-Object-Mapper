package com.reactive.sse.services

import com.reactive.sse.common.ObjectMapper
import com.reactive.sse.model.WeatherInfoEvent
import com.reactive.sse.model.WebsocketEventListenerInterface
import mu.KotlinLogging
import org.springframework.data.redis.connection.ReactiveSubscription
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct


@Service
class WeatherEventRedisConsumer(
    private val reactiveMsgListenerContainer: ReactiveRedisMessageListenerContainer,
    private val weatherDataRedisTopic: ChannelTopic,
    private val mapper: ObjectMapper
) {

    private val logger = KotlinLogging.logger {}


    lateinit var listener: WebsocketEventListenerInterface


    fun register(listener: WebsocketEventListenerInterface?) {
        this.listener = listener!!
    }


    fun onEvent(event: WeatherInfoEvent?) {
        if (this::listener.isInitialized) {
            listener.onData(event)
        }
    }


    fun onComplete() {
        listener.processComplete()
    }


    @PostConstruct
    private fun consumerInit() {
        this.reactiveMsgListenerContainer.receive(weatherDataRedisTopic)
            .map(ReactiveSubscription.Message<String, String>::getMessage)
            .map { msg -> mapper.deserialize<WeatherInfoEvent>(msg) }
            .doOnError { e -> logger.error(e) { "Error deserializing WeatherInfo event in redis consumer" } }
            .subscribe { tick: WeatherInfoEvent -> onEvent(tick) }
    }
}