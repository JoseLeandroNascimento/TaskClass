package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.example.taskclass.core.data.model.entity.EventEntity
import com.example.taskclass.core.data.model.enums.EEventStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Transaction
    @Query(
        """
        SELECT 
            *
        FROM events_table e 
        ORDER BY e.datetime ASC
    """
    )
    fun findAllWithType(): Flow<List<EventEndTypeEventDto>>

    @Transaction
    @Query(
        """
        SELECT 
            *
        FROM events_table e
        WHERE e.id = :id
    """
    )
    fun findByIdWithType(id: Int): Flow<EventEndTypeEventDto?>

    @Transaction
    @Query(
        """
         SELECT 
            *
    FROM events_table e
    WHERE (:id IS NULL OR e.id = :id)
      AND (:query is NULL OR e.title LIKE '%' || :query || '%')
      AND (:typeEventId IS NULL OR e.typeEventId = :typeEventId)
      AND (:completed IS NULL OR e.completed = :completed)
    """
    )
    fun search(
        id: Int? = null,
        query: String? = null,
        typeEventId: Int? = null,
        completed: Boolean? = null,
    ): Flow<List<EventEndTypeEventDto>>


    @Query("UPDATE events_table SET completed = :isCompleted WHERE id = :id")
    suspend fun updateCompleted(id: Int, isCompleted: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Query("DELETE FROM events_table WHERE id = :id")
    suspend fun deleteById(id: Int)
}
