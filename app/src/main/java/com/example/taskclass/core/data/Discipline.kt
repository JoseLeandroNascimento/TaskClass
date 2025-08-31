package com.example.taskclass.core.data

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "discipline_table")
data class Discipline(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val color: Color,
    @ColumnInfo(name = "teacher_name") val teacherName: String
)
