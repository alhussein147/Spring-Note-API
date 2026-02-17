package com.hussein.notes_api.database.token

import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document("users_tokens")
data class RefreshTokenEntity(
    val userId: ObjectId,
    val hashedToken: String,
    @Indexed(expireAfter = "0")
    val expiresAt: Instant,
    val createdAt: Instant = Instant.now()
)