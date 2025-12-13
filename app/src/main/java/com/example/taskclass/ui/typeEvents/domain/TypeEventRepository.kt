package com.example.taskclass.ui.typeEvents.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Order
import com.example.taskclass.core.data.model.entity.TypeEventEntity
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