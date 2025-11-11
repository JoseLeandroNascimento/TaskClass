package com.example.taskclass.common.composables.appNoteEditor

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.common.composables.SelectColorOptionsDialog
import com.example.taskclass.ui.theme.TaskClassTheme

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
    onAddLink: () -> Unit,
    textAlign: TextAlign,
    onAlignLeft: () -> Unit,
    onAlignJustify: () -> Unit,
    onAlignRight: () -> Unit,
    isOrderedList: Boolean,
    isUnorderedList: Boolean,
    onToggleOrderedList: () -> Unit,
    onToggleUnorderedList: () -> Unit,
    onEmojiPicker: () -> Unit
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

            IconButton(
                onClick = onEmojiPicker,
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEmotions,
                    contentDescription = "Inserir emoji",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }


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

            SingleChoiceSegmentedButtonRow {
                SegmentedButton(
                    border = SegmentedButtonDefaults.borderStroke(
                        color = Color.Transparent, width = 0.dp
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                        inactiveContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    selected = textAlign == TextAlign.Start || textAlign == TextAlign.Left,
                    label = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatAlignLeft,
                            contentDescription = "Alinhar à esquerda"
                        )
                    },
                    icon = {},
                    onClick = onAlignLeft,
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
                    selected = textAlign == TextAlign.Justify,
                    label = {
                        Icon(
                            imageVector = Icons.Default.FormatAlignJustify,
                            contentDescription = "Justificar"
                        )
                    },
                    icon = {},
                    onClick = onAlignJustify,
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
                    selected = textAlign == TextAlign.End || textAlign == TextAlign.Right,
                    label = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.FormatAlignRight,
                            contentDescription = "Alinhar à direita"
                        )
                    },
                    icon = {},
                    onClick = onAlignRight,
                    shape = MaterialTheme.shapes.small,
                )
            }

            VerticalDivider()

            Row {
                IconButton(onClick = onToggleOrderedList) {
                    Icon(
                        imageVector = Icons.Default.FormatListNumbered,
                        contentDescription = "Lista ordenada",
                        tint = if (isOrderedList) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }

                IconButton(onClick = onToggleUnorderedList) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.FormatListBulleted,
                        contentDescription = "Lista não ordenada",
                        tint = if (isUnorderedList) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
                }
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

            VerticalDivider()

        }
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
            onEmojiPicker = {},
            isLink = false,
            onAddLink = {},
            textAlign = TextAlign.Left,
            colorBackground = MaterialTheme.colorScheme.surface,
            onColorBackground = {},
            isUnderline = false,
            onUnderlineClick = {},
            isBold = false,
            onBoldClick = {},
            colorText = MaterialTheme.colorScheme.onSurface,
            onColorText = {},
            isItalic = false,
            onAlignJustify = {},
            isOrderedList = false,
            isUnorderedList = false,
            onAlignLeft = {},
            onAlignRight = {},
            onItalicClick = {},
            onToggleOrderedList = {},
            onToggleUnorderedList = {}

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
            onEmojiPicker = {},
            isLink = false,
            onAddLink = {},
            textAlign = TextAlign.Left,
            colorBackground = MaterialTheme.colorScheme.surface,
            onColorBackground = {},
            isUnderline = false,
            onUnderlineClick = {},
            isBold = false,
            onBoldClick = {},
            colorText = MaterialTheme.colorScheme.onSurface,
            onColorText = {},
            isItalic = false,
            onAlignJustify = {},
            isOrderedList = false,
            isUnorderedList = false,
            onAlignLeft = {},
            onAlignRight = {},
            onItalicClick = {},
            onToggleOrderedList = {},
            onToggleUnorderedList = {}

        )
    }
}