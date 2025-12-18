package com.joseleandro.taskclass.ui.notes.presentation.notesScreen

import com.joseleandro.taskclass.core.data.model.entity.NoteEntity

sealed interface NoteAction {
    data class OnToggleNote(val note: NoteEntity): NoteAction
}