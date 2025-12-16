package com.joseleandro.taskclass.ui.events.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.R
import com.joseleandro.taskclass.common.composables.AppButton
import com.joseleandro.taskclass.common.composables.AppDialog

@Composable
fun ConfirmDeleteDialog(
    onDismissRequest: () -> Unit,
    confirmDelete: () -> Unit
) {
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = "Excluir Evento",
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Tem certeza que deseja excluir este evento?",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(R.string.btn_cancelar),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                AppButton(
                    label = stringResource(R.string.btn_confirm),
                    onClick = confirmDelete
                )
            }
        }

    }
}