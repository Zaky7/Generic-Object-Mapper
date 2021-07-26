package com.reactive.sse.user

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.security.Principal


@RestController
@RequestMapping("/user")
class UserController {

    // Mock for Db call
    private val users: MutableMap<String, UserCredentials> = mutableMapOf(
        "test@gmail.com" to UserCredentials("test@gmail.com", "secret", listOf(Role.ROLE_ADMIN))
    )


    @PostMapping("/signup", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signUp(@RequestBody user: UserCredentials): Mono<ResponseEntity<Void>> {
        users[user.email] = user
        return Mono.just(ResponseEntity.noContent().build())
    }


    @GetMapping
    fun getMyself(principal: Principal): Mono<ResponseEntity<User>> {
        return Mono.justOrEmpty(users[principal.name])
            .map { ResponseEntity.ok(User(it.email)) }
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
    }

}