package com.hussein.notes_api.mappers

import com.hussein.notes_api.controllers.notes.NoteRequest
import com.hussein.notes_api.controllers.notes.NoteResponse
import com.hussein.notes_api.database.models.NoteEntity
import org.bson.types.ObjectId
import java.time.Instant

fun NoteRequest.toNote(ownerId:String):NoteEntity{
    return    NoteEntity(
        id = this.id?.let { ObjectId(it) } ?: ObjectId.get(),
        title = this.title,
        content = this.content,
        color = this.color,
        createAt = Instant.now(),
        ownerId = ObjectId(ownerId)
    )
}
fun NoteEntity.toResponse(): NoteResponse {
    return NoteResponse(
        id = this.id.toHexString(),
        title = this.title,
        color = this.color,
        content = this.content,
        createdAt = this.createAt
    )
}