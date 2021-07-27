package com.reactive.sse.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class PreReactiveAuthenticatedConverter : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange)
            .flatMap { serverWebExchange: ServerWebExchange ->
                Mono.justOrEmpty(serverWebExchange.request.headers)
            }
            .filter { headers: HttpHeaders -> headers.contains("USER_ROLE") }
            .map { headers: HttpHeaders ->
                val userRoles: List<String> = headers.getOrDefault("USER_ROLE", listOf(""))
                val roles: List<String> =
                    userRoles[0].split(",").filter { role -> role.isNotBlank() }.map { role: String -> "ROLE_$role" }
                UsernamePasswordAuthenticationToken(
                    roles,
                    roles
                )
            }
    }

}