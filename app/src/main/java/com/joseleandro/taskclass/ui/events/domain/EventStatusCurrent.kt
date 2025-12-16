package com.joseleandro.taskclass.ui.events.domain

import com.joseleandro.taskclass.core.data.model.entity.EventEntity
import com.joseleandro.taskclass.core.data.model.enums.EEventStatus
import java.time.LocalDate
import java.time.ZoneId

/**
 * Retorna o status atual do evento.
 */
fun EventEntity.statusCurrent(): EEventStatus{

    val eventDate = this.datetime
        .atZone(ZoneId.systemDefault())
        .toLocalDate()

    val today = LocalDate.now()

    return when {
        this.completed -> EEventStatus.CONCLUIDO
        eventDate == today -> EEventStatus.HOJE
        eventDate.isAfter(today) -> EEventStatus.AGENDADO
        else -> EEventStatus.ATRASADO
    }
}