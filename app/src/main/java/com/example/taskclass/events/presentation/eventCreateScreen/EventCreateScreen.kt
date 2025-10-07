package com.example.taskclass.events.presentation.eventCreateScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppInputDate
import com.example.taskclass.common.composables.AppInputTime
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun EventCreateScreen(
    viewModel: EventCreateViewModel,
    onBack: ()-> Unit
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
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreateScreen(
    uiState: EventCreateUiState,
    updateTitle:((String)-> Unit)? = null,
    updateDate:((String)-> Unit)? = null,
    updateTime:((String)-> Unit)? = null,
    updateDescription:((String)-> Unit)? = null,
    onBack: ()-> Unit,
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
                    Text(text = "Novo evento", style = MaterialTheme.typography.titleMedium)
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
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    label = {
                        Text(text = "Título", style = MaterialTheme.typography.labelMedium)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    value = uiState.title.value,
                    onValueChange = {
                        updateTitle?.invoke(it)
                    }
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    AppInputDate(
                        label = "Data",
                        value = uiState.date.value,
                        onValueChange = {
                            updateDate?.invoke(it)
                        },
                        modifier = Modifier.weight(1f),
                    )

                    AppInputTime(
                        modifier = Modifier.weight(1f),
                        label = "Hora",
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
                    label = "Cadastrar evento",
                    onClick = { /* salvar */ }
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
            onBack = {}
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
            onBack = {}
        )
    }

}