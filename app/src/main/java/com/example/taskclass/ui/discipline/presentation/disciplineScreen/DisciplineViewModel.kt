package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.ui.discipline.domain.DisciplineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DisciplineViewModel @Inject constructor(
    private val repo: DisciplineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisciplineUiState())
    val uiState: StateFlow<DisciplineUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            repo.findAll().collect { disciplines ->
                _uiState.update {
                    it.copy(disciplines = disciplines)
                }
            }
        }

    }

    fun deleteDiscipline(id: Int) {

        viewModelScope.launch {
            repo.delete(id)
        }
    }

}