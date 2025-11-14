package com.example.taskclass.core.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discipline_table")
data class Discipline(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "discipline_id")
    val id: Int = 0,
    val title: String,
    val color: Color,
    @ColumnInfo(name = "teacher_name") val teacherName: String,
    val createdAt: Long,
    val updatedAt: Long
)