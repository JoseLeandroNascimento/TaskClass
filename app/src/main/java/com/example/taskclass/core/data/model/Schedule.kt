package com.example.taskclass.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedule_table",
    foreignKeys = [
        ForeignKey(
            entity = Discipline::class,
            parentColumns = ["discipline_id"],
            childColumns = ["discipline_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "schedule_id")
    val id: Int = 0,

    @ColumnInfo(name = "day_week")
    val dayWeek: String,

    @ColumnInfo(name = "discipline_id", index = true)
    val disciplineId: Int,

    @ColumnInfo(name = "start_time")
    val startTime: Int,

    @ColumnInfo(name = "end_time")
    val endTime: Int,
)
