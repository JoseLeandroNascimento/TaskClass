package com.example.taskclass.ui.bulletin.presentation.bulletinScreen

data class BulletinUiState(
    val showScores: Boolean = true,
    val scores: List<DisciplineReport> = emptyList()
)
