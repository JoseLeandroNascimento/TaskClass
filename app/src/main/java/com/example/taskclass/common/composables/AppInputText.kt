package com.example.taskclass.common.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun AppInputText(
    modifier: Modifier = Modifier,
    value: String,
    isError: Boolean = false,
    supportingText: String? = null,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String? = null,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        isError = isError,
        supportingText = if (isError) {
            {
                Text(
                    text = supportingText ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            null
        },
        placeholder = placeholder?.let { placeholderText ->
            {
                Text(
                    placeholderText,
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                )
            }
        },
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium
            )
        },
        singleLine = singleLine
    )
}

@Preview(showBackground = true)
@Composable
private fun AppInputTextLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppInputText(
            value = "",
            onValueChange = {},
            label = "Teste"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppInputTextDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppInputText(
            value = "",
            onValueChange = {},
            label = "Teste"
        )
    }
}