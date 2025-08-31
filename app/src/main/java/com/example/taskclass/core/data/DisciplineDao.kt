package com.example.taskclass.core.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DisciplineDao {

    @Insert
    suspend fun save(data: Discipline)

    @Query("SELECT * FROM discipline_table")
    fun findAll(): Flow<List<Discipline>>

}