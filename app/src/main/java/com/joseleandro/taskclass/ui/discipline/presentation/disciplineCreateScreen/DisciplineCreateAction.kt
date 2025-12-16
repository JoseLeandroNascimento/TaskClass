package com.joseleandro.taskclass.ui.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color

sealed interface DisciplineCreateAction {

    data class UpdateTitle(val title: String) : DisciplineCreateAction
    data class UpdateTeacherName(val teacherName: String) : DisciplineCreateAction
    data class UpdateColorSelect(val color: Color) : DisciplineCreateAction
    data object OnSave : DisciplineCreateAction

}