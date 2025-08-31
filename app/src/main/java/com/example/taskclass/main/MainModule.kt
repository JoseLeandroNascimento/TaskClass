package com.example.taskclass.main

import com.example.taskclass.core.data.DisciplineDao
import com.example.taskclass.core.data.DisciplineRepositoryImpl
import com.example.taskclass.discipline.domain.DisciplineRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun provideDisciplineRepository(dao: DisciplineDao): DisciplineRepository {
        return DisciplineRepositoryImpl(dao)
    }
}