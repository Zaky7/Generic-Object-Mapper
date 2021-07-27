package com.reactive.sse.user

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class User(val email: String)

data class UserCredentials(val email: String, val password: String, val roles: List<Role>)

enum class Role {
    ROLE_USER, ROLE_ADMIN
}


@JsonIgnoreProperties(ignoreUnknown = true)
data class RolesBody (
    val roles: List<String>
)