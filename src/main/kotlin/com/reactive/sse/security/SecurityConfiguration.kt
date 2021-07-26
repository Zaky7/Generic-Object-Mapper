package com.reactive.sse.security

import com.reactive.sse.security.jwt.JwtReactiveAuthenticationManager
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono


@Configuration
@EnableWebFluxSecurity
class SecurityConfiguration {
    private val logger = KotlinLogging.logger {}


    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        jwtReactiveAuthenticationManager: JwtReactiveAuthenticationManager,
        jwtAuthenticationConverter: ServerAuthenticationConverter
    ): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(jwtReactiveAuthenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)

        return http
            .exceptionHandling()
            .authenticationEntryPoint { swe: ServerWebExchange, ex: AuthenticationException? ->
                logger.error(ex) { "Authentication exception" }
                Mono.fromRunnable { swe.response.statusCode = HttpStatus.UNAUTHORIZED }
            }.accessDeniedHandler { swe: ServerWebExchange, ex: AccessDeniedException? ->
                logger.error(ex) { "Authorization exception" }
                Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN }
            }.and()
            .authorizeExchange()
            .pathMatchers("/user/signup").permitAll()
            .pathMatchers("/user/login").permitAll()
            .pathMatchers("/user").authenticated()
            .pathMatchers("/weather/single").hasRole("USER")
            .pathMatchers("/weather/list").hasRole("ADMIN")
            .and()
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic().disable()
            .csrf().disable()
            .formLogin().disable()
            .logout().disable()
            .build()
    }
}