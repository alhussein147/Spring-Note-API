package com.hussein.notes_api.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Base64
import java.util.Date

@Service
class JwtService(@Value("\${jwt.secret}") private val jwtSecret: String) {

//    the fucking key must be > 256 bits
    private val secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret))
    val accessTokenValidityMs = 15L * 60L * 1000L
    val refreshTokenValidityMs = 30L * 24L * 60L * 60L * 1000L

    // claims are additional data that we can add to the token
    private fun generateToken(userId: String, type: String, validity: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + validity)
        return Jwts.builder()
            .subject(userId)
            .claim("type", type)
            .issuedAt(now)
            .signWith(secretKey, Jwts.SIG.HS256)
            .expiration(expiryDate).compact()
    }

    private fun parseAllClaims(token: String): Claims? {
        // claims are additional data provided in the payload
        val rawToken = if (token.startsWith("Bearer ") ){
            token.removePrefix("Bearer ")
        } else token
        return try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(rawToken).payload
        } catch (e: Exception) {
            null
        }
    }

    fun validateAccessToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "access"
    }

    fun validateRefreshToken(token: String): Boolean {
        val claims = parseAllClaims(token) ?: return false
        val tokenType = claims["type"] as? String ?: return false
        return tokenType == "refresh"
    }

    //    extracts the user id from jwt token
    fun getUserIdFromToken(token: String): String {

        val claims = parseAllClaims(token) ?: throw IllegalArgumentException("Invalid token.")
        return claims.subject
    }


    fun generateRefreshToken(userId: String) =
        generateToken(userId = userId, type = "refresh", validity = refreshTokenValidityMs)

    fun generateAccessToken(userId: String) =
        generateToken(userId = userId, type = "access", validity = accessTokenValidityMs)


}