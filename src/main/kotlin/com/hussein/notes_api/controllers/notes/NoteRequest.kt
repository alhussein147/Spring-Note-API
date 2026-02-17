package com.hussein.notes_api.controllers.notes

data class NoteRequest(
    val id: String?,
    val title: String,
    val content: String,
    val color: Long,
)

