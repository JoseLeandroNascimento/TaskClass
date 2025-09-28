package com.example.taskclass.core.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "type_event_table")
data class TypeEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val color: Color,
    val icon: Int? = null
)
