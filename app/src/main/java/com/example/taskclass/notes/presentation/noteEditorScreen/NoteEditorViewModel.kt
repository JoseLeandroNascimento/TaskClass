package com.example.taskclass.notes.presentation.noteEditorScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskclass.common.data.Resource
import com.example.taskclass.notes.domain.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NoteEditorViewModel @Inject constructor(
    private val repo: NoteRepository
) : ViewModel(){

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState = _uiState.asStateFlow()

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

    fun updatePlain(plain: String){
        _uiState.update {
            it.copy(plain = plain)
        }
    }


    fun saveNote() {

        with(_uiState.value){

            viewModelScope.launch {
                if (id == null) {
                    repo.saveNew(title, html, plain).collect { response ->
                        when(response){

                            is Resource.Loading ->{
                                _uiState.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is Resource.Success ->{
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isBackNavigation = true
                                    )
                                }
                            }

                            is Resource.Error ->{
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
                        when(response){

                            is Resource.Loading ->{
                                _uiState.update {
                                    it.copy(isLoading = true)
                                }
                            }

                            is Resource.Success ->{
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isBackNavigation = true
                                    )
                                }
                            }

                            is Resource.Error ->{
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