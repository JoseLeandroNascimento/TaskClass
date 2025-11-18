package com.example.taskclass.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.taskclass.core.data.model.entity.DisciplineEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface DisciplineDao {

    @Insert
    suspend fun save(data: DisciplineEntity)

    @Query("SELECT * FROM discipline_table WHERE discipline_id = :id")
    fun findById(id: Int): Flow<DisciplineEntity>

    @Query(
        """
        SELECT * 
        FROM 
        discipline_table
        WHERE
        (:title IS NULL OR title LIKE '%' || :title || '%') AND
        (:createdAt IS NULL OR title = :createdAt) AND
        (:updatedAt IS NULL OR title = :updatedAt)
        """
    )
    fun findAll(
        title: String? = null,
        createdAt: Long? = null,
        updatedAt: Long? = null,
    ): Flow<List<DisciplineEntity>>

    @Query("DELETE FROM discipline_table WHERE discipline_id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(data: DisciplineEntity)

}