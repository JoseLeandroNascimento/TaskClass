package com.example.taskclass.core.data

import com.example.taskclass.common.data.Resource
import com.example.taskclass.discipline.domain.DisciplineRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    override suspend fun update(data: Discipline) {
    }

    override suspend fun findAll(): Flow<Resource<List<Discipline>>> {
        return flow {
            try {
                emit(Resource.Loading())
                dao.findAll().collect {
                    emit(Resource.Success(it))
                }
            } catch (e: Exception) {
                emit(Resource.Error("Error desconhecido"))
            }
        }
    }

    override suspend fun delete(id: Int) {
        dao.delete(id)
    }
}