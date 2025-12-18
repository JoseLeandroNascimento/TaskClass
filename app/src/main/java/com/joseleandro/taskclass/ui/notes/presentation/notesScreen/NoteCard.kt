package com.joseleandro.taskclass.ui.notes.presentation.notesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.common.composables.AppCardNote
import com.joseleandro.taskclass.common.utils.formatRelativeOrDate
import com.joseleandro.taskclass.core.data.model.entity.NoteEntity
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText

@Composable
fun NoteCard(
    note: NoteEntity,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
    selected: Boolean = false,
    showSelected: Boolean = false,
    modifier: Modifier = Modifier
) {

    val state = rememberRichTextState()

    LaunchedEffect(note.id, note.updatedAt, note.html) {
        state.setHtml(note.html)
    }

    val collapsedHeight = 132.dp

    AppCardNote(
        modifier = modifier,
        onClick = onClick,
        onLongPress = onLongPress,
        header = {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showSelected) {
                    RadioButton(
                        modifier = Modifier.size(20.dp),
                        selected = selected,
                        onClick = onLongPress
                    )

                }
                Text(
                    modifier = Modifier.weight(1f),
                    text = note.title.ifBlank { "Sem título" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        footer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ProvideTextStyle(MaterialTheme.typography.labelSmall) {
                    Text(
                        text = note.updatedAt.formatRelativeOrDate(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,

                        )
                }
            }
        }
    ) {

        if (note.html.isNotBlank()) {

            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp)
                    .fillMaxWidth()
            ) {
                RichText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = collapsedHeight),
                    state = state,
                    style = MaterialTheme.typography.bodySmall,

                    )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                )
            }
        } else {
            Text(
                text = note.plain.ifBlank { "—" },
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun NoteCardPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {

        NoteCard(
            onClick = {},
            onLongPress = {},
            showSelected = true,
            note = NoteEntity(
                id = 0,
                title = "Título da nota",
                plain = "Texto da nota",
                html = "<h4>Conteúdo da nota</h4>",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NoteCardDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {

        NoteCard(
            onClick = {},
            onLongPress = {},
            note = NoteEntity(
                id = 0,
                title = "Título da nota",
                plain = "Texto da nota",
                html = "<h4>Conteúdo da nota</h4>",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            ),
        )
    }
}