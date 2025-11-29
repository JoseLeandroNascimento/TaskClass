package com.example.taskclass.core.data.repository

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.TypeEventDao
import com.example.taskclass.core.data.model.entity.TypeEventEntity
import com.example.taskclass.ui.typeEvents.domain.TypeEventRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TypeEventRepositoryImpl @Inject constructor(
    private val dao: TypeEventDao
) : TypeEventRepository {

    override fun save(data: TypeEventEntity): Flow<Resource<TypeEventEntity>> {
        return flow {
            emit(Resource.Loading())
            try {
                if(data.id == 0){
                    dao.save(data)
                }else{
                    dao.update(data)
                }
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun findAll(): Flow<Resource<List<TypeEventEntity>>> {

        return flow {
            emit(Resource.Loading())
            try {
                dao.findAll().collect {
                    emit(Resource.Success(it))
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