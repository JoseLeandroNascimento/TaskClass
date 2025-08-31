package com.example.taskclass.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        Discipline::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TaskClassDatabase : RoomDatabase() {
    abstract fun disciplineDao(): DisciplineDao
}