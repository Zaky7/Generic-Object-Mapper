package com.reactive.sse.user

data class User(val email: String)

data class UserCredentials(val email: String, val password: String, val roles: List<Role>)

enum class Role {
    ROLE_USER, ROLE_ADMIN
}