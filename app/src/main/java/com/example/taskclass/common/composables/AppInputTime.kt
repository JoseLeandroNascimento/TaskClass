package com.example.taskclass.common.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInputTime(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    trailingIcon: ImageVector = Icons.Default.AccessTime,
    onValueChange: (String) -> Unit
) {
    val currentTime = Calendar.getInstance()
    val regex = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")

    var showTimePicker by remember { mutableStateOf(false) }
    val timeState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    LaunchedEffect(value) {
        if (regex.matches(value)) {
            val (h, m) = value.split(":").map { it.toInt() }
            timeState.hour = h
            timeState.minute = m
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                showTimePicker = true
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            isError = error != null,
            supportingText = if (error != null) {
                {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                null
            },
            value = value,
            readOnly = true,
            onValueChange = {},
            trailingIcon = {
                Icon(trailingIcon, contentDescription = "Selecionar horário")
            },
            interactionSource = interactionSource
        )
        AnimatedVisibility(showTimePicker) {
            TimePickerDialog(
                title = "Selecionar horário",
                onDismiss = { showTimePicker = false },
                onConfirm = {
                    val formattedTime = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        timeState.hour,
                        timeState.minute
                    )
                    onValueChange(formattedTime)
                    showTimePicker = false
                },
                timeState = timeState
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    timeState: TimePickerState
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .clip(MaterialTheme.shapes.extraLarge)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                TimePicker(
                    state = timeState,
                    colors = TimePickerDefaults.colors(
                        selectorColor = MaterialTheme.colorScheme.primary,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primary,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.background,
                        timeSelectorUnselectedContentColor = MaterialTheme.colorScheme.primary,
                        clockDialSelectedContentColor = White,
                        timeSelectorSelectedContentColor = White,
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancelar") }
                    Spacer(modifier = Modifier.width(8.dp))
                    AppButton(
                        label = "Confirmar",
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppInputTimePreviewLight() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppInputTime(
            label = "Início",
            value = "09:00",
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppInputTimePreviewDark() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppInputTime(
            label = "Início",
            value = "09:00",
            onValueChange = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TimePickerDialogLightPreview() {

    val currentTime = Calendar.getInstance()
    val timeState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        TimePickerDialog(
            title = "Hora",
            onConfirm = {},
            onDismiss = {},
            timeState = timeState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TimePickerDialogDarkPreview() {
    val currentTime = Calendar.getInstance()
    val timeState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        TaskClassTheme(
            dynamicColor = false,
            darkTheme = true
        ) {
            TimePickerDialog(
                title = "Hora",
                onConfirm = {},
                onDismiss = {},
                timeState = timeState
            )
        }
    }
}

