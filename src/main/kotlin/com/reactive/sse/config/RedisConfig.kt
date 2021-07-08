package com.reactive.sse.config


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer


@Configuration
class RedisConfig {

    @Bean
    fun redisContainer(factory: ReactiveRedisConnectionFactory): ReactiveRedisMessageListenerContainer {
        return  ReactiveRedisMessageListenerContainer(factory)
    }
}