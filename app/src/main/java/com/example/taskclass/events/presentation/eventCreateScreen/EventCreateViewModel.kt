package com.example.taskclass.events.presentation.eventCreateScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.core.data.converters.Converters
import com.example.taskclass.core.data.model.DateInt
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.core.data.model.Time
import com.example.taskclass.core.data.model.TypeEvent
import com.example.taskclass.events.domain.EventRepository
import com.example.taskclass.typeEvents.domain.TypeEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EventCreateViewModel @Inject constructor(
    private val typeEventRepository: TypeEventRepository,
    private val eventRepository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventCreateUiState())
    val uiState: StateFlow<EventCreateUiState> = _uiState.asStateFlow()

    private val converters = Converters()

    init {
        loadTypesEvents()
    }

    private fun loadTypesEvents() {
        viewModelScope.launch {
            typeEventRepository.findAll().collect { response ->
                _uiState.update {
                    it.copy(typeEvents = response)
                }
            }
        }
    }

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

    fun updateTypeEvent(typeEvent: TypeEvent) {
        _uiState.update {
            it.copy(typeEventSelected = it.typeEventSelected.updateValue(typeEvent))
        }
    }

    private fun isValid(): Boolean {
        _uiState.update {
            it.copy(
                title = it.title.validate(),
                date = it.date.validate(),
                time = it.time.validate(),
                description = it.description.validate()
            )
        }

        val form = _uiState.value
        return form.title.isValid &&
                form.date.isValid &&
                form.time.isValid
    }

    fun saveEvent() {
        viewModelScope.launch {
            if (!isValid()) return@launch

            val ui = _uiState.value

            val event = EventEntity(
                title = ui.title.value,
                description = ui.description.value,
                date = converters.toDate(ui.date.value) ?: DateInt(0),
                time = converters.fromTimeString(ui.time.value) ?: Time(0),
                typeEventId = ui.typeEventSelected.value?.id,
                typeEventName = ui.typeEventSelected.value?.name
            )

            eventRepository.save(event).collect { response ->
                _uiState.update {
                    it.copy(typeEvents = it.typeEvents, eventResponse = response)
                }
            }
        }
    }
}
