package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.taskclass.core.data.model.Schedule

@Dao
interface ScheduleDao {

    @Insert
    suspend fun save(data: Schedule)
}