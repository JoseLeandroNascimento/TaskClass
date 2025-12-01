package com.example.taskclass.core.data.model.enums

enum class EEventStatus(
    private val description: String,
    val label: String
) {
    HOJE("HOJE", "Hoje"),
    AGENDADO("AGENDADO", "Proximos eventos"),
    ATRASADO("ATRASADO", "Evento Atrasado"),
    CONCLUIDO("CONCLUIDA", "Concluídos"),
}