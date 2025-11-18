package com.example.taskclass.ui.discipline.domain

import com.example.taskclass.core.data.model.entity.DisciplineEntity
import jakarta.inject.Inject

class CreateDisciplineUseCase @Inject constructor(
    private val repository: DisciplineRepository
) {

    suspend fun save(data: DisciplineEntity) {
        repository.save(data)
    }
}