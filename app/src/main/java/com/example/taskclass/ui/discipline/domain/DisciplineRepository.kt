package com.example.taskclass.ui.discipline.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.core.data.model.Order
import kotlinx.coroutines.flow.Flow

interface DisciplineRepository {

    suspend fun save(data: Discipline): Flow<Resource<Discipline>>

    suspend fun update(data: Discipline): Flow<Resource<Discipline>>

    suspend fun findById(id: Int): Flow<Resource<Discipline>>

    suspend fun findAll(
        title: String? = null,
        createdAt: Long? = null,
        updatedAt: Long? = null,
        order: Order<Discipline> = Order(selector = Discipline::title, ascending = false)
    ): Flow<Resource<List<Discipline>>>

    suspend fun delete(id: Int)
}