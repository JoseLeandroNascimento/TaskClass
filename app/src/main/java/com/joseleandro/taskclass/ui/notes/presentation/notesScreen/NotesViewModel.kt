package com.joseleandro.taskclass.ui.notes.presentation.notesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.NoteEntity
import com.joseleandro.taskclass.ui.notes.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repo: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesUiState())
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()


    init {
        loadData()
    }

    fun onAction(action: NoteAction) {

        when (action) {

            is NoteAction.OnToggleNote -> {
                onToggleNote(action.note)
            }

            is NoteAction.OnDelete -> {
                delete(action.notes)
            }

            is NoteAction.OnSelectAll -> {
                onSelectAll()
            }
        }
    }

    private fun onToggleNote(note: NoteEntity) {

        val notesSelected = _uiState.value.notesSelected

        if (notesSelected.contains(note)) {
            _uiState.update {
                it.copy(
                    notesSelected = notesSelected - note
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                notesSelected = notesSelected + note
            )
        }

    }

    private fun onSelectAll(){

        with(_uiState.value){

            if(notesSelected.size == notes.size){
                _uiState.update {
                    it.copy(
                        notesSelected = emptySet()
                    )
                }
                return
            }
        }
        _uiState.update {
            it.copy(
                notesSelected = it.notes.toSet()
            )
        }

    }

    private fun delete(notes: List<NoteEntity>) {


        viewModelScope.launch {

            if (notes.isEmpty()) return@launch

            repo.deleteAll(notes).collect { response ->
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
                                notesSelected = emptySet()
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

    private fun loadData() {
        viewModelScope.launch {
            repo.findAll().collect { response ->

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
                                notes = response.data,
                                isLoading = false
                            )
                        }
                    }

                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                messageError = response.message,
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

}