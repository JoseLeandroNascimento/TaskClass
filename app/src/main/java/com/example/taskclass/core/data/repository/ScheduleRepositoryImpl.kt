package com.example.taskclass.core.data.repository

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.ScheduleDao
import com.example.taskclass.core.data.model.Schedule
import com.example.taskclass.core.data.model.dto.ScheduleDTO
import com.example.taskclass.schedules.domain.ScheduleRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl @Inject constructor(
    private val dao: ScheduleDao
) : ScheduleRepository {

    override suspend fun save(data: Schedule): Flow<Resource<Schedule>> {

        return saveAndUpdate(data)

    }

    override suspend fun findAll(): Flow<Resource<List<ScheduleDTO>>> {

        return flow {
            try {
                emit(Resource.Loading())
                dao.findAll().collect { response ->
                    emit(Resource.Success(response))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Erro ao buscar horários: ${e.message}"))
            }
        }

    }

    override suspend fun findById(id: Int): Flow<Resource<Schedule>> {
        return flow {

            try {
                emit(Resource.Loading())
                dao.findById(id).collect {
                    emit(Resource.Success(it))
                }

            } catch (e: Exception) {
                emit(Resource.Error("Não foi encontrado o horário"))
            }
        }
    }

    override suspend fun deleteById(id: Int): Flow<Resource<Schedule>> {
        return flow {

            try {
                emit(Resource.Loading())
                val responseSchedule = dao.findById(id).firstOrNull()
                responseSchedule?.let {
                    dao.delete(responseSchedule)
                    emit(Resource.Success(responseSchedule))
                }

                if (responseSchedule == null)
                    emit(Resource.Error(message = "O Horário informado não existe ou já foi removido"))

            } catch (e: Exception) {
                emit(Resource.Error(message = "Ocorreu um erro ao deletar o horário"))
            }

        }
    }

    override suspend fun update(data: Schedule): Flow<Resource<Schedule>> {
        return saveAndUpdate(data)
    }

    private fun saveAndUpdate(data: Schedule): Flow<Resource<Schedule>> {
        return flow {

            try {
                val conflicts =
                    dao.findAllByRangeTime(data.startTime, data.endTime, data.dayWeek).first()

                if (conflicts.isEmpty()) {
                    if (data.id == 0) {
                        dao.save(data)
                    } else {
                        dao.update(data)
                    }
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Ocorreu um conflito de horários."))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Não foi possível salvar o horário"))
            }
        }
    }


}