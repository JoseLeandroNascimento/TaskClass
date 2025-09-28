package com.example.taskclass.core.data.repository

import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.dao.TypeEventDao
import com.example.taskclass.core.data.model.TypeEvent
import com.example.taskclass.typeEvents.domain.TypeEventRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TypeEventRepositoryImpl @Inject constructor(
    private val dao: TypeEventDao
) : TypeEventRepository {

    override fun save(data: TypeEvent): Flow<Resource<TypeEvent>> {
        return flow {
            emit(Resource.Loading())
            try {
                dao.save(data)
                emit(Resource.Success(data))
            } catch (e: Exception) {
                emit(Resource.Error(e.message.toString()))
            }
        }
    }

    override fun findAll(): Flow<Resource<List<TypeEvent>>> {

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
}