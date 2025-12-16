package com.joseleandro.taskclass.ui.discipline.presentation.disciplineScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.DisciplineEntity
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.ui.discipline.domain.DisciplineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DisciplineViewModel @Inject constructor(
    private val repo: DisciplineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DisciplineUiState())
    val uiState: StateFlow<DisciplineUiState> = _uiState.asStateFlow()
    private val _filterSort = MutableStateFlow(Order(selector = DisciplineEntity::createdAt))
    val filterSort = _filterSort.asStateFlow()

    private val _filterQuery = MutableStateFlow("")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()

    init {
        loadData()
    }

    fun onAction(action: DisciplinesAction) {
        when (action) {

            is DisciplinesAction.OnDeleteDiscipline -> {
                deleteDiscipline(action.id)
            }

            is DisciplinesAction.UpdateQuery -> {
                updateQuery(action.query)
            }

            is DisciplinesAction.UpdateFilterSort -> {
                updateFilterSort(action.order)
            }
        }
    }

    private fun updateFilterSort(
        order: Order<DisciplineEntity>
    ) {
        _filterSort.value = order
    }

    private fun updateQuery(query: String) {
        _filterQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun loadData() {
        viewModelScope.launch {

            combine(
                _filterSort,
                _filterQuery.debounce(300)
            ) { order, query ->
                order to query
            }.flatMapLatest { (order, query) ->
                repo.findAll(
                    order = order,
                    title = query
                )
            }.collect { response ->

                when (response) {

                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                disciplines = response.data,
                                isLoading = false
                            )
                        }

                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteDiscipline(id: Int) {

        viewModelScope.launch {
            repo.delete(id)
        }
    }

}