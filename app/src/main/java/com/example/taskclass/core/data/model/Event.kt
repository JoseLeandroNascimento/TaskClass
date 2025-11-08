package com.example.taskclass.core.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events_table")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val date: DateInt,  // <-- agora usa DateInt
    val time: Time,     // <-- e aqui Time
    val typeEventId: Int?,
    val typeEventName: String?,
    val status: EEventStatus = EEventStatus.AGENDADO
)