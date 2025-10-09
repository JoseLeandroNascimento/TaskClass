package com.example.taskclass.main

import com.example.taskclass.core.data.dao.DisciplineDao
import com.example.taskclass.core.data.dao.EventDao
import com.example.taskclass.core.data.dao.ScheduleDao
import com.example.taskclass.core.data.dao.TypeEventDao
import com.example.taskclass.core.data.repository.DisciplineRepositoryImpl
import com.example.taskclass.core.data.repository.EventRepositoryImpl
import com.example.taskclass.core.data.repository.ScheduleRepositoryImpl
import com.example.taskclass.core.data.repository.TypeEventRepositoryImpl
import com.example.taskclass.discipline.domain.DisciplineRepository
import com.example.taskclass.events.domain.EventRepository
import com.example.taskclass.schedules.domain.ScheduleRepository
import com.example.taskclass.typeEvents.domain.TypeEventRepository
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

    @Provides
    @Singleton
    fun provideScheduleRepository(dao: ScheduleDao): ScheduleRepository {
        return ScheduleRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideTypeEventRepository(dao: TypeEventDao): TypeEventRepository {
        return TypeEventRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideEventRepository(dao: EventDao): EventRepository {
        return EventRepositoryImpl(dao)
    }
}