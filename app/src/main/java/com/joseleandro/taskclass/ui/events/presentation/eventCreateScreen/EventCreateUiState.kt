package com.joseleandro.taskclass.ui.events.presentation.eventCreateScreen

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity

data class EventCreateUiState(
    val formState: FormState = FormState(),
    val typeEvents: Resource<List<TypeEventEntity>>? = null,
    val savedSuccessAndClose: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false
)
