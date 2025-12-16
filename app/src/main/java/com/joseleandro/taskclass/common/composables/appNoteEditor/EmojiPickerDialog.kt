package com.joseleandro.taskclass.common.composables.appNoteEditor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.common.composables.AppDialog
import com.joseleandro.taskclass.ui.theme.TaskClassTheme

private val EMOJIS = listOf(
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


@Composable
fun EmojiPickerDialog(
    onEmojiSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {

    AppDialog(
        onDismissRequest = onDismissRequest,
        title = "Escolher emoji"
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(6),
                modifier = Modifier.height(220.dp)
            ) {
                items(EMOJIS.size, key = { it }) { index ->
                    TextButton(
                        onClick = { onEmojiSelected(EMOJIS[index]) }
                    ) {
                        Text(
                            text = EMOJIS[index],
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmojiPickerDialogPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {

        EmojiPickerDialog(
            onDismissRequest = {},
            onEmojiSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmojiPickerDialogDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {

        EmojiPickerDialog(
            onDismissRequest = {},
            onEmojiSelected = {}
        )
    }
}