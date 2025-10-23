package com.example.taskclass.core.data.model.dto

import androidx.compose.ui.graphics.Color
import com.example.taskclass.core.data.model.DateInt
import com.example.taskclass.core.data.model.Time

data class EventWithType(
    val id: Int,
    val title: String,
    val description: String,
    val date: DateInt,
    val time: Time,
    val color: Color,
    val typeEventName: String,
    val typeEventId: Int
)