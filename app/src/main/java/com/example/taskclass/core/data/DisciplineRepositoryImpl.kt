package com.example.taskclass.core.data

import com.example.taskclass.discipline.domain.DisciplineRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class DisciplineRepositoryImpl @Inject constructor(
    private val dao: DisciplineDao
) : DisciplineRepository {

    override suspend fun save(data: Discipline) {
        dao.save(data)
    }

    override suspend fun update(data: Discipline) {
    }

    override suspend fun findAll(): Flow<List<Discipline>> {
        return dao.findAll()
    }
}