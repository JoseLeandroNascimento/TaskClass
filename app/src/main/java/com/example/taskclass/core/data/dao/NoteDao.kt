package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskclass.core.data.model.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert
    suspend fun insert(note: NoteEntity)

    @Update
    suspend fun update(note: NoteEntity)

    @Query("SELECT * FROM notes_table WHERE id = :id")
     fun findById(id: Int): Flow<NoteEntity?>

    @Query("SELECT * FROM notes_table ORDER BY updatedAt DESC")
     fun listAll(): Flow<List<NoteEntity>>
}