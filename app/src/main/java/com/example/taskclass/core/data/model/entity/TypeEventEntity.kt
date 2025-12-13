package com.example.taskclass.core.data.model.entity

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_event_table")
data class TypeEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: Color,
    val icon: Int? = null,
    val createdAt: Long,
    val updatedAt: Long
)