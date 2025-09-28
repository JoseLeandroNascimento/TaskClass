package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.taskclass.core.data.model.TypeEvent
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeEventDao {

    @Insert
    suspend fun save(data: TypeEvent)

    @Query("SELECT * FROM type_event_table")
    fun findAll(): Flow<List<TypeEvent>>
}