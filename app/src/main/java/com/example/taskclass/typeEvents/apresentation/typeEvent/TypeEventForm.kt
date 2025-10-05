package com.example.taskclass.typeEvents.apresentation.typeEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppInputText
import com.example.taskclass.common.composables.AppSelectColor
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun TypeEventForm(
    modifier: Modifier = Modifier,
    formState: TypeEventFormState,
    updateNameTypeEvent: ((String) -> Unit)? = null,
    updateColorTypeEvent: ((Color) -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    onSave: (() -> Unit)? = null
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppInputText(
            value = formState.nameTypeEvent.value,
            isError = formState.nameTypeEvent.error != null,
            supportingText = formState.nameTypeEvent.error,
            onValueChange = {
                updateNameTypeEvent?.invoke(it)
            },
            label = stringResource(R.string.label_nome_do_evento)
        )

        AppSelectColor(
            value = formState.colorTypeEvent.value,
            label = stringResource(R.string.label_cor_select),
            onValueChange = { color ->
                updateColorTypeEvent?.invoke(color)
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    onCancel?.invoke()
                }
            ) {
                Text(
                    text = stringResource(R.string.btn_cancelar),
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AppButton(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.btn_salvar)
            ) {
                onSave?.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TypeEventFormLightPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        TypeEventForm(
            formState = TypeEventFormState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TypeEventFormDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        TypeEventForm(formState = TypeEventFormState())
    }
}