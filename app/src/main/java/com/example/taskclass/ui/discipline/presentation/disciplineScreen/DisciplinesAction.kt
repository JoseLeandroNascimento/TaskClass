package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import com.example.taskclass.core.data.model.Order
import com.example.taskclass.core.data.model.entity.DisciplineEntity

sealed interface DisciplinesAction {
    data class OnDeleteDiscipline(val id: Int) : DisciplinesAction
    data class UpdateFilterSort(val order: Order<DisciplineEntity>) : DisciplinesAction
    data class UpdateQuery(val query: String) : DisciplinesAction
}