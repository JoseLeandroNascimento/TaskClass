package com.joseleandro.taskclass.common.composables.appNoteEditor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.common.composables.AppButton
import com.joseleandro.taskclass.common.composables.AppDialog
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun DialogAddLinkNoteEditorToolBar(
    state: RichTextState,
    onDismissRequest: () -> Unit
) {

    val selection = state.selection
    val textSelection = state.toText().substring(selection.min, selection.max)

    var text by remember { mutableStateOf(textSelection) }
    var url by remember { mutableStateOf("") }

    AppDialog(
        onDismissRequest = onDismissRequest,
        title = "Inserir link"
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Texto (opcional)") })
            OutlinedTextField(
                value = url,
                onValueChange = { url = it },
                label = { Text("URL (https://...)") })

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextButton(onClick = onDismissRequest) {
                    Text(text = "Cancelar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                AppButton(
                    label = "Inserir",
                    onClick = {
                        if (url.isNotBlank()) {

                            /**
                             * Se houver algum texto selecionado, tranformar em link
                             */
                            if (state.selection.start != state.selection.end) {
                                state.addLinkToSelection(url = url)
                            } else {
                                state.addLink(text = text.ifBlank { url }, url = url)
                            }

                            onDismissRequest()
                        }
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun DialogAddLinkNoteEditorToolBarPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        DialogAddLinkNoteEditorToolBar(
            state = rememberRichTextState(),
            onDismissRequest = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogAddLinkNoteEditorToolBarDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        DialogAddLinkNoteEditorToolBar(
            state = rememberRichTextState(),
            onDismissRequest = {}
        )
    }
}