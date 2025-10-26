package com.example.taskclass.events.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.dto.EventWithType
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun findById(id: Int): Flow<Resource<EventWithType>>
    fun findAll(): Flow<Resource<List<EventWithType>>>
    fun save(event: EventEntity): Flow<Resource<Unit>>
    fun update(event: EventEntity): Flow<Resource<Unit>>
    fun delete(id: Int): Flow<Resource<Unit>>
}
