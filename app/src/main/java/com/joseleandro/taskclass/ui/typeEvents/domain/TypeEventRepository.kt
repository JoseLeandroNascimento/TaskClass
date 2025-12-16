package com.joseleandro.taskclass.ui.typeEvents.domain

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import kotlinx.coroutines.flow.Flow

interface TypeEventRepository {

    fun save(data: TypeEventEntity): Flow<Resource<TypeEventEntity>>

    fun findAll(
        query: String?,
        order: Order<TypeEventEntity> = Order(TypeEventEntity::createdAt, ascending = true)
    ): Flow<Resource<List<TypeEventEntity>>>

    fun findById(id: Int): Flow<Resource<TypeEventEntity>>

    fun delete(id: Int): Flow<Resource<TypeEventEntity>>


}