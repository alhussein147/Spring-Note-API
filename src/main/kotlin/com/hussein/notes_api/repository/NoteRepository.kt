package com.hussein.notes_api.repository

import com.hussein.notes_api.database.models.NoteEntity
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository


interface NoteRepository : MongoRepository<NoteEntity, ObjectId> {
    fun findByOwnerId(ownerId: ObjectId): List<NoteEntity>

}