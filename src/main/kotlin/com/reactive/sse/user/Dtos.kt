package com.reactive.sse.user

import com.fasterxml.jackson.annotation.JsonIgnore
import com.reactive.sse.security.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors


data class UserCredentials(val email: String, val password: String)


class User(private val uname: String,
           private val pass: String,
           private val enabled: Boolean,
           private val roles: List<Role>) : UserDetails {

    companion object {
        private const val serialVersionUID = 1L
    }


    fun getRoles(): List<Role> = this.roles

    @JsonIgnore
    override fun getPassword(): String = this.pass


    override fun getUsername(): String = this.uname


    override fun isAccountNonExpired(): Boolean = false


    override fun isAccountNonLocked(): Boolean = false


    override fun isCredentialsNonExpired(): Boolean = false


    override fun isEnabled(): Boolean = this.enabled


    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.stream().map { authority: Role -> SimpleGrantedAuthority(authority.name) }
            .collect(Collectors.toList())
    }

}



