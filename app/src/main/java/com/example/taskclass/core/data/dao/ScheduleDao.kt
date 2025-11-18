package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskclass.core.data.model.dto.ScheduleAndDisciplineDTO
import com.example.taskclass.core.data.model.entity.ScheduleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert
    suspend fun save(data: ScheduleEntity)

    @Query(
        """
        SELECT *  
            FROM schedule_table 
            WHERE schedule_id = :id
        """
    )
    fun findById(id: Int): Flow<ScheduleEntity>

    @Query(
        """
        SELECT *
        FROM schedule_table AS s
    """
    )
    fun findAll(): Flow<List<ScheduleAndDisciplineDTO>>

    @Query(
        """
            SELECT *
            FROM schedule_table AS s
            WHERE s.start_time <= :timeEnd
              AND s.end_time >= :timeStart AND s.day_week = :weekDay
        """
    )
    fun findAllByRangeTime(
        timeStart: Int,
        timeEnd: Int,
        weekDay: Int
    ): Flow<List<ScheduleAndDisciplineDTO>>

    @Delete
    suspend fun delete(data: ScheduleEntity)

    @Update
    suspend fun update(data: ScheduleEntity)
}