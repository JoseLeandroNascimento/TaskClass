package com.example.taskclass.notes.presentation.noteEditorScreen

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
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

@Composable
fun EmojiPickerDialog(
    onEmojiSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val emojis = listOf(
        // 😄 Emoções e pessoas
        "😀", "😃", "😄", "😁", "😆", "🥹", "😅", "😂", "🤣", "🥲", "☺️", "😊", "😇", "🙂", "🙃", "😉", "😌",
        "😍", "🥰", "😘", "😗", "😙", "😚", "😋", "😛", "😝", "😜", "🤪", "🤨", "🧐", "🤓", "😎", "🥸",
        "🤩", "🥳", "😏", "😒", "😞", "😔", "😟", "😕", "🙁", "☹️", "😣", "😖", "😫", "😩", "🥺", "😢", "😭",
        "😤", "😠", "😡", "🤬", "🤯", "😳", "🥵", "🥶", "😱", "😨", "😰", "😥", "😓", "🤗", "🤔", "🫣", "🤭",
        "🫢", "🫡", "🤫", "🤥", "😶", "😐", "😑", "🫠", "😬", "🙄", "😯", "😦", "😧", "😮", "😲", "😴", "🤤",
        "😪", "😵", "🫥", "🤐", "🥴", "🤢", "🤮", "🤧", "😷", "🤒", "🤕", "🤑", "🤠", "😈", "👿", "👹",
        "👺", "💀", "☠️", "👻", "👽", "👾", "🤖",

        // 🐶 Animais e natureza
        "🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼", "🐻‍❄️", "🐨", "🐯", "🦁", "🐮", "🐷", "🐸", "🐵",
        "🙈", "🙉", "🙊", "🐒", "🐔", "🐧", "🐦", "🐤", "🐣", "🦆", "🦅", "🦉", "🦇", "🐺", "🐗", "🐴", "🦄",
        "🐝", "🐛", "🦋", "🐌", "🐞", "🐜", "🕷️", "🦂", "🐢", "🐍", "🦎", "🐙", "🦑", "🦞", "🦀", "🐡", "🐠",
        "🐟", "🐬", "🐳", "🐋", "🦈", "🐊", "🐅", "🐆", "🦓", "🦍", "🦧", "🐘", "🦛", "🦏", "🐪", "🐫", "🦒",
        "🦘", "🐃", "🐂", "🐄", "🐎", "🐖", "🐏", "🐑", "🦙", "🐐", "🦌", "🐕", "🐩", "🐈", "🐓", "🦃", "🦢",

        // 🍔 Comidas e bebidas
        "🍏", "🍎", "🍐", "🍊", "🍋", "🍌", "🍉", "🍇", "🍓", "🫐", "🍈", "🍒", "🍑", "🥭", "🍍", "🥥", "🥝",
        "🍅", "🍆", "🥑", "🥦", "🥬", "🥒", "🌶️", "🫑", "🌽", "🥕", "🧄", "🧅", "🥔", "🍠", "🥐", "🍞", "🥖",
        "🥨", "🧀", "🥚", "🍳", "🥞", "🧇", "🥓", "🥩", "🍗", "🍖", "🌭", "🍔", "🍟", "🍕", "🫓", "🥪", "🥙",
        "🧆", "🌮", "🌯", "🫔", "🥗", "🥘", "🫕", "🍝", "🍜", "🍲", "🍛", "🍣", "🍱", "🥟", "🦪", "🍤", "🍙",
        "🍚", "🍘", "🍥", "🥠", "🍢", "🍡", "🍧", "🍨", "🍦", "🥧", "🧁", "🍰", "🎂", "🍮", "🍭", "🍬", "🍫",
        "🍿", "🧋", "☕", "🫖", "🍵", "🍺", "🍻", "🥂", "🍷", "🥃", "🍸", "🍹", "🧉",

        // 🚗 Objetos e atividades
        "⌚", "📱", "💻", "🖥️", "🖨️", "🕹️", "🎮", "🎧", "🎤", "📷", "📹", "🎬", "💡", "🔦", "🏮", "📔", "📒",
        "📚", "📖", "✏️", "🖊️", "🖋️", "🖌️", "🖍️", "📎", "📐", "📏", "📅", "📆", "🗓️", "📊", "📈", "📉",
        "💰", "💸", "💳", "💎", "🧾", "💼", "📦", "📫", "📮", "🧳", "🚪", "🪑", "🛏️", "🛋️", "🚿", "🛁",
        "🪞", "🪟", "🕰️", "🪠", "🚽", "🧻", "🧹", "🧺", "🧼", "🧽", "🪣", "🧯", "🩹", "💉", "💊", "🩺",
        "🔒", "🔓", "🔑", "🗝️", "🔨", "🪓", "🔧", "🔩", "⚙️", "🪤", "🧲", "🔫", "💣", "🧨", "🪄", "🪅",

        // 🌍 Símbolos e diversos
        "❤️", "🧡", "💛", "💚", "💙", "💜", "🖤", "🤍", "🤎", "💔", "❣️", "💕", "💞", "💓", "💗", "💖",
        "💘", "💝", "💟", "☮️", "✝️", "☪️", "🕉️", "☯️", "☸️", "✡️", "🔯", "🕎", "☦️", "🛐", "⛎",
        "♈", "♉", "♊", "♋", "♌", "♍", "♎", "♏", "♐", "♑", "♒", "♓",
        "🆗", "🆕", "🆒", "🆓", "🆙", "🔝", "🔛", "🔜", "✔️", "☑️", "🔘", "🔴", "🟢", "🟡", "🟣", "⚪", "⚫"
    )


    AppDialog(
        onDismissRequest = onDismissRequest,
        title = "Escolher emoji"
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid (
                columns = GridCells.Fixed(6),
                modifier = Modifier.height(220.dp)
            ) {
                items(emojis.size, key = {it}) { index ->
                    TextButton(
                        onClick = { onEmojiSelected(emojis[index]) }
                    ) {
                        Text(
                            text = emojis[index],
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorToolBarSelect(
    modifier: Modifier = Modifier,
    value: String,
    content: @Composable (() -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.width(80.dp)
    ) {

        BasicTextField(
            value = value,
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
                    text = value
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
            content {
                expanded = false
            }
        }
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