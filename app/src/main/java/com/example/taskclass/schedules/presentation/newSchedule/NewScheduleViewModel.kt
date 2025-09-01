package com.example.taskclass.schedules.presentation.newSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.discipline.domain.DisciplineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NewScheduleViewModel @Inject constructor(
    private val repo: DisciplineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewScheduleUiState())
    val uiState: StateFlow<NewScheduleUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            repo.findAll().collect { response ->
                _uiState.update {
                    it.copy(disciplines = response)
                }
            }
        }
    }

}