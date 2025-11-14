package com.example.taskclass.ui.events.presentation.eventsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.ui.events.domain.CheckedEventUseCase
import com.example.taskclass.ui.events.domain.EventRepository
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
    private val repository: EventRepository,
    private val checkedEventUseCase: CheckedEventUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.findAll().collect { response ->

                when (response) {

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                events = response.data,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }

            }
        }
    }

    fun onCheckedStatusEvent(id: Int, isChecked: Boolean) {

        viewModelScope.launch {
            checkedEventUseCase(id, isChecked).collect { response ->
                when (response) {

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {

                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                    }
                }
            }
        }

    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(dateSelected = date) }
    }

}