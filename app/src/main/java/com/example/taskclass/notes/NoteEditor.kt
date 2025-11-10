package com.example.taskclass.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppDialog
import com.example.taskclass.common.composables.SelectColorOptionsDialog
import com.example.taskclass.ui.theme.TaskClassTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditor(
    modifier: Modifier = Modifier
) {

    val state = rememberRichTextState()

    var showDialogAddLink by remember { mutableStateOf(false) }

    if (showDialogAddLink) {

        DialogAddLinkNoteEditorToolBar(
            state = state,
            onDismissRequest = {
                showDialogAddLink = false
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        NoteEditorToolBar(
            isBold = state.currentSpanStyle.fontWeight == FontWeight.Bold,
            onBoldClick = {
                state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
            },
            isItalic = state.currentSpanStyle.fontStyle == FontStyle.Italic,
            onItalicClick = {
                state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
            },
            isUnderline = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline)
                ?: false,
            onUnderlineClick = {
                state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
            },
            colorBackground = state.currentSpanStyle.background,
            onColorBackground = { color ->
                state.toggleSpanStyle(SpanStyle(background = color))
            },
            colorText = state.currentSpanStyle.color,
            onColorText = { color ->
                state.toggleSpanStyle(SpanStyle(color = color))
            },
            isLink = state.isLink,
            onAddLink = {
                showDialogAddLink = !showDialogAddLink
            }

        )
        RichTextEditor(
            modifier = Modifier.fillMaxSize(),
            state = state,
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

@Composable
fun NoteEditorToolBar(
    modifier: Modifier = Modifier,
    isBold: Boolean = false,
    onBoldClick: () -> Unit,
    isItalic: Boolean = false,
    onItalicClick: () -> Unit,
    isUnderline: Boolean = false,
    onUnderlineClick: () -> Unit,
    colorBackground: Color = MaterialTheme.colorScheme.background,
    onColorBackground: (Color) -> Unit,
    colorText: Color = Color.Unspecified,
    onColorText: (Color) -> Unit,
    isLink: Boolean,
    onAddLink: () -> Unit
) {

    var expandedColorBackground by remember { mutableStateOf(false) }
    var expandedColorText by remember { mutableStateOf(false) }

    if (expandedColorBackground) {

        SelectColorOptionsDialog(
            currentColor = colorBackground,
            onColorSelected = {
                onColorBackground(it)
                expandedColorBackground = false
            },
            onDismiss = { expandedColorBackground = false }
        )

    }

    if (expandedColorText) {

        SelectColorOptionsDialog(
            currentColor = colorText,
            onColorSelected = {
                onColorText(it)
                expandedColorText = false
            },
            onDismiss = { expandedColorText = false }
        )

    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .horizontalScroll(state = rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Row() {

                IconButton(
                    onClick = onBoldClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatBold,
                        contentDescription = null,
                        tint = if (isBold) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onItalicClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatItalic,
                        contentDescription = null,
                        tint = if (isItalic) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onUnderlineClick,
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatUnderlined,
                        contentDescription = null,
                        tint = if (isUnderline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(
                    onClick = onAddLink,
                ) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = null,
                        tint = if (isLink) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            VerticalDivider()

            NoteEditorToolBarSelect()

            VerticalDivider()

            SingleChoiceSegmentedButtonRow() {
                SegmentedButton(
                    border = SegmentedButtonDefaults.borderStroke(
                        color = Color.Transparent,
                        width = 0.dp
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = true,
                    label = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
                            contentDescription = null
                        )
                    },
                    icon = {},
                    onClick = {},
                    shape = MaterialTheme.shapes.small,
                )
                SegmentedButton(
                    border = SegmentedButtonDefaults.borderStroke(
                        color = Color.Transparent,
                        width = 0.dp
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = false,
                    label = {
                        Icon(
                            imageVector = Icons.Default.FormatAlignJustify,
                            contentDescription = null
                        )
                    },

                    icon = {},
                    onClick = {},
                    shape = MaterialTheme.shapes.small,
                )
                SegmentedButton(
                    border = SegmentedButtonDefaults.borderStroke(
                        color = Color.Transparent,
                        width = 0.dp
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = false,
                    label = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatAlignRight,
                            contentDescription = null
                        )
                    },
                    icon = {},
                    onClick = {},
                    shape = MaterialTheme.shapes.small,
                )
            }

            VerticalDivider()

            Row() {

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatListNumbered,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = null
                    )
                }
            }

            VerticalDivider()

            Row() {

                IconButton(
                    onClick = { expandedColorBackground = !expandedColorBackground }
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatColorFill,
                        contentDescription = null,
                        tint = colorBackground
                    )
                }

                IconButton(
                    onClick = { expandedColorText = !expandedColorText }
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatColorText,
                        contentDescription = null,
                        tint = colorText
                    )
                }

            }

            VerticalDivider()

        }
    }


}

@Composable
fun DialogAddLinkNoteEditorToolBar(
    state: RichTextState,
    onDismissRequest: () -> Unit
) {

    var text by remember { mutableStateOf("") }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorToolBarSelect(
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("14") } // Exemplo de valor inicial
    val interactionSource = remember { MutableInteractionSource() }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.width(80.dp)
    ) {

        BasicTextField(
            value = selectedText,
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(
                    type = MenuAnchorType.PrimaryEditable
                )
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                ),
            readOnly = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            ),
            interactionSource = interactionSource,
            singleLine = true,
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surface)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = selectedText
                )
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        }

        ExposedDropdownMenu(
            shape = RoundedCornerShape(0.dp),
            containerColor = MaterialTheme.colorScheme.surface,
            expanded = expanded,
            matchTextFieldWidth = true,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("12") },
                onClick = {
                    selectedText = "12"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("14") },
                onClick = {
                    selectedText = "14"
                    expanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("16") },
                onClick = {
                    selectedText = "16"
                    expanded = false
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun NoteEditorPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NoteEditor()
    }

}

@Preview(showBackground = true)
@Composable
private fun NoteEditorDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        NoteEditor()
    }

}