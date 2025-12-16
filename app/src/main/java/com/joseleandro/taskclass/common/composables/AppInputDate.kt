package com.joseleandro.taskclass.common.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppInputDate(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    error: String? = null,
    onValueChange: (String) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    var textValue by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(value) {
        if (textValue.text != value) {
            textValue = TextFieldValue(
                text = value,
                selection = TextRange(value.length)
            )
        }
    }

    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    formatter.isLenient = false

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = runCatching {
            formatter.parse(value)?.time ?: System.currentTimeMillis()
        }.getOrDefault(System.currentTimeMillis())
    )

    fun formatInput(input: String): String {
        val digits = input.filter { it.isDigit() }.take(8)
        val sb = StringBuilder()

        for (i in digits.indices) {
            sb.append(digits[i])
            if (i == 1 || i == 3) sb.append('/')
        }
        return sb.toString()
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = { newTextFieldValue ->
            val oldValue = textValue
            val newValue = newTextFieldValue
            if (oldValue.text.length > newValue.text.length) {
                if (oldValue.text.endsWith('/') && newValue.text == oldValue.text.dropLast(1)) {
                    val textWithoutNumber = newValue.text.dropLast(1)
                    textValue = TextFieldValue(
                        text = textWithoutNumber,
                        selection = TextRange(textWithoutNumber.length)
                    )
                    onValueChange(textWithoutNumber)
                } else {
                    textValue = newValue
                    onValueChange(newValue.text)
                }
                return@OutlinedTextField
            }

            val formatted = formatInput(newValue.text)
            textValue = TextFieldValue(
                text = formatted,
                selection = TextRange(formatted.length)
            )
            onValueChange(formatted)
        },
        label = { Text(label) },
        placeholder = { Text("dd/mm/aaaa", style = MaterialTheme.typography.labelSmall) },
        singleLine = true,
        maxLines = 1,
        modifier = modifier,
        isError = error != null,
        supportingText = error?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = "Selecionar data",
                modifier = Modifier.clickable { showDialog = true }
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    )

    if (showDialog) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = formatter.format(Date(millis))
                        textValue = TextFieldValue(
                            text = selectedDate,
                            selection = TextRange(selectedDate.length)
                        )

                        onValueChange(selectedDate)
                    }
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppInputDateLightPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppInputDate(
            label = "Data",
            value = "",
            onValueChange = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppInputDateDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppInputDate(
            label = "Data",
            value = "",
            onValueChange = {}
        )
    }
}