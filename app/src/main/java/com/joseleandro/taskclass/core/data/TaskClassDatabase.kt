package com.joseleandro.taskclass.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.joseleandro.taskclass.core.data.converters.Converters
import com.joseleandro.taskclass.core.data.dao.DisciplineDao
import com.joseleandro.taskclass.core.data.dao.EventDao
import com.joseleandro.taskclass.core.data.dao.ScheduleDao
import com.joseleandro.taskclass.core.data.dao.TypeEventDao
import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity
import com.joseleandro.taskclass.core.data.model.entity.EventEntity
import com.joseleandro.taskclass.core.data.model.entity.NoteEntity
import com.joseleandro.taskclass.core.data.model.entity.ScheduleEntity
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import com.joseleandro.taskclass.core.data.dao.NoteDao

@Database(
    entities = [
        DisciplineEntity::class,
        ScheduleEntity::class,
        TypeEventEntity::class,
        EventEntity::class,
        NoteEntity::class
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
    abstract fun noteDao(): NoteDao
}