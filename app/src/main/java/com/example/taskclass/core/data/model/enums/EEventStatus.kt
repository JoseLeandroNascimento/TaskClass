package com.example.taskclass.core.data.model.enums

enum class EEventStatus(
    private val description: String,
    val label: String
) {
    AGENDADO("AGENDADO", "Agendado"),
    PENDENTE("PENDENTE", "Pendente"),
    CONCLUIDA("CONCLUIDA", "Concluída"),
}