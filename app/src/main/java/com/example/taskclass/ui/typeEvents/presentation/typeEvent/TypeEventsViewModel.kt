package com.example.taskclass.ui.typeEvents.presentation.typeEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Order
import com.example.taskclass.core.data.model.entity.TypeEventEntity
import com.example.taskclass.ui.typeEvents.domain.TypeEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TypeEventsViewModel @Inject constructor(
    private val repo: TypeEventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TypeEventsUiState())
    val uiState: StateFlow<TypeEventsUiState> = _uiState.asStateFlow()

    private val _filterSort =
        MutableStateFlow(Order(selector = TypeEventEntity::createdAt))
    val filterSort: StateFlow<Order<TypeEventEntity>> = _filterSort.asStateFlow()

    private val _filterQuery = MutableStateFlow("")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()


    init {
        loadAll()
    }

    fun onAction(action: TypeEventAction) {
        when (action) {

            is TypeEventAction.OnDelete -> {
                delete(action.id)
            }

            is TypeEventAction.Search -> {
                search(action.query)
            }

            is TypeEventAction.ShowSearch -> {
                updateShowSearch(action.show)
            }

            is TypeEventAction.OrderBy -> {
                orderBy(action.order)
            }
        }

    }

    @OptIn(FlowPreview::class)
    fun loadAll() {
        viewModelScope.launch {

            combine(
                _filterSort,
                _filterQuery.debounce(300)
            ) { order, query ->
                order to query
            }.collectLatest { (order, query) ->

                repo.findAll(query = query, order = order).collectLatest { response ->
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
                                    isLoading = false,
                                    typeEvents = response.data
                                )
                            }
                        }

                        is Resource.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun updateShowSearch(show: Boolean) {
        _uiState.update {
            it.copy(showSearch = show)
        }
    }

    private fun search(query: String) {
        _filterQuery.value = query
    }

    private fun orderBy(order: Order<TypeEventEntity>) {
        _filterSort.value = order
    }

    private fun delete(id: Int) {
        viewModelScope.launch {
            repo.delete(id).collect { response ->

            }
        }
    }

}