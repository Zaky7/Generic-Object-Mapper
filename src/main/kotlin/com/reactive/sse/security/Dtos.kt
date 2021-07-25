package com.reactive.sse.security


enum class Role {
    ROLE_USER, ROLE_ADMIN
}

data class UserCredentials(val username: String, val password: String)

data class AuthResponse(private val token: String)

data class Message (private val content: String)

