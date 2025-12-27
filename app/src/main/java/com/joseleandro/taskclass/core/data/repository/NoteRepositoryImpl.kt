package com.joseleandro.taskclass.core.data.repository

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.dao.NoteDao
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.core.data.model.entity.NoteEntity
import com.joseleandro.taskclass.ui.notes.domain.NoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

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

    override fun findAll(sort: Order<NoteEntity>): Flow<Resource<List<NoteEntity>>> {

        return flow {
            try {
                emit(Resource.Loading())
                dao.listAll()
                    .map { list ->

                        val comparator = Comparator<NoteEntity> { a, b ->
                            val va = sort.getValue(a)
                            val vb = sort.getValue(b)

                            when {
                                va == null && vb == null -> 0
                                va == null -> if (sort.ascending) -1 else 1
                                vb == null -> if (sort.ascending) 1 else -1
                                else -> {

                                    val ca = va as Comparable<Any>
                                    val cb = vb as Comparable<Any>

                                    if (sort.ascending) ca.compareTo(cb)
                                    else cb.compareTo(ca)
                                }
                            }
                        }

                        list.sortedWith(comparator)
                    }
                    .collect { data ->
                    emit(Resource.Success(data))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown Error"))
            }
        }

    }


    override suspend fun deleteAll(notes: List<NoteEntity>): Flow<Resource<Unit>> {


        return flow {
            try {
                emit(Resource.Loading())

                dao.deleteAll(notes)


                emit(Resource.Success(Unit))
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Não foi possível excluir a(s) nota(s)"))
            }
        }
    }
}