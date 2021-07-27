package com.reactive.sse.security.jwt

import com.reactive.sse.common.ObjectMapper
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Component
class PreReactiveAuthenticatedManager(private val objectMapper: ObjectMapper) : ReactiveAuthenticationManager {


    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return Mono.just(authentication)
            .map { authenticationObj: Authentication ->
                val roles: List<String> = (authenticationObj.credentials as List<*>).filterIsInstance<String>()
                val rolesAuthority = roles.stream().map { value -> SimpleGrantedAuthority(value) }.toList()
                UsernamePasswordAuthenticationToken(
                    roles,
                    roles,
                    rolesAuthority
                )
            }
    }
}