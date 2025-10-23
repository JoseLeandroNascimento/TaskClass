package com.example.taskclass.events.presentation.eventsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.events.domain.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllEvents().collect { response ->
                _uiState.update {
                    Log.d("teste", response.toString())
                    it.copy(events = response)
                }
            }
        }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(dateSelected = date) }
    }

}