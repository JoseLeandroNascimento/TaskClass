package com.example.taskclass.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.taskclass.R
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun AppConfirmDialog(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {}
) {

    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
                    textAlign = TextAlign.Justify
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onCancel
                    ) {
                        Text(text = stringResource(R.string.btn_cancelar))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    AppButton(
                        label = stringResource(R.string.btn_confirm),
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppConfirmDialogLightPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppConfirmDialog(
            onDismissRequest = {},
            title = "Título",
            description = LoremIpsum(30).values.joinToString(" ")
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppConfirmDialogDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppConfirmDialog(
            onDismissRequest = {},
            title = "Título",
            description = LoremIpsum(30).values.joinToString(" ")
        )
    }
}