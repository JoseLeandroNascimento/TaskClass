package com.example.taskclass.core.data.repository

import android.util.Log
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.ScheduleDao
import com.example.taskclass.core.data.model.Schedule
import com.example.taskclass.core.data.model.dto.ScheduleDTO
import com.example.taskclass.schedules.domain.ScheduleRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ScheduleRepositoryImpl @Inject constructor(
    private val dao: ScheduleDao
) : ScheduleRepository {

    override suspend fun save(data: Schedule): Flow<Resource<Schedule>> {

        return flow {
            try {
                emit(Resource.Loading())
                dao.save(data)
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error("Erro ao salvar o hórario"))
            }
        }

    }

    override suspend fun findAll(): Flow<Resource<List<ScheduleDTO>>> {

        return flow {
            try {
                emit(Resource.Loading())
                dao.findAll().collect { response ->
                    Log.d("teste",response.toString())
                    emit(Resource.Success(response))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Erro ao buscar horários: ${e.message}"))
            }
        }

    }
}