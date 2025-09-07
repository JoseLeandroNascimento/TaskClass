package com.example.taskclass.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.discipline.domain.DisciplineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DisciplineCreateViewModel @Inject constructor(
    private val repo: DisciplineRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisciplineCreateUiState())
    val uiState: StateFlow<DisciplineCreateUiState> = _uiState.asStateFlow()

    private val disciplineId: String? = savedStateHandle["disciplineId"]

    init {

        disciplineId?.let { idDiscipline ->

            _uiState.update {
                it.copy(idDiscipline = idDiscipline.toInt())
            }
            viewModelScope.launch {
                repo.findById(idDiscipline.toInt()).collect { response ->
                    when (response) {
                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {
                            updateTitle(response.data.title)
                            updateTeacherName(response.data.teacherName)
                            updateColorSelect(response.data.color)
                        }

                        is Resource.Error -> {

                        }
                    }
                }
            }
        }

    }

    fun updateTitle(title: String) {

        _uiState.update {
            it.copy(
                title = it.title.updateValue(title)
            )
        }
    }

    fun updateTeacherName(teacherName: String) {
        _uiState.update {
            it.copy(
                teacherName = it.teacherName.updateValue(teacherName)
            )
        }
    }

    fun updateColorSelect(color: Color) {
        _uiState.update {
            it.copy(
                colorSelect = color
            )
        }
    }

    fun changePickerColor(show: Boolean) {
        _uiState.update {
            it.copy(showPickerColor = show)
        }
    }

    fun save() {

        _uiState.update {
            it.copy(
                title = it.title.validate()
            )
        }

        if (_uiState.value.title.isValid) {

            val data = Discipline(
                color = _uiState.value.colorSelect,
                title = _uiState.value.title.value,
                teacherName = _uiState.value.teacherName.value
            )

            viewModelScope.launch {

                if (disciplineId == null) {

                    repo.save(data).collect { response ->
                        _uiState.update {
                            it.copy(disciplineResponse = response)
                        }
                    }
                } else {

                    repo.update(
                        data.copy(
                            id = disciplineId.toInt()
                        )
                    ).collect { response ->

                        _uiState.update {
                            it.copy(disciplineResponse = response)
                        }
                    }
                }
            }
        }

    }

}