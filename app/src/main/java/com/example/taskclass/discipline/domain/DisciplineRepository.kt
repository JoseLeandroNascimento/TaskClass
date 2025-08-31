package com.example.taskclass.discipline.domain

import com.example.taskclass.core.data.Discipline
import kotlinx.coroutines.flow.Flow

interface DisciplineRepository {

    suspend fun save(data: Discipline)

    suspend fun update(data: Discipline)

    suspend fun findAll():  Flow<List<Discipline>>
}