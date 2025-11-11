package com.example.taskclass.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val html: String,
    val plain: String,
    val createdAt: Long,
    val updatedAt: Long
)
