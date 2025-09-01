package com.example.taskclass.discipline.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.Discipline
import kotlinx.coroutines.flow.Flow

interface DisciplineRepository {

    suspend fun save(data: Discipline): Flow<Resource<Discipline>>

    suspend fun update(data: Discipline)

    suspend fun findAll(): Flow<Resource<List<Discipline>>>

    suspend fun delete(id: Int)
}