package com.example.taskclass.core.data.repository

import android.util.Log
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.EventDao
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.dto.EventWithType
import com.example.taskclass.events.domain.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val dao: EventDao
) : EventRepository {

    companion object {
        private const val LOG_TAG = "EVENT_REPOSITORY"
    }

    override fun findById(id: Int): Flow<Resource<EventWithType>> =
        dao.findByIdWithType(id)
            .map { event -> // ← aqui o compilador não consegue inferir o tipo
                if (event != null) Resource.Success(event)
                else Resource.Error("Evento não encontrado.")
            }


    override fun findAll(): Flow<Resource<List<EventWithType>>> =
        dao.findAllWithType()
            .map { events -> // ✅ tipo explícito
                Resource.Success(events)
            }


    override fun save(event: EventEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            dao.insert(event)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Erro ao salvar evento", e)
            emit(Resource.Error("Erro ao salvar evento."))
        }
    }

    override fun update(event: EventEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            dao.update(event)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Erro ao atualizar evento", e)
            emit(Resource.Error("Erro ao atualizar evento."))
        }
    }

    override fun delete(id: Int): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            dao.deleteById(id)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Erro ao deletar evento", e)
            emit(Resource.Error("Erro ao deletar evento."))
        }
    }
}
