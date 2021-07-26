//package com.reactive.sse.user
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.web.server.LocalServerPort
//import org.springframework.http.HttpStatus
//import org.springframework.util.LinkedMultiValueMap
//import org.springframework.web.reactive.function.client.WebClient
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//internal class UserControllerTest {
//
//    @LocalServerPort
//    val serverPort: Int? = null
//
//
//    @Test
//    fun `can obtain own user details when logged in`() {
//        // arrange
//        val webClient = WebClient.builder()
//            .baseUrl("http://localhost:${serverPort}")
//            .build()
//
//        // act
//        webClient.post()
//            .uri("/user/signup")
//            .bodyValue(UserCredentials("new@example.com", "pw"))
//            .exchange()
//            .block()
//
//
//        val loginResponse = webClient.post()
//            .uri("/user/login")
//            .bodyValue(UserCredentials("new@example.com", "pw"))
//            .exchange()
//            .block() ?: throw RuntimeException("Should have gotten a response")
//
//        val responseCookies = loginResponse.cookies()
//            .map { it.key to it.value.map { cookie -> cookie.value } }
//            .toMap()
//
//        val response = webClient.get()
//            .uri("/user")
//            .cookies { it.addAll(LinkedMultiValueMap(responseCookies)) }
//            .exchange()
//            .block()
//
//        // assert
//        assertThat(response?.statusCode()).isEqualTo(HttpStatus.OK)
//        assertThat(response?.bodyToFlux(User::class.java)?.blockFirst()).isEqualTo(User("new@example.com"))
//    }
//}