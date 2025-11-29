package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import com.example.taskclass.core.data.model.entity.DisciplineEntity
import kotlin.reflect.KProperty1

sealed interface DisciplinesAction {
    data class OnDeleteDiscipline(val id: Int) : DisciplinesAction
    data class UpdateFilterSort(
        val orderBy: KProperty1<DisciplineEntity, Comparable<*>>,
        val sortDirection: Boolean
    ) : DisciplinesAction

    data class UpdateQuery(val query: String) : DisciplinesAction
}