package com.joseleandro.taskclass.ui.notes.domain

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun saveNew(title: String, html: String, plain: String): Flow<Resource<NoteEntity>>
    suspend fun update(id: Int, title: String, html: String, plain: String): Flow<Resource<NoteEntity>>
    suspend fun load(id: Int): Flow<NoteEntity?>
    fun findAll(): Flow<Resource<List<NoteEntity>>>
    suspend fun delete(id: Int)
}