package com.joseleandro.taskclass.ui.events.domain

import com.joseleandro.taskclass.core.data.model.DateInt
import com.joseleandro.taskclass.core.data.model.enums.EEventStatus
import com.joseleandro.taskclass.core.data.model.Time

data class EventFilter(
    val id: Int? = null,
    val query: String? = null,
    val date: DateInt? = null,
    val time: Time? = null,
    val typeEventId: Int? = null,
    val typeEventName: String? = null,
    val isCompleted: Boolean? = null,
    val status: EEventStatus? = null
)
