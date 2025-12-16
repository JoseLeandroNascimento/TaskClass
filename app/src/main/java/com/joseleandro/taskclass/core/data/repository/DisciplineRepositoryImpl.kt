package com.joseleandro.taskclass.core.data.repository

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.dao.DisciplineDao
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity
import com.joseleandro.taskclass.ui.discipline.domain.DisciplineRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DisciplineRepositoryImpl @Inject constructor(
    private val dao: DisciplineDao
) : DisciplineRepository {

    override suspend fun save(data: DisciplineEntity): Flow<Resource<DisciplineEntity>> {

        return flow {
            try {
                emit(Resource.Loading())
                if (data.id == 0)
                    dao.save(data)
                else
                    dao.update(data)
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error("Error desconhecido"))
            }
        }
    }

    override suspend fun findById(id: Int): Flow<Resource<DisciplineEntity>> {

        return flow {

            try {
                emit(Resource.Loading())

                dao.findById(id).collect { response ->
                    emit(Resource.Success(response))
                }

            } catch (e: Exception) {
                emit(Resource.Error(message = "Disciplina não encontrada"))
            }
        }

    }

    override suspend fun findAll(
        title: String?,
        createdAt: Long?,
        updatedAt: Long?,
        order: Order<DisciplineEntity>
    ): Flow<Resource<List<DisciplineEntity>>> {
        return flow {
            try {
                emit(Resource.Loading())
                dao.findAll(
                    title = title,
                ).map { list ->
                    val comparator = Comparator<DisciplineEntity> { a, b ->
                        val va = order.getValue(a)
                        val vb = order.getValue(b)

                        when {
                            va == null && vb == null -> 0
                            va == null -> if (order.ascending) -1 else 1
                            vb == null -> if (order.ascending) 1 else -1
                            else -> {

                                val ca = va as Comparable<Any>
                                val cb = vb as Comparable<Any>

                                if (order.ascending) ca.compareTo(cb)
                                else cb.compareTo(ca)
                            }
                        }
                    }

                    list.sortedWith(comparator)
                }.collect {
                    emit(Resource.Success(it))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error desconhecido ${e.message}"))
            }
        }
    }

    override suspend fun delete(id: Int) {
        dao.delete(id)
    }
}