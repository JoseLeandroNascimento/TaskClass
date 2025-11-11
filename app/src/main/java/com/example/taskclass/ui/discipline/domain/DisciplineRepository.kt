package com.example.taskclass.ui.discipline.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import kotlinx.coroutines.flow.Flow

interface DisciplineRepository {

    suspend fun save(data: Discipline): Flow<Resource<Discipline>>

    suspend fun update(data: Discipline): Flow<Resource<Discipline>>

    suspend fun findById(id: Int): Flow<Resource<Discipline>>

    suspend fun findAll(): Flow<Resource<List<Discipline>>>

    suspend fun delete(id: Int)
}