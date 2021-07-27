package com.reactive.sse.weatherinfo.services

import com.reactive.sse.common.ObjectMapper
import com.reactive.sse.common.services.RedisService
import com.reactive.sse.weatherinfo.model.WeatherInfoEvent
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.stubbing.OngoingStubbing
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveValueOperations
import reactor.core.publisher.Mono
import reactor.test.StepVerifier


internal class WeatherInfoServiceTest {

    private val redisTemplate: ReactiveRedisTemplate<String, String> = mock()
    private val valueOperations: ReactiveValueOperations<String, String> = mock()
    private val objectMapper = ObjectMapper()
    private val redisService = RedisService(redisTemplate, objectMapper)
    private val weatherInfoService = WeatherInfoService(redisService)

    private fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)

    @Test
    fun `should not return weather info when key not present in the redis and the database`() {
        // given
        val emptyWeatherInfoKey = "EMPTY_TICKER"

        whenever(redisTemplate.opsForValue()).thenReturn(valueOperations)
        whenever(redisTemplate.opsForValue().get(emptyWeatherInfoKey)).thenReturn(Mono.empty())

        // when
        val weatherInfoListMono: Mono<List<WeatherInfoEvent>> =
            weatherInfoService.getWeatherDataFromRedis(emptyWeatherInfoKey)

        // then
        StepVerifier.create(weatherInfoListMono)
            .expectSubscription()
            .verifyComplete()
    }


    @Test
    fun `should return weather info event when present in the cache`() {
        val weatherInfoKey = "today"
        val expectedWeatherInfoEventList = listOf(WeatherInfoEvent(122, 89))

        whenever(redisTemplate.opsForValue()).thenReturn(valueOperations)
        whenever(
            redisTemplate.opsForValue().get(weatherInfoKey)
        ).thenReturn(Mono.just(objectMapper.serialize(expectedWeatherInfoEventList)))

        // when
        val weatherInfoListMono: Mono<List<WeatherInfoEvent>> =
            weatherInfoService.getWeatherDataFromRedis(weatherInfoKey)

        // then
        StepVerifier.create(weatherInfoListMono)
            .expectSubscription()
            .assertNext { weatherInfoEvent: List<WeatherInfoEvent> ->
                MatcherAssert.assertThat(weatherInfoEvent, Matchers.`is`(expectedWeatherInfoEventList))
            }
            .verifyComplete()

    }
}

