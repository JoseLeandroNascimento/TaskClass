package com.example.taskclass.core.data.dao

import androidx.room.*
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.dto.EventWithType
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query(
        """
        SELECT 
            e.id,
            e.title,
            e.description,
            e.date,
            e.time,
            e.typeEventId,
            t.name AS typeEventName,
            t.color AS typeEventColor,
            e.status
        FROM events_table e
        LEFT JOIN type_event_table t ON e.typeEventId = t.id
        ORDER BY e.date ASC
    """
    )
    fun findAllWithType(): Flow<List<EventWithType>> // ✅ Flow reativo

    @Query(
        """
        SELECT 
            e.id,
            e.title,
            e.description,
            e.date,
            e.time,
            e.typeEventId,
            t.name AS typeEventName,
            t.color AS typeEventColor,
            e.status
        FROM events_table e
        LEFT JOIN type_event_table t ON e.typeEventId = t.id
        WHERE e.id = :id
    """
    )
    fun findByIdWithType(id: Int): Flow<EventWithType?> // ✅ Flow reativo

    @Query(
        """
         SELECT 
        e.id,
        e.title,
        e.description,
        e.date,
        e.time,
        e.typeEventId,
        t.name AS typeEventName,
        t.color AS typeEventColor,
        e.status
    FROM events_table e
    LEFT JOIN type_event_table t ON e.typeEventId = t.id
    WHERE (:id IS NULL OR e.id = :id)
      AND (:title IS NULL OR e.title LIKE '%' || :title || '%')
      AND (:date IS NULL OR e.date = :date)
      AND (:time IS NULL OR e.time = :time)
      AND (:typeEventId IS NULL OR e.typeEventId = :typeEventId)
      AND (:status IS NULL OR e.status = :status)
    """
    )
    fun search(
        id: Int? = null,
        title: String? = null,
        date: String? = null,
        time: String? = null,
        typeEventId: Int? = null,
        status: EEventStatus? = null
    ): Flow<List<EventWithType>>


    @Query("UPDATE events_table SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Int, status: EEventStatus)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Update
    suspend fun update(event: EventEntity)

    @Query("DELETE FROM events_table WHERE id = :id")
    suspend fun deleteById(id: Int)
}
