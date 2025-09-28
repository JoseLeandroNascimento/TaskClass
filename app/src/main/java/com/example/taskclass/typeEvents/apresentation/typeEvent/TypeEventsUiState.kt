package com.example.taskclass.typeEvents.apresentation.typeEvent

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent

data class TypeEventsUiState(
    val id: Int = 0,
    val typeEvents: Resource<List<TypeEvent>>? = null
)