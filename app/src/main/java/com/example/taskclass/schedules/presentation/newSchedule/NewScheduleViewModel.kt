package com.example.taskclass.schedules.presentation.newSchedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
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

    fun updateDayWeek(dayWeek: String) {

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
            it.copy(startTime = it.startTime.updateValue(startTime))
        }

    }

    fun updateEndTime(endTime: String) {

        _uiState.update {
            it.copy(endTime = it.endTime.updateValue(endTime))
        }

    }

    fun save() {


        val value = _uiState.value

        val data = Schedule(
            dayWeek = value.dayWeek.value,
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