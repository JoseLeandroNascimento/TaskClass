package com.joseleandro.taskclass.ui.typeEvents.presentation.typeEventCreate

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.taskclass.common.data.AppNotification
import com.joseleandro.taskclass.common.data.NotificationCenter
import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import com.joseleandro.taskclass.ui.typeEvents.domain.TypeEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TypeEventCreateViewModel @Inject constructor(
    private val repo: TypeEventRepository,
    private val saveStateHandler: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TypeEventUiState())
    val uiState: StateFlow<TypeEventUiState> = _uiState.asStateFlow()

    private val typeId: String? = saveStateHandler["typeId"]

    init {

        typeId?.let {
            loadDataById(it.toInt())
        }
    }

    private fun loadDataById(id: Int) {

        viewModelScope.launch {
            repo.findById(id).collect { response ->

                when (response) {

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Resource.Success -> {

                        _uiState.update {
                            it.copy(
                                formState = it.formState.copy(
                                    id = it.formState.id.updateValue(response.data.id),
                                    createdAt = it.formState.createdAt.updateValue(response.data.createdAt),
                                ),
                                isLoading = false
                            )
                        }
                        updateTitle(response.data.name)
                        updateColor(response.data.color)

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

                val data = TypeEventEntity(
                    id = id.value ?: 0,
                    name = nameTypeEvent.value,
                    color = colorTypeEvent.value,
                    createdAt = createdAt.value ?: System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )

                viewModelScope.launch {
                    repo.save(data).collect { response ->

                        when (response) {

                            is Resource.Loading -> {
                                _uiState.update {
                                    it.copy(isLoadingButton = true)
                                }
                            }

                            is Resource.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoadingButton = false,
                                        isBackNavigation = true
                                    )
                                }

                                NotificationCenter.push(AppNotification.Success("Tipo de evento salvo com sucesso"))
                            }

                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(isLoadingButton = false)
                                }

                                NotificationCenter.push(AppNotification.Error(response.message))
                            }
                        }
                    }
                }
            }
        }
    }
}