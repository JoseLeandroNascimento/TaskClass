package com.joseleandro.taskclass.ui.events.presentation.eventCreateScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.taskclass.R
import com.joseleandro.taskclass.common.composables.AppButton
import com.joseleandro.taskclass.common.composables.AppDropdown
import com.joseleandro.taskclass.common.composables.AppInputDate
import com.joseleandro.taskclass.common.composables.AppInputText
import com.joseleandro.taskclass.common.composables.AppInputTime
import com.joseleandro.taskclass.common.composables.CircleIndicator
import com.joseleandro.taskclass.common.composables.HideKeyboardOnTap
import com.joseleandro.taskclass.common.data.Resource
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.joseleandro.taskclass.ui.theme.White

@Composable
fun EventCreateScreen(
    viewModel: EventCreateViewModel,
    onBack: () -> Unit,
    addTypeEvent: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value


    EventCreateScreen(
        uiState = uiState,
        onBack = onBack,
        updateTitle = {
            viewModel.updateTitle(it)
        },
        updateDate = {
            viewModel.updateDate(it)
        },
        updateTime = {
            viewModel.updateTime(it)
        },
        updateDescription = {
            viewModel.updateDescription(it)
        },
        updateTypeEvent = {
            viewModel.updateTypeEvent(it)
        },
        addTypeEvent = addTypeEvent,
        onSave = {
            viewModel.saveEvent()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreateScreen(
    uiState: EventCreateUiState,
    updateTitle: ((String) -> Unit)? = null,
    updateDate: ((String) -> Unit)? = null,
    updateTime: ((String) -> Unit)? = null,
    updateDescription: ((String) -> Unit)? = null,
    updateTypeEvent: ((TypeEventEntity) -> Unit)? = null,
    addTypeEvent: (() -> Unit)? = null,
    onSave: () -> Unit,
    onBack: () -> Unit,
) {

    val titleFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {

        titleFocus.requestFocus()
    }

    LaunchedEffect(uiState.savedSuccessAndClose) {
        if (uiState.savedSuccessAndClose) onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
                title = {
                    Text(
                        text = stringResource(R.string.tela_titulo_novo_evento),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center

        ) {

            HideKeyboardOnTap(
                modifier = Modifier.align(alignment = Alignment.TopCenter)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    AppInputText(
                        label = stringResource(R.string.label_titulo),
                        isError = uiState.formState.title.error != null,
                        supportingText = uiState.formState.title.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(titleFocus),
                        value = uiState.formState.title.value,
                        onValueChange = {
                            updateTitle?.invoke(it)
                        }
                    )

                    AppDropdown(
                        label = stringResource(R.string.label_tipo_de_evento),
                        error = uiState.formState.typeEventSelected.error,
                        value = uiState.formState.typeEventSelected.value?.name ?: "",
                        leadingIcon = uiState.formState.typeEventSelected.value?.let {
                            {
                                CircleIndicator(
                                    color = it.color,
                                    size = 20.dp
                                )
                            }
                        }
                    ) {
                        uiState.typeEvents?.let { response ->
                            when (response) {
                                is Resource.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxWidth(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                is Resource.Success -> {

                                    response.data.forEachIndexed { _, typeEvent ->

                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = typeEvent.name,
                                                    overflow = TextOverflow.Ellipsis,
                                                    maxLines = 1
                                                )
                                            },
                                            leadingIcon = {

                                                CircleIndicator(
                                                    size = 26.dp,
                                                    color = typeEvent.color
                                                )
                                            },
                                            onClick = {
                                                updateTypeEvent?.invoke(typeEvent)
                                                closeMenu()
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                        )
                                    }

                                    DropdownMenuItem(
                                        onClick = {
                                            closeMenu()
                                            addTypeEvent?.invoke()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null
                                            )
                                        },
                                        text = {
                                            Text(text = stringResource(R.string.novo_tipo_de_evento))
                                        }
                                    )
                                }

                                is Resource.Error -> {

                                }
                            }
                        }
                    }


                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        AppInputDate(
                            label = stringResource(R.string.label_data),
                            value = uiState.formState.date.value,
                            onValueChange = {
                                updateDate?.invoke(it)
                            },
                            error = uiState.formState.date.error,
                            modifier = Modifier.weight(1f),
                        )

                        AppInputTime(
                            modifier = Modifier.weight(1f),
                            label = stringResource(R.string.label_hora),
                            error = uiState.formState.time.error,
                            value = uiState.formState.time.value
                        ) {
                            updateTime?.invoke(it)
                        }
                    }

                    OutlinedTextField(
                        label = {
                            Text(text = "Descrição", style = MaterialTheme.typography.labelMedium)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 4,
                        maxLines = 4,
                        value = uiState.formState.description.value,
                        onValueChange = {
                            updateDescription?.invoke(it)
                        }
                    )

                    AppButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        label = stringResource(R.string.btn_cadastrar_evento),
                        onClick = onSave
                    )
                }
            }

            if(uiState.isLoading){
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventCreateScreenLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        EventCreateScreen(
            uiState = EventCreateUiState(),
            onBack = {},
            onSave = {}
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun EventCreateScreenDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        EventCreateScreen(
            uiState = EventCreateUiState(),
            onBack = {},
            onSave = {}
        )
    }

}