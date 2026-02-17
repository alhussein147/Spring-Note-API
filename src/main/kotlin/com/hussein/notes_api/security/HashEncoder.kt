package com.hussein.notes_api.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class HashEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    fun encode(raw: String): String = bcrypt.encode(raw)
    fun matches(raw: String, encoded: String): Boolean = bcrypt.matches(raw, encoded)


     fun hashRefreshToken(refreshToken: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(refreshToken.encodeToByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)

    }
}