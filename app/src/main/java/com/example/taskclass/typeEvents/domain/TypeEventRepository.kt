package com.example.taskclass.typeEvents.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent
import kotlinx.coroutines.flow.Flow

interface TypeEventRepository {

    fun save(data: TypeEvent): Flow<Resource<TypeEvent>>

    fun findAll(): Flow<Resource<List<TypeEvent>>>

    fun findById(id: Int): Flow<Resource<TypeEvent>>

    fun delete(id: Int): Flow<Resource<TypeEvent>>

    fun update(data: TypeEvent): Flow<Resource<TypeEvent>>

}