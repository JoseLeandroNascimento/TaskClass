package com.example.taskclass.core.data.repository

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.entity.NoteEntity
import com.example.taskclass.core.data.dao.NoteDao
import com.example.taskclass.ui.notes.domain.NoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override suspend fun saveNew(
        title: String,
        html: String,
        plain: String
    ): Flow<Resource<NoteEntity>> {

        return flow {
            try {

                emit(Resource.Loading())
                val now = System.currentTimeMillis()
                val data = NoteEntity(
                    title = title,
                    html = html,
                    plain = plain,
                    createdAt = now,
                    updatedAt = now
                )

                dao.insert(
                    data
                )

                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown Error"))
            }

        }
    }

    override suspend fun update(
        id: Int,
        title: String,
        html: String,
        plain: String
    ): Flow<Resource<NoteEntity>> {

        return flow {

            try {
                emit(Resource.Loading())
                dao.findById(id).collect { response ->
                    response?.let { old ->
                        val data = old.copy(
                            title = title,
                            html = html,
                            plain = plain,
                            updatedAt = System.currentTimeMillis()
                        )
                        dao.update(
                            data
                        )
                        emit(Resource.Success(data))
                    }
                }


            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown Error"))
            }
        }
    }

    override suspend fun load(id: Int): Flow<NoteEntity?> {
        return dao.findById(id)
    }

    override fun findAll(): Flow<Resource<List<NoteEntity>>> {

        return flow {
            try {
                emit(Resource.Loading())
                dao.listAll().collect { data ->
                    emit(Resource.Success(data))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown Error"))
            }
        }

    }

    override suspend fun delete(id: Int) {
        TODO("Not yet implemented")
    }
}