package com.joseleandro.taskclass.ui.notes.presentation.noteEditorScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.ui.notes.domain.NoteRepository
import com.mohamedrejeb.richeditor.model.RichTextState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val repo: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val id: String? = savedStateHandle["idNote"]

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState = _uiState.asStateFlow()
    val stateEditor = mutableStateOf(RichTextState())

    init {
        loadById()
    }

    private fun loadById() {
        id?.let { id ->
            viewModelScope.launch {
                repo.load(id.toInt()).collect {
                    it?.let { response ->
                        updateTitle(response.title)
                        updateHtml(response.html)
                        updatePlain(response.plain)
                        _uiState.update {state->
                            state.copy(id = response.id)
                        }
                        stateEditor.value.setHtml(response.html)
                    }
                }
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.update {
            it.copy(title = title)
        }
    }

    fun updateHtml(html: String) {
        _uiState.update {
            it.copy(html = html)
        }
    }

    fun updatePlain(plain: String) {
        _uiState.update {
            it.copy(plain = plain)
        }
    }


    fun saveNote() {

        with(_uiState.value) {

            viewModelScope.launch {
                if (id == null) {
                    repo.saveNew(title, html, plain).collect { response ->
                        when (response) {

                            is Resource.Loading -> {
                                _uiState.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is Resource.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isBackNavigation = true
                                    )
                                }
                            }

                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        messageError = response.message
                                    )
                                }
                            }
                        }
                    }
                } else {
                    repo.update(id, title, html, plain).collect { response ->
                        when (response) {

                            is Resource.Loading -> {
                                _uiState.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is Resource.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isBackNavigation = true
                                    )
                                }
                            }

                            is Resource.Error -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        messageError = response.message
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    fun loadNote(id: Int) {
        viewModelScope.launch {
            repo.load(id).collect { response ->

                response?.let { noteResponse ->
                    _uiState.update {
                        it.copy(
                            html = noteResponse.html,
                            title = noteResponse.title,
                            id = noteResponse.id,
                            plain = noteResponse.plain
                        )
                    }
                }
            }


        }
    }
}