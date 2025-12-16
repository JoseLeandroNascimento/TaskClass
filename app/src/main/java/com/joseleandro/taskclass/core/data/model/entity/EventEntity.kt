package com.joseleandro.taskclass.core.data.model.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(
    tableName = "events_table",
    foreignKeys = [
        ForeignKey(
            parentColumns = ["id"],
            childColumns = ["typeEventId"],
            entity = TypeEventEntity::class,
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val datetime: Instant,
    val typeEventId: Int,
    val completed: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long
)