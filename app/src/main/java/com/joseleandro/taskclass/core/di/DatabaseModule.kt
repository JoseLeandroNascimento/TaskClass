package com.joseleandro.taskclass.core.di

import android.content.Context
import androidx.room.Room
import com.joseleandro.taskclass.core.data.TaskClassDatabase
import com.joseleandro.taskclass.core.data.dao.DisciplineDao
import com.joseleandro.taskclass.core.data.dao.EventDao
import com.joseleandro.taskclass.core.data.dao.ScheduleDao
import com.joseleandro.taskclass.core.data.dao.TypeEventDao
import com.joseleandro.taskclass.core.data.dao.NoteDao
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

    @Provides
    fun provideTypeEventDao(db: TaskClassDatabase): TypeEventDao = db.typeEventDao()

    @Provides
    fun provideEventDao(db: TaskClassDatabase): EventDao = db.eventDao()

    @Provides
    fun provideNoteDao(db: TaskClassDatabase): NoteDao = db.noteDao()
}