package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.taskclass.core.data.model.Discipline
import kotlinx.coroutines.flow.Flow

@Dao
interface DisciplineDao {

    @Insert
    suspend fun save(data: Discipline)

    @Query("SELECT * FROM discipline_table")
    fun findAll(): Flow<List<Discipline>>

    @Query("DELETE FROM discipline_table WHERE discipline_id = :id")
    suspend fun delete(id: Int)

}