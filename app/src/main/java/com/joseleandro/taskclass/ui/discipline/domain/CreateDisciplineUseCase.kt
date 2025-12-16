package com.joseleandro.taskclass.ui.discipline.domain

import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity
import jakarta.inject.Inject

class CreateDisciplineUseCase @Inject constructor(
    private val repository: DisciplineRepository
) {

    suspend fun save(data: DisciplineEntity) {
        repository.save(data)
    }
}