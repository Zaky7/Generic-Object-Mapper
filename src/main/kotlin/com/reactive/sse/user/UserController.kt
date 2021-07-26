package com.reactive.sse.user

import com.reactive.sse.security.UserCredentials
import com.reactive.sse.security.AuthResponse
import com.reactive.sse.security.Message
import com.reactive.sse.security.jwt.JWTUtil
import com.reactive.sse.security.jwt.PBKDF2Encoder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
    private val passwordEncoder: PBKDF2Encoder,
    private val jwtUtil: JWTUtil
) {


    @PostMapping("/signup", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun signUp(@RequestBody user: UserCredentials): Mono<ResponseEntity<Void>> {
        userService.setUser(user.username, passwordEncoder.encode(user.password))
        return Mono.just(ResponseEntity.noContent().build())
    }

    @PostMapping("/login")
    fun login(@RequestBody userCredentials: UserCredentials): Mono<ResponseEntity<AuthResponse>> {
        return userService.findByUsername(userCredentials.username)
            .filter { saveUserDetails: User ->
                println(saveUserDetails.password)
                println(passwordEncoder.encode(userCredentials.password))
                val result = passwordEncoder.encode(userCredentials.password) == saveUserDetails.password
                result
            }
            .map { userDetails ->
                val body = AuthResponse(jwtUtil.generateToken(userDetails))
                ResponseEntity.ok(body)
            }
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()))
    }


    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    fun user(): Mono<ResponseEntity<Message>>? {
        return Mono.just(ResponseEntity.ok(Message("Content for user")))
    }


    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun admin(): Mono<ResponseEntity<Message>>? {
        return Mono.just(ResponseEntity.ok(Message("Content for Admin")))
    }


    @GetMapping("/user-or-admin")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun userOrAdmin(): Mono<ResponseEntity<Message>>? {
        return Mono.just(ResponseEntity.ok(Message("Content for Admin")))
    }

}