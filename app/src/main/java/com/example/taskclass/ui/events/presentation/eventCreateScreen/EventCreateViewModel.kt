package com.example.taskclass.ui.events.presentation.eventCreateScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.common.utils.parseToInstant
import com.example.taskclass.core.data.model.entity.EventEntity
import com.example.taskclass.core.data.model.entity.TypeEventEntity
import com.example.taskclass.ui.events.domain.EventRepository
import com.example.taskclass.ui.typeEvents.domain.TypeEventRepository
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

    fun updateTypeEvent(typeEvent: TypeEventEntity) {
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

                val instant = parseToInstant(dateStr = validatedForm.date.value, timeStr = validatedForm.time.value)

                val event = EventEntity(
                    title = validatedForm.title.value,
                    description = validatedForm.description.value,
                    datetime = instant,
                    typeEventId = validatedForm.typeEventSelected.value!!.id,
                )

                eventRepository.save(event).collect { response ->
                    when(response){

                        is Resource.Loading -> {

                        }

                        is Resource.Success->{
                            _uiState.update {
                                it.copy(savedSuccessAndClose = true)
                            }
                        }

                        is Resource.Error ->{

                        }
                    }
                }
            }

        }
    }
}
