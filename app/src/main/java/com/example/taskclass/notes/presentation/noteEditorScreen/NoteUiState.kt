package com.example.taskclass.notes.presentation.noteEditorScreen

data class NoteUiState(
    val html: String = "",
    val plain: String = "",
    val title: String = "",
    val id: Int? = null,
    val isLoading: Boolean = false,
    val isBackNavigation: Boolean = false,
    val messageError: String? = null
)
