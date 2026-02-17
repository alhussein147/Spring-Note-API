package com.hussein.notes_api.repository.token

import com.hussein.notes_api.database.token.RefreshTokenEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface RefreshTokenRepository : MongoRepository<RefreshTokenEntity, ObjectId> {
    fun findByUserIdAndHashedToken(userId: ObjectId, hashedToken: String):RefreshTokenEntity?
    fun deleteByUserIdAndHashedToken(userId: ObjectId , hashedToken: String)
}