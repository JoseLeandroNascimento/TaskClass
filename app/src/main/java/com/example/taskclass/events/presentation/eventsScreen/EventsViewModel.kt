package com.example.taskclass.events.presentation.eventsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.events.domain.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EventsUiState(isLoading = true))
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()
    private var allEvents: List<EventEntity> = emptyList()

    init { loadEvents() }

    private fun loadEvents() = viewModelScope.launch {
        repository.getAllEvents()
            .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
            .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message ?: "Erro ao carregar eventos") } }
            .collect { list ->
                allEvents = list
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        events = list,
                        monthDays = generateMonthDays(Calendar.getInstance(), list),
                        selectedDate = Calendar.getInstance(),
                        error = null
                    )
                }
            }
    }

    fun filterByDate(calendar: Calendar) {
        val dateInt = calendar.toIntDate()
        _uiState.update {
            it.copy(
                selectedDate = calendar,
                events = allEvents.filter { e -> e.date.value == dateInt }
            )
        }
    }

    fun generateMonthDays(month: Calendar, events: List<EventEntity> = allEvents): List<DayWithEvents> {
        val tempCal = (month.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
        val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1
        val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
        var dayCounter = 1 - firstDayOfWeek
        val daysList = mutableListOf<DayWithEvents>()

        while (dayCounter <= daysInMonth) {
            for (i in 0..6) {
                if (dayCounter in 1..daysInMonth) {
                    val dayCal = (month.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, dayCounter) }
                    val dateInt = dayCal.toIntDate()
                    val hasEvent = events.any { it.date.value == dateInt }
                    daysList.add(DayWithEvents(dayCal, hasEvent))
                } else {
                    daysList.add(DayWithEvents(null))
                }
                dayCounter++
            }
        }
        return daysList
    }

    private fun Calendar.toIntDate(): Int {
        val year = get(Calendar.YEAR)
        val month = get(Calendar.MONTH) + 1
        val day = get(Calendar.DAY_OF_MONTH)
        return year * 10000 + month * 100 + day
    }
}