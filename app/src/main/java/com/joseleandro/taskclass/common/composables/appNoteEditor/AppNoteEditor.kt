package com.joseleandro.taskclass.common.composables.appNoteEditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditor(
    modifier: Modifier = Modifier,
    stateRichText: RichTextState
) {


    var showDialogAddLink by remember { mutableStateOf(false) }
    var showEmojiPicker by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }


    if (showDialogAddLink) {

        DialogAddLinkNoteEditorToolBar(
            state = stateRichText,
            onDismissRequest = {
                showDialogAddLink = false
            }
        )
    }


    if (showEmojiPicker) {
        EmojiPickerDialog(
            onEmojiSelected = { emoji ->
                stateRichText.addTextAfterSelection(emoji)
                showEmojiPicker = false
            },
            onDismissRequest = { showEmojiPicker = false }
        )
    }

    LaunchedEffect(Unit) {

        stateRichText.toggleParagraphStyle(
            paragraphStyle = ParagraphStyle(
                textAlign = TextAlign.Start
            )
        )
    }


    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        NoteEditorToolBar(
            state = stateRichText,
            colorBackground = stateRichText.currentSpanStyle.background,
            onColorBackground = { color ->
                stateRichText.toggleSpanStyle(SpanStyle(background = color))
            },
            colorText = stateRichText.currentSpanStyle.color,
            onColorText = { color ->
                stateRichText.toggleSpanStyle(SpanStyle(color = color))
            },
            onAddLink = {
                showDialogAddLink = !showDialogAddLink
            },
            onEmojiPicker = {

                showEmojiPicker = !showEmojiPicker

            }
        )
        RichTextEditor(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester),
            state = stateRichText,
            shape = RoundedCornerShape(0.dp),
            placeholder = {
                Text(
                    text = "\u270D\uFE0F Escreva suas anotações aqui...",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            },
            colors = RichTextEditorDefaults.richTextEditorColors(
                containerColor = MaterialTheme.colorScheme.background,
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
            ),
        )
    }

}


@Preview(showBackground = true)
@Composable
private fun NoteEditorPreview() {

    val state = rememberRichTextState()
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NoteEditor(
            stateRichText = state
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun NoteEditorDarkPreview() {

    val state = rememberRichTextState()

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        NoteEditor(
            stateRichText = state
        )
    }

}