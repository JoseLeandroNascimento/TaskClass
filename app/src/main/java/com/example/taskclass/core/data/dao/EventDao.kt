package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskclass.core.data.model.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events_table ORDER BY date, time")
    fun findAll(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events_table WHERE date = :date ORDER BY time")
    fun findByDate(date: String): Flow<List<EventEntity>>

    @Insert
    suspend fun insert(event: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM events_table")
    suspend fun clearAll()
}