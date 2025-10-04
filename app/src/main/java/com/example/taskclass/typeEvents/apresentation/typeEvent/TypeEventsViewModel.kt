package com.example.taskclass.typeEvents.apresentation.typeEvent

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.core.data.model.TypeEvent
import com.example.taskclass.typeEvents.domain.TypeEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TypeEventsViewModel @Inject constructor(
    private val repo: TypeEventRepository
) : ViewModel() {

    private val _formState = MutableStateFlow(TypeEventFormState())
    val formState: StateFlow<TypeEventFormState> = _formState.asStateFlow()
    private val _uiState = MutableStateFlow(TypeEventsUiState())
    val uiState: StateFlow<TypeEventsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repo.findAll().collect { response ->
                _uiState.update {
                    it.copy(typeEvents = response)
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _formState.update {
            it.copy(nameTypeEvent = it.nameTypeEvent.updateValue(title))
        }
    }

    fun updateColor(color: Color) {
        _formState.update {
            it.copy(colorTypeEvent = it.colorTypeEvent.updateValue(color))
        }
    }

    fun resetForm() {
        _formState.value = TypeEventFormState()
    }

    private fun isValid(): Boolean {
        _formState.update {
            it.copy(
                nameTypeEvent = it.nameTypeEvent.validate(),
                colorTypeEvent = it.colorTypeEvent.validate()
            )
        }
        return _formState.value.nameTypeEvent.isValid && _formState.value.colorTypeEvent.isValid
    }

    fun save() {

        if (isValid()) {
            val data = TypeEvent(
                name = _formState.value.nameTypeEvent.value,
                color = _formState.value.colorTypeEvent.value
            )
            viewModelScope.launch {
                repo.save(data).collect {

                }
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            repo.delete(id).collect {response ->

            }
        }
    }

}