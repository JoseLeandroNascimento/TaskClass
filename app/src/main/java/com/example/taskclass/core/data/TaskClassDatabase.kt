package com.example.taskclass.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.taskclass.core.data.converters.Converters
import com.example.taskclass.core.data.dao.DisciplineDao
import com.example.taskclass.core.data.dao.EventDao
import com.example.taskclass.core.data.dao.ScheduleDao
import com.example.taskclass.core.data.dao.TypeEventDao
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.Schedule
import com.example.taskclass.core.data.model.TypeEvent

@Database(
    entities = [
        Discipline::class,
        Schedule::class,
        TypeEvent::class,
        EventEntity::class
    ],
    version = 1
)
@TypeConverters(
    Converters::class
)
abstract class TaskClassDatabase : RoomDatabase() {
    abstract fun disciplineDao(): DisciplineDao
    abstract fun scheduleDao(): ScheduleDao
    abstract fun typeEventDao(): TypeEventDao
    abstract fun eventDao(): EventDao
}