package com.example.taskclass.ui.events.presentation.eventCreateScreen

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent

data class EventCreateUiState(
    val formState: FormState = FormState(),
    val typeEvents: Resource<List<TypeEvent>>? = null,
    val savedSuccessAndClose: Boolean = false
)
