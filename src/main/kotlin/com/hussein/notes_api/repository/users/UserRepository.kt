package com.hussein.notes_api.repository.users

import com.hussein.notes_api.database.user.UserEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<UserEntity, ObjectId> {
    fun findUserByEmail(email:String):UserEntity?
}