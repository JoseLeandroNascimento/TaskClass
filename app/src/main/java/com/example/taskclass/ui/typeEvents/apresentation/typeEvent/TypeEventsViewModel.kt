package com.example.taskclass.ui.typeEvents.apresentation.typeEvent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.ui.typeEvents.domain.TypeEventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class TypeEventsViewModel @Inject constructor(
    private val repo: TypeEventRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TypeEventsUiState())
    val uiState: StateFlow<TypeEventsUiState> = _uiState.asStateFlow()

    init {
        loadAll()
    }

    fun loadAll() {
        viewModelScope.launch {
            repo.findAll().collectLatest { response ->
               when(response){

                   is Resource.Loading ->{
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                   }

                   is Resource.Success ->{
                       _uiState.update {
                           it.copy(
                               isLoading = false,
                               typeEvents = response.data
                           )
                       }
                   }

                   is Resource.Error ->{

                   }
               }
            }
        }
    }


    fun delete(id: Int) {
        viewModelScope.launch {
            repo.delete(id).collect { response ->

            }
        }
    }

}