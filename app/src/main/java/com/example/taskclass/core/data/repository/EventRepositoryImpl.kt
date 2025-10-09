package com.example.taskclass.core.data.repository

import com.example.taskclass.core.data.dao.EventDao
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.events.domain.EventRepository
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val eventDao: EventDao
) : EventRepository {

    override fun getAllEvents(): Flow<List<EventEntity>> = eventDao.findAll()

    override fun getEventsByDate(date: String): Flow<List<EventEntity>> = eventDao.findByDate(date)

    override suspend fun saveEvent(event: EventEntity) {
        eventDao.insert(event)
    }

    override suspend fun updateEvent(event: EventEntity) {
        eventDao.update(event)
    }

    override suspend fun deleteEvent(event: EventEntity) {
        eventDao.delete(event)
    }

    override suspend fun clearAll() {
        eventDao.clearAll()
    }
}