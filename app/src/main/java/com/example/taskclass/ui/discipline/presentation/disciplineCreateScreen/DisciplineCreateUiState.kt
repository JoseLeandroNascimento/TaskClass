package com.example.taskclass.ui.discipline.presentation.disciplineCreateScreen

data class DisciplineCreateUiState(
    val id: Int? = null,
    val form: FormState = FormState(),
    val showPickerColor: Boolean = false,
    val isBackNavigation: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingButton: Boolean = false,
)
