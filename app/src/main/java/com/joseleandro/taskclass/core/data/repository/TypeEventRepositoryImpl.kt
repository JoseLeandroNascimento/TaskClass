package com.joseleandro.taskclass.core.data.repository

import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.dao.TypeEventDao
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import com.joseleandro.taskclass.ui.typeEvents.domain.TypeEventRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class TypeEventRepositoryImpl @Inject constructor(
    private val dao: TypeEventDao
) : TypeEventRepository {

    override fun save(data: TypeEventEntity): Flow<Resource<TypeEventEntity>> {
        return flow {
            emit(Resource.Loading())
            try {
                if (data.id == 0) {
                    dao.save(data)
                } else {
                    dao.update(data)
                }
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun findAll(
        query: String?,
        order: Order<TypeEventEntity>
    ): Flow<Resource<List<TypeEventEntity>>> {

        return flow {
            emit(Resource.Loading())
            try {
                dao.findAll(name = query)
                    .map { response ->

                        val comparator = Comparator<TypeEventEntity> { a, b ->
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
                        response.sortedWith(comparator)

                    }
                    .collect { response ->
                        emit(Resource.Success(response))
                    }

            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }

        }
    }

    override fun findById(id: Int): Flow<Resource<TypeEventEntity>> {
        return flow {
            emit(Resource.Loading())
            try {
                dao.findById(id).collect {
                    emit(Resource.Success(it))
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }

    }

    override fun delete(id: Int): Flow<Resource<TypeEventEntity>> {

        return flow {

            emit(Resource.Loading())

            try {
                with(dao) {
                    findById(id)
                        .catch {
                            emit(Resource.Error(it.message.toString()))
                        }
                        .collect { response ->
                            delete(response.id)
                            emit(Resource.Success(response))
                        }
                }

            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

}