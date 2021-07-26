package com.reactive.sse.security.jwt

import com.reactive.sse.common.ObjectMapper
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.util.MultiValueMap
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Component
class PreReactiveAuthenticatedConverter(private val objectMapper: ObjectMapper) : ServerAuthenticationConverter {

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        return Mono.justOrEmpty(exchange)
            .flatMap { serverWebExchange: ServerWebExchange ->
                Mono.justOrEmpty(serverWebExchange.request.queryParams)
            }
            .filter { authenticationMap: MultiValueMap<String, String> -> authenticationMap.containsKey("roles") }
            .map { authenticationMap: MultiValueMap<String, String> ->
                val rolesStr: List<String> = authenticationMap["roles"]!!
                val roles = rolesStr[0].split(",").map { role: String -> "ROLE_$role" }
                UsernamePasswordAuthenticationToken(
                    roles,
                    roles
                )
            }
    }

}