package com.example.taskclass.ui.agenda.presentation

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.dto.ScheduleDTO
import com.example.taskclass.ui.schedules.domain.ScheduleRepository
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

    fun updateDayOfWeek() {
        val calendar = Calendar.getInstance()
        _uiState.update {
            it.copy(currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1)
        }
    }

    fun deleteSchedule() {
        _uiState.value.selectItem?.let {
            viewModelScope.launch {
                repo.deleteById(it.scheduleId).collect { response ->
                    when (response) {

                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            resetItemSelected()
                        }

                        is Resource.Error -> {

                        }
                    }
                }
            }
        }
    }

    fun resetItemSelected() {
        _uiState.update {
            it.copy(selectItem = null)
        }
    }

    fun selectedItem(data: ScheduleDTO) {
        _uiState.update {
            it.copy(selectItem = data)
        }
    }


}