package com.hussein.notes_api.database.user

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("users")
data class UserEntity(
    @Id val id: ObjectId = ObjectId.get(),
    val email: String,
    val hashedPassword: String
)