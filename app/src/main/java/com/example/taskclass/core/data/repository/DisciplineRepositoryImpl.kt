package com.example.taskclass.core.data.repository

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.DisciplineDao
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.core.data.model.Order
import com.example.taskclass.ui.discipline.domain.DisciplineRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class DisciplineRepositoryImpl @Inject constructor(
    private val dao: DisciplineDao
) : DisciplineRepository {

    override suspend fun save(data: Discipline): Flow<Resource<Discipline>> {

        return flow {
            try {
                emit(Resource.Loading())
                dao.save(data)
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error("Error desconhecido"))
            }
        }
    }

    override suspend fun update(data: Discipline): Flow<Resource<Discipline>> {

        return flow {

            try {
                emit(Resource.Loading())
                dao.update(data)
                emit(Resource.Success(data))

            } catch (e: Exception) {
                emit(Resource.Error("Erro ao atualizar"))
            }
        }
    }

    override suspend fun findById(id: Int): Flow<Resource<Discipline>> {

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
        order: Order<Discipline>
    ): Flow<Resource<List<Discipline>>> {
        return flow {
            try {
                emit(Resource.Loading())
                dao.findAll(
                    title = title,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                ).map { list ->
                    val comparator = Comparator<Discipline> { a, b ->
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