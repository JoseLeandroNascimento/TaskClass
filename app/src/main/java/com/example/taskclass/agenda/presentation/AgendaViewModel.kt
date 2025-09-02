package com.example.taskclass.agenda.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.schedules.domain.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AgendaViewModel @Inject constructor(
    private val repo: ScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AgendaUiState())
    val uiState: StateFlow<AgendaUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            repo.findAll().collect { response ->
                _uiState.update {
                    it.copy(schedules = response)
                }
            }
        }
    }
}