package com.reactive.sse.security.jwt

import org.springframework.http.HttpCookie
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class JwtServerAuthenticationConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange)
            .flatMap { serverWebExchange: ServerWebExchange ->
                Mono.justOrEmpty(serverWebExchange.request.cookies["X-Auth"])
            }
            .filter { cookies: List<HttpCookie> -> cookies.isNotEmpty() }
            .map { cookies: List<HttpCookie> -> cookies[0].value }
            .map { authenticationStr: String ->
                UsernamePasswordAuthenticationToken(
                    authenticationStr,
                    authenticationStr
                )
            }
    }

}