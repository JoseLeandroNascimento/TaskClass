package com.joseleandro.taskclass.ui.notes.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.R
import com.joseleandro.taskclass.common.composables.AppButton
import com.joseleandro.taskclass.common.composables.AppDialog
import com.joseleandro.taskclass.ui.theme.TaskClassTheme

@Composable
fun NoteConfirmDeleteDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {

    AppDialog(
        onDismissRequest = onDismissRequest,
        title = "Excluir anotação"
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Deseja realmente excluir esta anotação?",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(id = R.string.btn_cancelar),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                AppButton(
                    label = stringResource(id = R.string.btn_confirm),
                    onClick = onConfirm
                )
            }
        }

    }
}

@Preview
@Composable
private fun NoteConfirmDeleteDialogPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NoteConfirmDeleteDialog(
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}

@Preview
@Composable
private fun NoteConfirmDeleteDialogDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NoteConfirmDeleteDialog(
            onDismissRequest = {},
            onConfirm = {}
        )
    }
}

