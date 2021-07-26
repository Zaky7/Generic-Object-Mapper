package com.reactive.sse.user

import com.reactive.sse.security.jwt.JwtSigner
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.security.Principal


@RestController
@RequestMapping("/user")
class UserController(private val jwtSigner: JwtSigner) {

    // Mock for Db call
    private val users: MutableMap<String, UserCredentials> = mutableMapOf(
        "test@gmail.com" to UserCredentials("test@gmail.com", "secret", listOf(Role.ROLE_ADMIN))
    )


    @PostMapping("/signup", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signUp(@RequestBody user: UserCredentials): Mono<ResponseEntity<Void>> {
        users[user.email] = user
        return Mono.just(ResponseEntity.noContent().build())
    }


    @PostMapping("/login")
    fun logIn(@RequestBody user: UserCredentials): Mono<ResponseEntity<Void>> {
        return Mono.justOrEmpty(users[user.email])
            .filter { it.password == user.password }
            .map {
                val jwt = jwtSigner.createJwt(it.email, it.roles)
                val authCookie = ResponseCookie.fromClientResponse("X-Auth", jwt).maxAge(3600)
                    .httpOnly(true).path("/")
                    .secure(false) // true in production
                    .build()

                ResponseEntity.noContent().header("Set-Cookie", authCookie.toString()).build<Void>()
            }.switchIfEmpty(Mono.just(
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            ))
    }


    @GetMapping
    fun getMyself(principal: Principal): Mono<ResponseEntity<User>> {
        return Mono.justOrEmpty(users[principal.name])
            .map { ResponseEntity.ok(User(it.email)) }
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
    }

}