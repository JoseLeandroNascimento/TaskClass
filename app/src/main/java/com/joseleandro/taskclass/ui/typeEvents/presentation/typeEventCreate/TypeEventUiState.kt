package com.joseleandro.taskclass.ui.typeEvents.presentation.typeEventCreate

data class TypeEventUiState(
    val formState: FormState = FormState(),
    val isLoading: Boolean = false,
    val isLoadingButton: Boolean = false,
    val isBackNavigation: Boolean = false
)
