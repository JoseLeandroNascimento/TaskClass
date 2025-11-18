package com.example.taskclass.ui.events.domain

import com.example.taskclass.core.data.model.DateInt
import com.example.taskclass.core.data.model.enums.EEventStatus
import com.example.taskclass.core.data.model.Time

data class EventFilter(
    val id: Int? = null,
    val query: String? = null,
    val date: DateInt? = null,
    val time: Time? = null,
    val typeEventId: Int? = null,
    val typeEventName: String? = null,
    val status: EEventStatus? = null
)
