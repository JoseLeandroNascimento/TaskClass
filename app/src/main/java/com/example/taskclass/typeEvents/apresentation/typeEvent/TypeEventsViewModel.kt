package com.example.taskclass.typeEvents.apresentation.typeEvent

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class TypeEventsViewModel @Inject constructor() : ViewModel() {

    private val _formState = MutableStateFlow(TypeEventFormState())
    val formState: StateFlow<TypeEventFormState> = _formState.asStateFlow()

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

    fun resetForm(){
        _formState.value = TypeEventFormState()
    }

}