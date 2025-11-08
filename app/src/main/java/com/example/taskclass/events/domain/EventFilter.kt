package com.example.taskclass.events.domain

import com.example.taskclass.core.data.model.DateInt
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.core.data.model.Time

data class EventFilter(
    val id: Int? = null,
    val title: String? = null,
    val description: String? = null,
    val date: DateInt? = null,
    val time: Time? = null,
    val typeEventId: Int? = null,
    val typeEventName: String? = null,
    val status: EEventStatus? = null
)
