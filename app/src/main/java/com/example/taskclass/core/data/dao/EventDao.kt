package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.dto.EventWithType
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query(
        """
    SELECT 
        e.id AS id, 
        e.title AS title, 
        e.description AS description, 
        e.date AS date, 
        e.time AS time, 
        t.color AS color, 
        t.name AS typeEventName, 
        t.id AS typeEventId
    FROM events_table AS e
    INNER JOIN type_event_table AS t ON e.typeEventId = t.id
    ORDER BY e.date, e.time
    """
    )
    fun findAll(): Flow<List<EventWithType>>

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