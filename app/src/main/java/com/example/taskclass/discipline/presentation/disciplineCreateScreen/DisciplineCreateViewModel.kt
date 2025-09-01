package com.example.taskclass.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.FieldState
import com.example.taskclass.core.data.Discipline
import com.example.taskclass.discipline.domain.CreateDisciplineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DisciplineCreateViewModel @Inject constructor(
    private val createDisciplineUseCase: CreateDisciplineUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisciplineCreateUiState())
    val uiState: StateFlow<DisciplineCreateUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {

        var messageError: String? = null

        if (title.length < 3) {
            messageError = "Nome da disciplina deve ter no mínimo 3 carecteres"
        }
        if (title.isBlank()) {
            messageError = "Nome da disciplina não pode ser vazio"
        }

        _uiState.update {
            it.copy(
                title = FieldState(value = title, error = messageError)
            )
        }
    }

    fun updateTeacherName(teacherName: String) {
        _uiState.update {
            it.copy(
                teacherName = FieldState(value = teacherName)
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

        val data = Discipline(
            id = null,
            color = _uiState.value.colorSelect,
            title = _uiState.value.title.value,
            teacherName = _uiState.value.teacherName.value
        )

        viewModelScope.launch {
            createDisciplineUseCase.save(data)
            _uiState.update {
                it.copy(saveSuccess = true)
            }
        }

    }

}