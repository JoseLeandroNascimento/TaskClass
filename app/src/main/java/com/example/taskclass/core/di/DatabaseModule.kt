package com.example.taskclass.core.di

import android.content.Context
import androidx.room.Room
import com.example.taskclass.core.data.dao.DisciplineDao
import com.example.taskclass.core.data.TaskClassDatabase
import com.example.taskclass.core.data.dao.ScheduleDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskClassDatabase {
        return Room.databaseBuilder(
            context,
            TaskClassDatabase::class.java,
            "app_database",
        ).fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideDisciplineDao(db: TaskClassDatabase): DisciplineDao =
        db.disciplineDao()

    @Provides
    fun provideScheduleDao(db: TaskClassDatabase): ScheduleDao = db.scheduleDao()
}