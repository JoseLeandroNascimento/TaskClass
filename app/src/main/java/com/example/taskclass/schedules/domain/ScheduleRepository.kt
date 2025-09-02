package com.example.taskclass.schedules.domain

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Schedule
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun save(data: Schedule): Flow<Resource<Schedule>>
}