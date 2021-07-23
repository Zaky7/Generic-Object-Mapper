package com.reactive.sse.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.time.Duration
import java.time.Instant
import java.util.*


@Service
class JwtSigner {

    val keyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)


    /**
     * Create jwt token
     *
     * @param userId
     * @return
     */
    fun createJwt(userId: String): String {
        return Jwts.builder().signWith(keyPair.private, SignatureAlgorithm.RS256)
            .setSubject(userId)
            .setIssuer("identity")
            .setExpiration(Date.from(Instant.now().plus(Duration.ofMinutes(15))))
            .setIssuedAt(Date.from(Instant.now()))
            .compact()
    }


    /**
     * Validate jwt token
     *
     * @param jwt
     * @return
     */
    fun validateJwt(jwt: String): Jws<Claims> {
        return Jwts.parserBuilder()
                .setSigningKey(keyPair.public)
                .build().parseClaimsJws(jwt)
    }
}