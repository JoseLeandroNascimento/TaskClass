package com.example.taskclass.core.data.repository

import android.util.Log
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.EventDao
import com.example.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.example.taskclass.core.data.model.entity.EventEntity
import com.example.taskclass.core.data.model.enums.EEventStatus
import com.example.taskclass.ui.events.domain.EventFilter
import com.example.taskclass.ui.events.domain.EventRepository
import com.example.taskclass.ui.events.domain.statusCurrent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val dao: EventDao
) : EventRepository {

    companion object {
        private const val LOG_TAG = "EVENT_REPOSITORY"
    }

    override fun findById(id: Int): Flow<Resource<EventEndTypeEventDto>> =
        dao.findByIdWithType(id)
            .map { event ->
                if (event != null) Resource.Success(event)
                else Resource.Error("Evento não encontrado.")
            }


    override fun findAll(): Flow<Resource<List<EventEndTypeEventDto>>> =
        dao.findAllWithType()
            .map { events ->
                Resource.Success(events)
            }

    override fun filter(filter: EventFilter): Flow<Resource<Map<EEventStatus, List<EventEndTypeEventDto>>>> {

        return flow {

            try {
                emit(Resource.Loading())
                dao.search(
                    id = filter.id,
                    query = filter.query,
                    completed = filter.isCompleted,
                    typeEventId = filter.typeEventId,
                ).map { events ->
                    val grouped = events.groupBy { item ->
                       item.event.statusCurrent()
                    }.toSortedMap(compareBy { it.ordinal })

                    if (filter.status == null) {
                        grouped
                    } else {
                        val onlyStatusList = grouped[filter.status] ?: emptyList()
                        mapOf(filter.status to onlyStatusList)
                    }
                }.collect {
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

    override fun updateCompleted(
        id: Int,
        isCompleted: Boolean
    ): Flow<Resource<Unit>> {

        return flow {
            emit(Resource.Loading())
            dao.updateCompleted(id, isCompleted)
            emit(Resource.Success(Unit))

        }.catch { e ->
            emit(Resource.Error(e.message!!))
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
