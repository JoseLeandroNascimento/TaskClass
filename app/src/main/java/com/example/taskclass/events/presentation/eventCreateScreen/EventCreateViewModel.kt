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
            it.copy(
                formState = it.formState.copy(
                    title = it.formState.title.updateValue(title)
                )
            )
        }
    }

    fun updateDate(date: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    date = it.formState.date.updateValue(date)
                )
            )
        }
    }

    fun updateTime(time: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    time = it.formState.time.updateValue(time)
                )
            )
        }
    }

    fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    description = it.formState.description.updateValue(description)
                )
            )
        }
    }

    fun updateTypeEvent(typeEvent: TypeEvent) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    typeEventSelected = it.formState.typeEventSelected.updateValue(typeEvent)
                )
            )
        }
    }



    fun saveEvent() {
        viewModelScope.launch {

            val validatedForm = _uiState.value.formState.validateAll()

            _uiState.update {
                it.copy(formState = validatedForm)
            }


            if (validatedForm.isValid()){
                val event = EventEntity(
                    title = validatedForm.title.value,
                    description = validatedForm.description.value,
                    date = converters.toDate(validatedForm.date.value) ?: DateInt(0),
                    time = converters.fromTimeString(validatedForm.time.value) ?: Time(0),
                    typeEventId = validatedForm.typeEventSelected.value?.id,
                    typeEventName = validatedForm.typeEventSelected.value?.name
                )

                eventRepository.save(event).collect { response ->
                    _uiState.update {
                        it.copy(typeEvents = it.typeEvents, eventResponse = response)
                    }
                }
            }

        }
    }
}
