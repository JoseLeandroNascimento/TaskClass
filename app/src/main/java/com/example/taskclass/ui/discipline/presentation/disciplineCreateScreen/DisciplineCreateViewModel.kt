package com.example.taskclass.ui.discipline.presentation.disciplineCreateScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.ui.discipline.domain.DisciplineRepository
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
                it.copy(
                    form = it.form.copy(
                        idDiscipline = idDiscipline.toInt()
                    )
                )
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
                            updateCreatedAt(response.data.createdAt)
                            updateUpdatedAt(response.data.updatedAt)
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
                form = it.form.copy(
                    title = it.form.title.updateValue(title)
                )
            )
        }
    }

    fun updateTeacherName(teacherName: String) {
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    teacherName = it.form.teacherName.updateValue(teacherName)
                )
            )
        }
    }

    fun updateColorSelect(color: Color) {
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    colorSelect = color
                )
            )
        }
    }

    private fun updateCreatedAt(time: Long){
        _uiState.update {
            it.copy(
                form = it.form.copy(
                    createdAt = time
                )
            )
        }
    }

    private fun updateUpdatedAt(time: Long){
        _uiState.update {
            it.copy(
               form = it.form.copy(
                   updatedAt = time
               )
            )
        }
    }


    fun save() {

        _uiState.update {
            it.copy(
                form = it.form.copy(
                    title = it.form.title.validate()
                )
            )
        }

        if (_uiState.value.form.title.isValid) {

            val data = Discipline(
                color = _uiState.value.form.colorSelect,
                title = _uiState.value.form.title.value,
                teacherName = _uiState.value.form.teacherName.value,
                createdAt = _uiState.value.form.createdAt,
                updatedAt = _uiState.value.form.updatedAt
            )

            viewModelScope.launch {

                _uiState.update {
                    it.copy(isLoading = true)
                }

                if (disciplineId == null) {

                    repo.save(data).collect { response ->
                        _uiState.update {
                            it.copy(
                                isBackNavigation = true,
                                isLoading = false
                            )
                        }
                    }
                } else {

                    repo.update(
                        data.copy(
                            id = disciplineId.toInt(),
                            updatedAt = System.currentTimeMillis()
                        )
                    ).collect { response ->

                        _uiState.update {
                            it.copy(
                                isBackNavigation = true,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }

    }

}