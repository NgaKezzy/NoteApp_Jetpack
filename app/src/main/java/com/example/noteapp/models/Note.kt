package com.example.noteapp.models

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class Note (
    var title: String,
    var description: String,
    var createdAt: LocalDate
): Serializable