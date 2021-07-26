package com.reactive.sse.user

import com.reactive.sse.security.Role
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService {
    private var data: MutableMap<String, User> = mutableMapOf()

    init {
        data["user"] = User("user", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", true, listOf(Role.ROLE_USER))
        data["admin"] =
            User("admin", "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", true, listOf(Role.ROLE_ADMIN))

    }

    fun findByUsername(username: String): Mono<User> {
        return Mono.justOrEmpty(data[username])
    }

    fun setUser(name: String, pass: String): User {
        val user = User(name, pass, true, listOf(Role.ROLE_USER))
        data[name] = user
        return user
    }
}
