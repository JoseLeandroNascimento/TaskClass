package com.example.taskclass.ui.events.presentation.eventAllScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.ui.events.domain.EventFilter
import com.example.taskclass.ui.events.domain.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EventAllViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventAllUiState())
    val uiState: StateFlow<EventAllUiState> = _uiState.asStateFlow()
    private val _filter = MutableStateFlow(EventFilter())
    val filter: StateFlow<EventFilter> = _filter.asStateFlow()

    init {
        search()
    }


    fun filterByStatus(status: EEventStatus?) {
        _filter.update {
            it.copy(
                status = status
            )
        }
    }

    fun updateStatusChecked(id: Int, isChecked: Boolean) {

        viewModelScope.launch {
            val status = if (isChecked) EEventStatus.CONCLUIDA else EEventStatus.AGENDADO
            repo.updateStatus(id, status).collect { response ->

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
                                isLoading = false,
                            )
                        }
                    }

                    is Resource.Error -> {

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.message
                            )
                        }
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun search() {

        viewModelScope.launch {

            _filter.flatMapLatest { filter ->
                repo.filter(filter)
            }.collect { response ->

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
                                isLoading = false,
                                events = response.data
                            )
                        }
                    }

                    is Resource.Error -> {

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.message
                            )
                        }
                    }
                }
            }
        }

    }
}
