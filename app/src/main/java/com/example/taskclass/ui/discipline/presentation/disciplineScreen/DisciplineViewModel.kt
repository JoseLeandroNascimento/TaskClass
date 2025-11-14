package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.core.data.model.Order
import com.example.taskclass.ui.discipline.domain.DisciplineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

@HiltViewModel
class DisciplineViewModel @Inject constructor(
    private val repo: DisciplineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisciplineUiState())
    val uiState: StateFlow<DisciplineUiState> = _uiState.asStateFlow()
    private val _filterSort = MutableStateFlow(Order(selector = Discipline::createdAt))

    init {
        loadData()
    }

    fun updateFilterSort(orderBy: KProperty1<Discipline, Comparable<*>>,sortDirection: Boolean ) {

        _uiState.update {
            it.copy(
                orderBy = orderBy,
                sortDirection = sortDirection
            )
        }
        _filterSort.update {
            it.copy(selector = orderBy, ascending = sortDirection)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadData() {
        viewModelScope.launch {
            _filterSort.flatMapLatest {filter ->
                repo.findAll(
                    order = filter
                )
            }.collect { disciplines ->
                _uiState.update {
                    it.copy(disciplines = disciplines)
                }
            }
        }
    }

    fun deleteDiscipline(id: Int) {

        viewModelScope.launch {
            repo.delete(id)
        }
    }

}