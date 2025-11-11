package com.example.taskclass.notes.presentation.notesScreen

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.core.data.model.NoteEntity
import com.example.taskclass.ui.theme.TaskClassTheme
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichText
import java.util.Date
import java.util.Locale

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotesScreen(
        modifier = modifier,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    uiState: NotesUiState
) {

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            when {

                uiState.isLoading -> {

                    CircularProgressIndicator()
                }

                uiState.notes.isEmpty() -> {

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Storage,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = "Nenhuma anotação encontrada!",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                else -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(12.dp),
                        modifier = Modifier.align(alignment = Alignment.TopCenter)
                    ) {

                        items(items = uiState.notes, key = { it.id }) { note ->
                            NoteCard(
                                note = note
                            )
                        }
                    }
                }
            }


        }

    }
}

@Composable
private fun NoteCard(
    note: NoteEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = .2.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = note.title.ifBlank { "Sem título" },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingContent = {

            }
        )

        if (note.html.isNotBlank()) {
            val state = rememberRichTextState()

            LaunchedEffect(note.id, note.updatedAt, note.html) {
                state.setHtml(note.html)
            }

            val collapsedHeight = 132.dp

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

        Spacer(Modifier.height(8.dp))

        ProvideTextStyle(MaterialTheme.typography.labelSmall) {
            Text(
                text = formatRelativeOrDate(note.updatedAt),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .align(Alignment.End)
            )
        }
    }
}

private fun formatRelativeOrDate(epochMillis: Long): String {
    if (epochMillis <= 0L) return ""
    val cal = Calendar.getInstance()
    val todayY = cal.get(Calendar.YEAR)
    val todayD = cal.get(Calendar.DAY_OF_YEAR)

    cal.timeInMillis = epochMillis
    val y = cal.get(Calendar.YEAR)
    val d = cal.get(Calendar.DAY_OF_YEAR)

    val timeFmt = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    return when {
        y == todayY && d == todayD -> "Hoje, ${timeFmt.format(Date(epochMillis))}"
        y == todayY && d == todayD - 1 -> "Ontem, ${timeFmt.format(Date(epochMillis))}"
        else -> dateFmt.format(Date(epochMillis))
    }
}


@Preview(showBackground = true)
@Composable
private fun NotesScreenLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NotesScreen(
            uiState = NotesUiState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NotesScreenDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        NotesScreen(
            uiState = NotesUiState()
        )
    }
}