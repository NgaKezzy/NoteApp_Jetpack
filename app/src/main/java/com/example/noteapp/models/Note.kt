package com.example.noteapp.models

import java.time.LocalDateTime

data class Note (
    var title: String,
    var description: String,
    var createdAt: LocalDateTime
)