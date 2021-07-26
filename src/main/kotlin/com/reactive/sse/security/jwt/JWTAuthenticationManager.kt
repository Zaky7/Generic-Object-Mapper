package com.reactive.sse.security.jwt

import com.fasterxml.jackson.core.type.TypeReference
import io.jsonwebtoken.Claims
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import kotlin.reflect.KClass
import kotlin.reflect.typeOf


@Component
class JwtReactiveAuthenticationManager(private val jwtUtil: JWTUtil) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()
        val username = jwtUtil!!.getUsernameFromToken(authToken)
        return Mono.just(jwtUtil.validateToken(authToken))
            .filter { validObj -> validObj }
            .switchIfEmpty(Mono.empty())
            .map {
                val claims: Claims = jwtUtil.getAllClaimsFromToken(authToken)
                val temp: List<*> = claims.get("role", List::class.java)
                val rolesMap: List<String> = temp.map { it.toString() }
                UsernamePasswordAuthenticationToken(
                    username,
                    authentication.credentials as String,
                    rolesMap.stream().map { value -> SimpleGrantedAuthority(value) }.toList()
                );
            }
    }
}