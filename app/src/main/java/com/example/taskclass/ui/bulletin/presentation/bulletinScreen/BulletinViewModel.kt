package com.example.taskclass.ui.bulletin.presentation.bulletinScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.collections.listOf

@HiltViewModel
class BulletinViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BulletinUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                scores = listOf(
                    DisciplineReport(
                        name = "Matemática",
                        teacher = "Prof. Letícia",
                        grades = listOf(7.5, 8.0, 9.0)
                    ),
                    DisciplineReport(
                        name = "História",
                        teacher = "Prof. Carlos",
                        grades = listOf(6.0, 5.5, 4.0)
                    ),
                    DisciplineReport(
                        name = "Química",
                        teacher = "Prof. Ana",
                        grades = listOf(3.0, 4.5, 5.0)
                    )
                )
            )
        }
    }

    fun changeShowScore() {
        _uiState.update {
            it.copy(showScores = it.showScores.not())
        }
    }
}