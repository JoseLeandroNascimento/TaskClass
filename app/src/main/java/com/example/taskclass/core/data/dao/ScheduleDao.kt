package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.taskclass.core.data.model.Schedule
import com.example.taskclass.core.data.model.dto.ScheduleDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduleDao {

    @Insert
    suspend fun save(data: Schedule)

    @Query(
        """
        SELECT s.schedule_id, s.day_week, s.start_time, s.end_time,
               d.discipline_id, d.title AS disciplineTitle, d.teacher_name, d.color AS color
        FROM schedule_table AS s
        INNER JOIN discipline_table AS d
        ON s.discipline_id = d.discipline_id
    """
    )
    fun findAll(): Flow<List<ScheduleDTO>>

    @Query(
        """
            SELECT s.schedule_id, s.day_week, s.start_time, s.end_time,
                   d.discipline_id, d.title AS disciplineTitle, d.teacher_name, d.color AS color
            FROM schedule_table AS s
            INNER JOIN discipline_table AS d
              ON s.discipline_id = d.discipline_id
            WHERE s.start_time < :timeEnd
              AND s.end_time > :timeStart
        """
    )
    fun findAllByRangeTime(timeStart: Int, timeEnd: Int): Flow<List<ScheduleDTO>>
}