package com.example.taskclass.core.data.model.dto

import androidx.room.Embedded
import androidx.room.Relation
import com.example.taskclass.core.data.model.entity.EventEntity
import com.example.taskclass.core.data.model.entity.TypeEventEntity

data class EventEndTypeEventDto(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "typeEventId",
        entityColumn = "id"
    )
    val typeEvent: TypeEventEntity
)