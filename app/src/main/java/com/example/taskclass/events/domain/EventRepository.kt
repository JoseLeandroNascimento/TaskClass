package com.example.taskclass.events.domain

import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.dto.EventWithType
import kotlinx.coroutines.flow.Flow

interface EventRepository {

    fun getAllEvents(): Flow<List<EventWithType>>

    fun getEventsByDate(date: String): Flow<List<EventEntity>>

    suspend fun saveEvent(event: EventEntity)

    suspend fun updateEvent(event: EventEntity)

    suspend fun deleteEvent(event: EventEntity)

    suspend fun clearAll()
}