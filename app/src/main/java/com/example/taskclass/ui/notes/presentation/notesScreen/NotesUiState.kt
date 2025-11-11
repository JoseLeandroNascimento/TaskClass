package com.example.taskclass.ui.notes.presentation.notesScreen

import com.example.taskclass.core.data.model.NoteEntity

data class NotesUiState(
    val isLoading: Boolean = false,
    val notes: List<NoteEntity> = emptyList(),
    val messageError: String? = null
)