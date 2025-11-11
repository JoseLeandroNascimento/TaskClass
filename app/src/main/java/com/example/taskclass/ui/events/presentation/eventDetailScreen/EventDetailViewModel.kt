package com.example.taskclass.ui.events.presentation.eventDetailScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.ui.events.domain.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EventDetailViewModel @Inject constructor(
    private val repository: EventRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val LOG_TAG = "EVENT_DETAIL_VIEWMODEL"
    }

    private val _uiState = MutableStateFlow(EventDetailUiState())
    val uiState: StateFlow<EventDetailUiState> = _uiState.asStateFlow()

    private val eventId: Int? = savedStateHandle["eventId"]

    init {
        eventId?.let {
            Log.d(LOG_TAG, "Carregando evento com id: $it")
            loadEvent(it)
        } ?: Log.e(LOG_TAG, "Nenhum ID de evento foi passado na navegação")
    }

    private fun loadEvent(id: Int) {
        viewModelScope.launch {
            repository.findById(id).collect { response ->
                when (response) {
                    is Resource.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Resource.Success -> _uiState.update {
                        it.copy(isLoading = false, event = response.data, error = null)
                    }
                    is Resource.Error -> _uiState.update {
                        it.copy(isLoading = false, error = response.message)
                    }
                }

            }
        }
    }
}


