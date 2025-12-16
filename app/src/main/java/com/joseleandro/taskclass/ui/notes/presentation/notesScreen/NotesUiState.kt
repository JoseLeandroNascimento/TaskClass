package com.joseleandro.taskclass.ui.notes.presentation.notesScreen

import com.joseleandro.taskclass.core.data.model.entity.NoteEntity

data class NotesUiState(
    val isLoading: Boolean = false,
    val notes: List<NoteEntity> = emptyList(),
    val messageError: String? = null
)