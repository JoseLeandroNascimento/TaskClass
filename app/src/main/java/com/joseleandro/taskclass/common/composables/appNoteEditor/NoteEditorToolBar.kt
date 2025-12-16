package com.joseleandro.taskclass.common.composables.appNoteEditor

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatAlignLeft
import androidx.compose.material.icons.automirrored.filled.FormatAlignRight
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.FormatAlignJustify
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.common.composables.SelectColorOptionsDialog
import com.joseleandro.taskclass.ui.theme.ColorsSelect
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@Composable
fun NoteEditorToolBar(
    state: RichTextState,
    modifier: Modifier = Modifier,
    colorBackground: Color = MaterialTheme.colorScheme.background,
    onColorBackground: (Color) -> Unit,
    colorText: Color = Color.Unspecified,
    onColorText: (Color) -> Unit,
    onAddLink: () -> Unit,
    onEmojiPicker: () -> Unit
) {

    var expandedColorBackground by remember { mutableStateOf(false) }
    var expandedColorText by remember { mutableStateOf(false) }

    val colorsSelect = (ColorsSelect.GrayPalette + ColorsSelect.predefinedColors).toMutableList().also {
        it.add(0,MaterialTheme.colorScheme.background)
    }

    if (expandedColorBackground) {

        SelectColorOptionsDialog(
            currentColor = colorBackground,
            onColorSelected = {
                onColorBackground(it)
                expandedColorBackground = false
            },
            colorTransparent = MaterialTheme.colorScheme.background,
            predefinedColors = colorsSelect,
            onDismiss = { expandedColorBackground = false }
        )

    }

    if (expandedColorText) {

        SelectColorOptionsDialog(
            currentColor = colorText,
            predefinedColors =  colorsSelect,
            colorTransparent = MaterialTheme.colorScheme.background,
            onColorSelected = {
                onColorText(it)
                expandedColorText = false
            },
            onDismiss = { expandedColorText = false }
        )

    }

    val currentAlign: TextAlign = state.currentParagraphStyle.textAlign


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

            IconButtonActionStyle(
                imageVector = Icons.Default.EmojiEmotions,
                contentDescription = "Inserir emoji",
                onClick = onEmojiPicker
            )

            VerticalDivider()

            Row() {

                IconButtonActionStyle(
                    imageVector = Icons.Default.FormatBold,
                    selected = state.currentSpanStyle.fontWeight == FontWeight.Bold,
                    onClick = {
                        state.toggleSpanStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                )


                IconButtonActionStyle(
                    imageVector = Icons.Default.FormatItalic,
                    selected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                    onClick = {
                        state.toggleSpanStyle(
                            SpanStyle(
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                )

                IconButtonActionStyle(
                    imageVector = Icons.Default.FormatUnderlined,
                    selected = state.currentSpanStyle.textDecoration?.contains(TextDecoration.Underline)
                        ?: false,
                    onClick = {
                        state.toggleSpanStyle(
                            SpanStyle(
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    }
                )

                IconButtonActionStyle(
                    imageVector = Icons.Default.Link,
                    selected = state.isLink,
                    onClick = onAddLink
                )

            }

            VerticalDivider()

            SingleChoiceSegmentedButtonRow {
                SegmentedButton(
                    border = SegmentedButtonDefaults.borderStroke(
                        color = Color.Transparent, width = 0.dp
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = currentAlign == TextAlign.Start || currentAlign == TextAlign.Left,
                    label = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
                            contentDescription = "Alinhar à esquerda"
                        )
                    },
                    icon = {},
                    onClick = {
                        state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
                    },
                    shape = MaterialTheme.shapes.small,
                )
                SegmentedButton(
                    border = SegmentedButtonDefaults.borderStroke(
                        color = Color.Transparent, width = 0.dp
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = currentAlign == TextAlign.Justify,
                    label = {
                        Icon(
                            imageVector = Icons.Default.FormatAlignJustify,
                            contentDescription = "Justificar"
                        )
                    },
                    icon = {},
                    onClick = {
                        state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Justify))
                    },
                    shape = MaterialTheme.shapes.small,
                )
                SegmentedButton(
                    border = SegmentedButtonDefaults.borderStroke(
                        color = Color.Transparent, width = 0.dp
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = currentAlign == TextAlign.End || currentAlign == TextAlign.Right,
                    label = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatAlignRight,
                            contentDescription = "Alinhar à direita"
                        )
                    },
                    icon = {},
                    onClick = {
                        state.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Right))
                    },
                    shape = MaterialTheme.shapes.small,
                )
            }

            VerticalDivider()

            Row {

                IconButtonActionStyle(
                    imageVector = Icons.Default.FormatListNumbered,
                    selected = state.isOrderedList,
                    onClick = {
                        if (state.isOrderedList)
                            state.removeOrderedList()
                        else
                            state.addOrderedList()
                    }
                )

                IconButtonActionStyle(
                    imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                    selected = state.isUnorderedList,
                    onClick = {
                        if (state.isUnorderedList)
                            state.removeUnorderedList()
                        else
                            state.addUnorderedList()
                    }
                )

            }

            VerticalDivider()

            Row() {

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f)
                    ),
                    onClick = { expandedColorBackground = !expandedColorBackground }
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatColorFill,
                        contentDescription = null,
                        tint = colorBackground
                    )
                }

                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f)
                    ),
                    onClick = { expandedColorText = !expandedColorText }
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatColorText,
                        contentDescription = null,
                        tint = colorText
                    )
                }

            }

        }
    }
}

@Composable
fun IconButtonActionStyle(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String = "",
    selected: Boolean = false,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = if (selected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteEditorToolBarPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {

        NoteEditorToolBar(
            state = rememberRichTextState(),
            onEmojiPicker = {},
            onAddLink = {},
            colorBackground = MaterialTheme.colorScheme.surface,
            onColorBackground = {},

            colorText = MaterialTheme.colorScheme.onSurface,
            onColorText = {},


            )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteEditorToolBarDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {

        NoteEditorToolBar(
            state = rememberRichTextState(),
            onEmojiPicker = {},
            onAddLink = {},
            colorBackground = MaterialTheme.colorScheme.surface,
            onColorBackground = {},
            colorText = MaterialTheme.colorScheme.onSurface,
            onColorText = {},

            )
    }
}