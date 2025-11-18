package com.example.taskclass.ui.events.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.example.taskclass.core.data.model.entity.EventEntity
import com.example.taskclass.core.data.model.enums.EEventStatus
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun findById(id: Int): Flow<Resource<EventEndTypeEventDto>>
    fun findAll(): Flow<Resource<List<EventEndTypeEventDto>>>
    fun filter(filter: EventFilter): Flow<Resource<List<EventEndTypeEventDto>>>
    fun save(event: EventEntity): Flow<Resource<Unit>>
    fun updateStatus(id: Int, status: EEventStatus): Flow<Resource<Unit>>
    fun update(event: EventEntity): Flow<Resource<Unit>>
    fun delete(id: Int): Flow<Resource<Unit>>
}
