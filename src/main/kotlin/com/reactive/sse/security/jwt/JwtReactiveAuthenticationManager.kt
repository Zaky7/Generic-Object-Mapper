package com.reactive.sse.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono



@Component
class JwtReactiveAuthenticationManager(private val jwtSigner: JwtSigner) : ReactiveAuthenticationManager {


    /**
     * Authenticate based on the Jwt token
     *
     * @property authentication
     * @return Authentication Publisher
     */
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return Mono.just(authentication).map { jwtSigner.validateJwt(it.credentials as String) }
            .onErrorResume { Mono.empty() }
            .map { jws: Jws<Claims> ->
                val claims: Claims = jws.body
                val username = claims.subject
                val temp: List<*> = claims.get("role", List::class.java)
                val rolesMap: List<String> = temp.map { it.toString() }
                val isAuthenticated = UsernamePasswordAuthenticationToken(
                    username,
                    authentication.credentials as String,
                    rolesMap.stream().map { value -> SimpleGrantedAuthority(value) }.toList()
                )
                isAuthenticated
            }
    }
}