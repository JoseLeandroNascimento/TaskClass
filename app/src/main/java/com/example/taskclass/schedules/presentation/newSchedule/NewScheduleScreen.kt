package com.example.taskclass.schedules.presentation.newSchedule

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppDialog
import com.example.taskclass.common.composables.AppDropdown
import com.example.taskclass.common.composables.AppInputTime
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun NewScheduleScreen(
    onBack: () -> Unit,
    viewModel: NewScheduleViewModel
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    NewScheduleScreen(
        onBack = onBack,
        uiState = uiState,
        updateDayWeek = {
            viewModel.updateDayWeek(it)
        },
        updateDiscipline = {
            viewModel.updateDiscipline(it)
        },
        updateEndTime = {
            viewModel.updateEndTime(it)
        },
        updateStartTime = {
            viewModel.updateStartTime(it)
        },
        onSave = {
            viewModel.save()
        },
        onCloseModalErrorResponse = {
            viewModel.closeModalErrorResponse()
        }
    )

}


@SuppressLint("LocalContextResourcesRead")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScheduleScreen(
    onBack: () -> Unit,
    uiState: NewScheduleUiState,
    updateDayWeek: ((Int) -> Unit)? = null,
    updateDiscipline: ((Discipline) -> Unit)? = null,
    updateStartTime: ((String) -> Unit)? = null,
    updateEndTime: ((String) -> Unit)? = null,
    onCloseModalErrorResponse: (() -> Unit)? = null,
    onSave: () -> Unit
) {

    val context = LocalContext.current
    val daysOfWeek = context.resources.getStringArray(R.array.dias_da_semana_completo)

    if (uiState.scheduleResponse is Resource.Success) {
        onBack()
    }

    uiState.scheduleResponse.let { response ->
        if (response is Resource.Error) {
            ErrorDialog(
                message = response.message,
                onDismiss = { onCloseModalErrorResponse?.invoke() }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Novo Horário",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppDropdown(
                    value = uiState.dayWeek.value?.let { daysOfWeek[it] } ?: "",
                    error = uiState.dayWeek.error,
                    label = "Dia da Semana *"
                ) { closeMenu ->
                    daysOfWeek.forEachIndexed { index, day ->
                        DropdownMenuItem(
                            text = { Text(day) },
                            onClick = {
                                updateDayWeek?.invoke(index)
                                closeMenu()
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }

                AppDropdown(
                    value = uiState.discipline.value?.title ?: "",
                    label = "Disciplina *",
                    error = uiState.discipline.error
                ) { closeMenu ->

                    when (uiState.disciplines) {
                        is Resource.Loading -> {

                        }

                        is Resource.Success -> {

                            uiState.disciplines.data.forEach { discipline ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            discipline.title,
                                            overflow = TextOverflow.Ellipsis,
                                            maxLines = 1
                                        )
                                    },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(26.dp)
                                                .background(discipline.color, CircleShape)
                                                .border(
                                                    1.dp,
                                                    Color.Black.copy(alpha = 0.08f),
                                                    CircleShape
                                                )
                                                .shadow(3.dp, CircleShape, clip = false)
                                        )
                                    },
                                    onClick = {
                                        updateDiscipline?.invoke(discipline)
                                        closeMenu()
                                    },
                                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                )
                            }

                        }

                        is Resource.Error -> {

                        }
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    AppInputTime(
                        modifier = Modifier.weight(1f),
                        value = uiState.startTime.value ?: "",
                        error = uiState.startTime.error,
                        trailingIcon = Icons.Default.Schedule,
                        onValueChange = {
                            updateStartTime?.invoke(it)
                        },
                        label = "Início"
                    )
                    AppInputTime(
                        modifier = Modifier.weight(1f),
                        trailingIcon = Icons.Default.Timer,
                        value = uiState.endTime.value ?: "",
                        error = uiState.endTime.error,
                        onValueChange = {
                            updateEndTime?.invoke(it)
                        },
                        label = "Fim"
                    )
                }

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isLoading = uiState.scheduleResponse is Resource.Loading,
                    label = "Cadastrar Horário",
                    onClick = onSave
                )
            }
        }
    }
}


@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = "Algo deu errado",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Fechar",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    )
}


@Preview()
@Composable
private fun NewScheduleLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NewScheduleScreen(
            onBack = {},
            onSave = {},
            uiState = NewScheduleUiState(
                scheduleResponse = Resource.Error(message = "Conflito de horário")
            ),
            onCloseModalErrorResponse = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewScheduleDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        NewScheduleScreen(
            onBack = {

            },
            onSave = {},
            uiState = NewScheduleUiState()
        )
    }
}