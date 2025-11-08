package com.example.taskclass.core.data.repository

import android.util.Log
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.EventDao
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.dto.EventWithType
import com.example.taskclass.core.data.model.formatted
import com.example.taskclass.events.domain.EventFilter
import com.example.taskclass.events.domain.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
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
            .map { events ->
                Resource.Success(events)
            }

    override fun filter(filter: EventFilter): Flow<Resource<List<EventWithType>>> {

        return flow {

            try {
                emit(Resource.Loading())
                dao.search(
                    id = filter.id,
                    title = filter.title,
                    status = filter.status,
                    typeEventId = filter.typeEventId,
                    time = filter.time?.formatted(),
                    date = filter.date?.formatted()
                ).collect {
                    emit(Resource.Success(it))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message!!))
            }

        }


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

    override fun updateStatus(
        id: Int,
        status: EEventStatus
    ): Flow<Resource<Unit>> {

        return flow {
            try {
                emit(Resource.Loading())
                dao.updateStatus(id, status)
                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message!!))
            }
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
