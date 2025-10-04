package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskclass.core.data.model.TypeEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeEventDao {

    @Insert
    suspend fun save(data: TypeEvent)

    @Query("SELECT * FROM type_event_table WHERE id = :id")
    fun findById(id: Int): Flow<TypeEvent>

    @Query("SELECT * FROM type_event_table")
    fun findAll(): Flow<List<TypeEvent>>

    @Query("DELETE FROM type_event_table WHERE id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(data: TypeEvent)

}