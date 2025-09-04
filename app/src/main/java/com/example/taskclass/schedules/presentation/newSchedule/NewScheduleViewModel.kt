package com.example.taskclass.schedules.presentation.newSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.validators.TimeValidator
import com.example.taskclass.core.data.converters.TimeConverters
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.core.data.model.Schedule
import com.example.taskclass.discipline.domain.DisciplineRepository
import com.example.taskclass.schedules.domain.ScheduleRepository
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
    private val scheduleRepo: ScheduleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewScheduleUiState())
    val uiState: StateFlow<NewScheduleUiState> = _uiState.asStateFlow()

    init {

        viewModelScope.launch {
            repo.findAll().collect { response ->
                _uiState.update {
                    it.copy(disciplines = response)
                }
            }
        }
    }

    fun updateDayWeek(dayWeek: Int) {

        _uiState.update {
            it.copy(dayWeek = it.dayWeek.updateValue(dayWeek))
        }

    }

    fun updateDiscipline(discipline: Discipline) {

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

    fun closeModalErrorResponse(){
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

        if (!isValid()) {
            return
        }

        val value = _uiState.value

        val data = Schedule(
            dayWeek = value.dayWeek.value!!,
            startTime = TimeConverters().fromTimeString(value.startTime.value)!!,
            endTime = TimeConverters().fromTimeString(value.endTime.value)!!,
            disciplineId = value.discipline.value?.id!!
        )

        viewModelScope.launch {
            scheduleRepo.save(data).collect { response ->
                _uiState.update {
                    it.copy(scheduleResponse = response)
                }
            }
        }
    }

}