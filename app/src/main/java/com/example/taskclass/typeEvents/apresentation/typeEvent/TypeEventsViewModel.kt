package com.example.taskclass.typeEvents.apresentation.typeEvent

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent
import com.example.taskclass.typeEvents.domain.TypeEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
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
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            repo.findAll().collectLatest { response ->
                _uiState.update { it.copy(typeEvents = response) }
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

    fun changeBottomSheetState(value: Boolean) {

        if (!value)
            resetForm()

        _uiState.update {
            it.copy(
                showBottomSheet = value
            )
        }
    }

    private fun resetForm() {
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

    fun onSelectedItemEdit(id: Int) {

        viewModelScope.launch {

            repo.findById(id)
                .distinctUntilChanged { old, new -> old is Resource.Success && new is Resource.Success && old.data.id == new.data.id }
                .collect { response ->

                    when (response) {

                        is Resource.Loading -> {
                        }

                        is Resource.Success -> {

                            _formState.update {
                                it.copy(
                                    id = it.id.updateValue(response.data.id),
                                    nameTypeEvent = it.nameTypeEvent.updateValue(response.data.name),
                                    colorTypeEvent = it.colorTypeEvent.updateValue(response.data.color)
                                )
                            }
                            changeBottomSheetState(true)
                        }

                        is Resource.Error -> {
                        }
                    }
                }
        }
    }

    fun save() {

        if (isValid()) {

            formState.value.apply {

                val data = TypeEvent(
                    id = id.value ?: 0,
                    name = nameTypeEvent.value,
                    color = colorTypeEvent.value
                )

                viewModelScope.launch {
                    repo.save(data).collect { response ->

                        when (response) {

                            is Resource.Loading -> {

                            }

                            is Resource.Success -> {
                                changeBottomSheetState(false)
                            }

                            is Resource.Error -> {

                            }
                        }
                    }
                }
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            repo.delete(id).collect { response ->

            }
        }
    }

}