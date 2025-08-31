package com.example.taskclass.discipline.domain

import com.example.taskclass.core.data.Discipline
import jakarta.inject.Inject

class CreateDisciplineUseCase @Inject constructor(
    private val repository: DisciplineRepository
) {

    suspend fun save(data: Discipline) {
        repository.save(data)
    }
}