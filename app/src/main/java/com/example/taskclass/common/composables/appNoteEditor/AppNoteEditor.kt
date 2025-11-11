package com.example.taskclass.common.composables.appNoteEditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme
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


    stateRichText.config.linkColor = MaterialTheme.colorScheme.primary
    stateRichText.config.linkTextDecoration = TextDecoration.None

    var showDialogAddLink by remember { mutableStateOf(false) }

    if (showDialogAddLink) {

        DialogAddLinkNoteEditorToolBar(
            state = stateRichText,
            onDismissRequest = {
                showDialogAddLink = false
            }
        )
    }

    var showEmojiPicker by remember { mutableStateOf(false) }

    if (showEmojiPicker) {
        EmojiPickerDialog(
            onEmojiSelected = { emoji ->
                // Adiciona o emoji no ponto atual do cursor
                stateRichText.addTextAfterSelection(emoji)
                showEmojiPicker = false
            },
            onDismissRequest = { showEmojiPicker = false }
        )
    }

    val currentAlign: TextAlign = stateRichText.currentParagraphStyle.textAlign
    val isOrdered = stateRichText.isOrderedList
    val isUnordered = stateRichText.isUnorderedList

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        NoteEditorToolBar(
            isBold = stateRichText.currentSpanStyle.fontWeight == FontWeight.Bold,
            onBoldClick = {
                stateRichText.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            },
            isItalic = stateRichText.currentSpanStyle.fontStyle == FontStyle.Italic,
            onItalicClick = {
                stateRichText.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            },
            isUnderline = stateRichText.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline)
                ?: false,
            onUnderlineClick = {
                stateRichText.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            },
            colorBackground = stateRichText.currentSpanStyle.background,
            onColorBackground = { color ->
                stateRichText.toggleSpanStyle(SpanStyle(background = color))
            },
            colorText = stateRichText.currentSpanStyle.color,
            onColorText = { color ->
                stateRichText.toggleSpanStyle(SpanStyle(color = color))
            },
            isLink = stateRichText.isLink,
            onAddLink = {
                showDialogAddLink = !showDialogAddLink
            },
            textAlign = currentAlign,
            onAlignLeft = {
                stateRichText.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
            },
            onAlignJustify = {
                stateRichText.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Justify))
            },
            onAlignRight = {
                stateRichText.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
            },
            isOrderedList = isOrdered,
            isUnorderedList = isUnordered,
            onToggleOrderedList = { stateRichText.toggleOrderedList() },
            onToggleUnorderedList = { stateRichText.toggleUnorderedList() },
            onEmojiPicker = {

                showEmojiPicker = !showEmojiPicker

            }
        )
        RichTextEditor(
            modifier = Modifier.fillMaxSize(),
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
            )
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