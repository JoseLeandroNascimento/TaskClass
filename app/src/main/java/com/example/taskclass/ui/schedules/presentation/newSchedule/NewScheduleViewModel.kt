package com.example.taskclass.ui.schedules.presentation.newSchedule

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.common.utils.toFormattedTime
import com.example.taskclass.common.utils.toLocalTime
import com.example.taskclass.common.validators.TimeValidator
import com.example.taskclass.core.data.model.entity.DisciplineEntity
import com.example.taskclass.core.data.model.entity.ScheduleEntity
import com.example.taskclass.ui.discipline.domain.DisciplineRepository
import com.example.taskclass.ui.schedules.domain.ScheduleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NewScheduleViewModel @Inject constructor(
    private val repo: DisciplineRepository,
    private val scheduleRepo: ScheduleRepository,
    private val saveStateHandler: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewScheduleUiState())
    val uiState: StateFlow<NewScheduleUiState> = _uiState.asStateFlow()
    val idSchedule: String? = saveStateHandler["scheduleId"]

    init {

        Log.d(LOG_TAG, "Carregando disciplinas")
        viewModelScope.launch {
            repo.findAll().collect { responseDisciplines ->
                _uiState.update {
                    it.copy(disciplines = responseDisciplines)
                }

                if (responseDisciplines is Resource.Success) {
                    idSchedule?.let { idSchedule ->
                        scheduleRepo.findById(idSchedule.toInt()).collect { scheduleResponse ->

                            if (scheduleResponse is Resource.Success) {
                                scheduleResponse.data.apply {
                                    updateDayWeek(dayWeek)
                                    updateDiscipline(responseDisciplines.data.find { item -> item.id == disciplineId }!!)
                                    updateStartTime(startTime.toFormattedTime())
                                    updateEndTime(endTime.toFormattedTime())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun updateDayWeek(dayWeek: Int) {

        _uiState.update {
            it.copy(dayWeek = it.dayWeek.updateValue(dayWeek))
        }

    }

    fun updateDiscipline(discipline: DisciplineEntity) {

        _uiState.update {
            it.copy(discipline = it.discipline.updateValue(discipline))
        }

    }

    fun updateStartTime(startTime: String) {

        _uiState.update {
            it.copy(
                startTime = it.startTime.updateValue(startTime),
                endTime = it.endTime.copy(
                    validators = listOf(
                        TimeValidator(
                            timeMin = startTime,
                            timeRangeError = "Hora anterior à $startTime"
                        )
                    )
                ).updateValue(startTime)
            )
        }

    }

    fun updateEndTime(endTime: String) {

        _uiState.update {
            it.copy(endTime = it.endTime.updateValue(endTime))
        }

    }

    fun closeModalErrorResponse() {
        _uiState.update {
            it.copy(scheduleResponse = null)
        }
    }

    fun isValid(): Boolean {
        _uiState.update {
            it.copy(
                dayWeek = it.dayWeek.validate(),
                endTime = it.endTime.validate(),
                startTime = it.startTime.validate(),
                discipline = it.discipline.validate()
            )
        }

        val formData = _uiState.value

        return formData.endTime.isValid && formData.discipline.isValid && formData.dayWeek.isValid && formData.startTime.isValid
    }

    fun save() {

        Log.d(LOG_TAG, "Validando dados do horário")
        if (!isValid()) {
            return
        }

        val value = _uiState.value

        val data = ScheduleEntity(
            dayWeek = value.dayWeek.value!!,
            startTime = value.startTime.value!!.toLocalTime(),
            endTime = value.endTime.value!!.toLocalTime(),
            disciplineId = value.discipline.value?.id!!
        )

        Log.d(LOG_TAG, "Dados do horário: $data")

        viewModelScope.launch {

            if (idSchedule == null) {
                scheduleRepo.save(data).collect { response ->

                    Log.d(LOG_TAG, "Retorno: $response")

                    _uiState.update {
                        it.copy(scheduleResponse = response)
                    }
                }
            } else {

                scheduleRepo.update(data.copy(id = idSchedule.toInt())).collect { response ->

                    Log.d(LOG_TAG, "Retorno: $response")

                    _uiState.update {
                        it.copy(scheduleResponse = response)
                    }
                }
            }
        }
    }

    companion object {

        const val LOG_TAG = "NEW_SCHEDULE_VIEWMODEL"

    }

}