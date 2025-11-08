package com.example.taskclass.core.data.model.dto

import androidx.compose.ui.graphics.Color
import com.example.taskclass.core.data.model.DateInt
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.Time

data class EventWithType(
    val id: Int,
    val title: String,
    val description: String,
    val date: DateInt,
    val time: Time,
    val typeEventId: Int?,
    val typeEventName: String?,
    val typeEventColor: Color?,
    val status: EEventStatus
) {
    val color: Color
        get() = typeEventColor ?: Color.Gray

    fun toEntity(): EventEntity {
        return EventEntity(
            id = id,
            title = title,
            description = description,
            date = date,
            time = time,
            typeEventId = typeEventId,
            typeEventName = typeEventName,
            status = status
        )
    }
}
