package com.example.taskclass.core.data.model

enum class EEventStatus(
    private val description: String
) {
    AGENDADO("AGENDADO"),
    PENDENTE("PENDENTE"),
    CONCLUIDA("CONCLUIDA")
}