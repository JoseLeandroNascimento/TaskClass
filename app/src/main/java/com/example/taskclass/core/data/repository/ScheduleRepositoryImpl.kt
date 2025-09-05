package com.example.taskclass.core.data.repository

import android.util.Log
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.ScheduleDao
import com.example.taskclass.core.data.model.Schedule
import com.example.taskclass.core.data.model.dto.ScheduleDTO
import com.example.taskclass.schedules.domain.ScheduleRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl @Inject constructor(
    private val dao: ScheduleDao
) : ScheduleRepository {

    override suspend fun save(data: Schedule): Flow<Resource<Schedule>> {

        return flow {
            try {
                emit(Resource.Loading())

                val conflicts = dao.findAllByRangeTime(data.startTime, data.endTime, data.dayWeek).first()

                if (conflicts.isEmpty()) {
                    dao.save(data)
                    emit(Resource.Success(data))
                } else {
                    emit(Resource.Error("Ocorreu um conflito de horários."))
                }

            } catch (e: Exception) {
                emit(Resource.Error("Erro ao salvar o horário"))
            }
        }

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


}