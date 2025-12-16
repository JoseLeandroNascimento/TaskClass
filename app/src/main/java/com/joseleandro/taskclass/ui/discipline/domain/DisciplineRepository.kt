package com.joseleandro.taskclass.ui.discipline.domain

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity
import com.joseleandro.taskclass.core.data.model.Order
import kotlinx.coroutines.flow.Flow

interface DisciplineRepository {

    suspend fun save(data: DisciplineEntity): Flow<Resource<DisciplineEntity>>


    suspend fun findById(id: Int): Flow<Resource<DisciplineEntity>>

    suspend fun findAll(
        title: String? = null,
        createdAt: Long? = null,
        updatedAt: Long? = null,
        order: Order<DisciplineEntity> = Order(selector = DisciplineEntity::title, ascending = false)
    ): Flow<Resource<List<DisciplineEntity>>>

    suspend fun delete(id: Int)
}