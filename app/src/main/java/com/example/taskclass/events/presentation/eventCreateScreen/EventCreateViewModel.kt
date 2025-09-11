package com.example.taskclass.events.presentation.eventCreateScreen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@HiltViewModel
class EventCreateViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EventCreateUiState())
    val uiState: StateFlow<EventCreateUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(title = it.title.updateValue(title))
        }
    }

    fun updateDate(date: String) {
        _uiState.update {
            it.copy(date = it.date.updateValue(date))
        }
    }

    fun updateTime(time: String) {
        _uiState.update {
            it.copy(time = it.time.updateValue(time))
        }
    }

    fun updateDescription(description: String) {
        _uiState.update {
            it.copy(description = it.description.updateValue(description))
        }
    }

}