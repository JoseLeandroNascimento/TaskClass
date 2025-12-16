package com.joseleandro.taskclass.ui.schedules.domain

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.ScheduleEntity
import com.joseleandro.taskclass.core.data.model.dto.ScheduleAndDisciplineDTO
import kotlinx.coroutines.flow.Flow

interface ScheduleRepository {

    suspend fun save(data: ScheduleEntity): Flow<Resource<ScheduleEntity>>

    suspend fun findAll(): Flow<Resource<List<ScheduleAndDisciplineDTO>>>

    suspend fun findById(id: Int): Flow<Resource<ScheduleEntity>>

    suspend fun deleteById(id: Int): Flow<Resource<ScheduleEntity>>

    suspend fun update(data: ScheduleEntity): Flow<Resource<ScheduleEntity>>

}