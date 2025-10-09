package com.example.taskclass.events.presentation.eventCreateScreen

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppDropdown
import com.example.taskclass.common.composables.AppInputDate
import com.example.taskclass.common.composables.AppInputText
import com.example.taskclass.common.composables.AppInputTime
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun EventCreateScreen(
    viewModel: EventCreateViewModel,
    onBack: () -> Unit
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
    updateTypeEvent: ((TypeEvent) -> Unit)? = null,
    onSave: ()-> Unit,
    onBack: () -> Unit,
) {

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
                title = {
                    Text(
                        text = stringResource(R.string.tela_titulo_novo_evento),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
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
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.title.value,
                    onValueChange = {
                        updateTitle?.invoke(it)
                    }
                )

                AppDropdown(
                    label = stringResource(R.string.label_tipo_de_evento),
                    value = uiState.typeEventSelected.value?.name ?: "",
                ) { closeDropdown ->
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

                                response.data.forEachIndexed { index, typeEvent ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = typeEvent.name,
                                                style = MaterialTheme.typography.labelMedium,
                                                overflow = TextOverflow.Ellipsis,
                                                maxLines = 1
                                            )
                                        },
                                        onClick = {
                                            updateTypeEvent?.invoke(typeEvent)
                                            closeDropdown()
                                        }
                                    )
                                }
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
                        value = uiState.date.value,
                        onValueChange = {
                            updateDate?.invoke(it)
                        },
                        modifier = Modifier.weight(1f),
                    )

                    AppInputTime(
                        modifier = Modifier.weight(1f),
                        label = stringResource(R.string.label_hora),
                        value = uiState.time.value
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
                    value = uiState.description.value,
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