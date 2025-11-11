package com.example.taskclass.ui.events.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.dto.EventWithType
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun findById(id: Int): Flow<Resource<EventWithType>>
    fun findAll(): Flow<Resource<List<EventWithType>>>
    fun filter(filter: EventFilter): Flow<Resource<List<EventWithType>>>
    fun save(event: EventEntity): Flow<Resource<Unit>>
    fun updateStatus(id: Int, status: EEventStatus): Flow<Resource<Unit>>
    fun update(event: EventEntity): Flow<Resource<Unit>>
    fun delete(id: Int): Flow<Resource<Unit>>
}
