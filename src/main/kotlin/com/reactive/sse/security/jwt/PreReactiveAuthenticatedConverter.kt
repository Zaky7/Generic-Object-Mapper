package com.reactive.sse.security.jwt

import com.reactive.sse.common.ObjectMapper
import com.reactive.sse.user.RolesBody
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.SequenceInputStream
import java.util.stream.Collectors


@Component
class PreReactiveAuthenticatedConverter(private val objectMapper: ObjectMapper) : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange)
            .flatMap { serverWebExchange: ServerWebExchange ->
                Mono.justOrEmpty(serverWebExchange.request.body)
            }.flatMap { data: Flux<DataBuffer> ->
                data.reduce(object : InputStream() {
                    override fun read() = -1
                }) { s: InputStream, d -> SequenceInputStream(s, d.asInputStream()) }
            }
            .map { inputStream: InputStream ->
                BufferedReader(InputStreamReader(inputStream)).lines().collect(Collectors.joining(""))
            }.map { requestBody: String ->
                println(requestBody)
                val body = objectMapper.deserialize<RolesBody>(requestBody)
                val roles: List<String> = body.roles.filter { role -> role.isNotBlank() }
                    .map { role: String -> "ROLE_$role" }
                UsernamePasswordAuthenticationToken(
                    roles,
                    roles
                )
            }
    }

}