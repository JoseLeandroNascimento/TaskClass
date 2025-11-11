package com.example.taskclass.ui.notes.presentation.noteEditorScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppDialog
import com.example.taskclass.common.composables.AppInputText
import com.example.taskclass.common.composables.appNoteEditor.NoteEditor
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun NoteEditScreen(
    viewModel: NoteEditorViewModel,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NoteEditScreen(
        uiState = uiState,
        onBack = onBack,
        updateHtml = viewModel::updateHtml,
        updatePlain = viewModel::updatePlain,
        updateTitle = viewModel::updateTitle,
        onSave = viewModel::saveNote
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    uiState: NoteUiState,
    onBack: () -> Unit,
    updateHtml: ((String) -> Unit)? = null,
    updatePlain: ((String) -> Unit)? = null,
    updateTitle: ((String) -> Unit)? = null,
    onSave: (() -> Unit)? = null,
) {

    val stateEditor = rememberRichTextState()
    var showConfirmSaveDialog by remember { mutableStateOf(false) }

    if(uiState.isBackNavigation){
        onBack()
    }

    if(showConfirmSaveDialog) {

        AppDialog(
            title = "Salvar alterações",
            onDismissRequest = {
                showConfirmSaveDialog = false
            }
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                AppInputText(
                    label = "Título",
                    value = uiState.title,
                    onValueChange = {
                        updateTitle?.invoke(it)
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = {
                            showConfirmSaveDialog = false
                        }
                    ) {
                        Text(text = "Cancelar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    AppButton(
                        label = "Salvar"
                    ) {

                        updateHtml?.invoke(stateEditor.toHtml())
                        updatePlain?.invoke(stateEditor.toText())
                        onSave?.invoke()
                        showConfirmSaveDialog = false
                    }
                }
            }

        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Anotação",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = White
                        ),
                        onClick = {
                            showConfirmSaveDialog = !showConfirmSaveDialog
                        }
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 2.dp),
                            imageVector = Icons.Default.Save,
                            contentDescription = null
                        )
                        Text(
                            text = "Salvar",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                    actionIconContentColor = White,
                )
            )
        }


    ) { innerPadding ->

        NoteEditor(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            stateRichText = stateEditor
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun NoteEditPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NoteEditScreen(
            uiState = NoteUiState(),
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteEditDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        NoteEditScreen(
            uiState = NoteUiState(),
            onBack = {}
        )
    }
}