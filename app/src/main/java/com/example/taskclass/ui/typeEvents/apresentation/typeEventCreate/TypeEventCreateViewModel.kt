package com.example.taskclass.ui.typeEvents.apresentation.typeEventCreate

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent
import com.example.taskclass.ui.typeEvents.domain.TypeEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TypeEventCreateViewModel @Inject constructor(
    private val repo: TypeEventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TypeEventUiState())
    val uiState: StateFlow<TypeEventUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    nameTypeEvent = it.formState.nameTypeEvent.updateValue(title)
                )
            )
        }
    }

    fun updateColor(color: Color) {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    colorTypeEvent = it.formState.colorTypeEvent.updateValue(color)
                )
            )
        }
    }

    private fun isValid(): Boolean {
        _uiState.update {
            it.copy(
                formState = it.formState.copy(
                    nameTypeEvent = it.formState.nameTypeEvent.validate(),
                    colorTypeEvent = it.formState.colorTypeEvent.validate()
                )
            )
        }
        return _uiState.value.formState.nameTypeEvent.isValid && _uiState.value.formState.colorTypeEvent.isValid
    }

    fun save() {

        if (isValid()) {

            uiState.value.formState.apply {

                val data = TypeEvent(
                    id = id.value ?: 0,
                    name = nameTypeEvent.value,
                    color = colorTypeEvent.value
                )

                viewModelScope.launch {
                    repo.save(data).collect { response ->

                        when (response) {

                            is Resource.Loading -> {
                                _uiState.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is Resource.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isBackNavigation = true
                                    )
                                }
                            }

                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(isLoading = false)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}