package com.joseleandro.taskclass.ui.notes.presentation.notesScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.taskclass.common.composables.AppButtonOrderBy
import com.joseleandro.taskclass.common.composables.OrderByOption
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.core.data.model.entity.NoteEntity
import com.joseleandro.taskclass.ui.notes.presentation.components.NoteConfirmDeleteDialog
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import kotlin.math.roundToInt

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    onEditNavigation: (Int) -> Unit,
    viewModel: NotesViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotesScreen(
        modifier = modifier,
        onEditNavigation = onEditNavigation,
        onAction = viewModel::onAction,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    onEditNavigation: (Int) -> Unit,
    onAction: (NoteAction) -> Unit,
    uiState: NotesUiState
) {


    var sortDirection by remember { mutableStateOf(true) }
    val density = LocalDensity.current
    val modeSelectedNoteActive by remember(uiState.notesSelected) {
        derivedStateOf {
            uiState.notesSelected.isNotEmpty()
        }
    }
    val gridState = rememberLazyStaggeredGridState()

    val topBarHeight = 56.dp
    val topBarHeightPx = with(LocalDensity.current) { topBarHeight.toPx() }
    val contentTopSpacing = 12.dp
    var topBarOffsetPx by remember { mutableFloatStateOf(0f) }

    val visibleTopBarHeightDp = with(density) {
        (topBarHeightPx - topBarOffsetPx).toDp() + contentTopSpacing
    }

    val selectedAll by remember(uiState.notesSelected) {
        derivedStateOf {
            uiState.notesSelected.size == uiState.notes.size
        }
    }

    var showDeleteAllConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.firstVisibleItemScrollOffset }
            .collect { scrollOffset ->
                topBarOffsetPx = scrollOffset
                    .toFloat()
                    .coerceIn(0f, topBarHeightPx)
            }
    }


    if (showDeleteAllConfirmDialog) {

        NoteConfirmDeleteDialog(
            onDismissRequest = {
                showDeleteAllConfirmDialog = false
            },
            onConfirm = {
                onAction(
                    NoteAction.OnDelete(
                        uiState.notesSelected.toList()
                    )
                )
                showDeleteAllConfirmDialog = false
            }
        )
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Box {

            NotesTopBar(
                modifier = Modifier
                    .zIndex(1f)
                    .height(topBarHeight)
                    .offset {
                        IntOffset(
                            x = 0,
                            y = -topBarOffsetPx.roundToInt()
                        )
                    }
                    .graphicsLayer {
                        alpha = 1f - (topBarOffsetPx / topBarHeightPx)
                    },
                modeSelection = modeSelectedNoteActive,
                sort = uiState.sort,
                elementsSelectedTotal = uiState.notesSelected.size,
                onDelete = {
                    showDeleteAllConfirmDialog = !showDeleteAllConfirmDialog
                },
                onSortChange = { /* ação */ },
                onToggleSortDirection = {
                    sortDirection = !sortDirection
                },
                selectedAll = selectedAll,
                onSelectedAll = {
                    onAction(NoteAction.OnSelectAll)
                }
            )

            when {
                uiState.isLoading -> NotesLoading()
                uiState.notes.isEmpty() -> NotesEmptyState(modifier = Modifier.padding(top = topBarHeight))
                else -> NotesGrid(
                    state = gridState,
                    topPadding = visibleTopBarHeightDp,
                    notes = uiState.notes,
                    selectedNotes = uiState.notesSelected,
                    selectionMode = modeSelectedNoteActive,
                    onClick = onEditNavigation,
                    onLongPress = { onAction(NoteAction.OnToggleNote(it)) }
                )
            }

        }
    }

}

@Composable
private fun NotesGrid(
    state: LazyStaggeredGridState,
    notes: List<NoteEntity>,
    topPadding: Dp,
    selectedNotes: Set<NoteEntity>,
    selectionMode: Boolean,
    onClick: (Int) -> Unit,
    onLongPress: (NoteEntity) -> Unit
) {
    LazyVerticalStaggeredGrid(
        state = state,
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(
            top = topPadding,
            start = 12.dp,
            end = 12.dp,
            bottom = 96.dp
        ),
        modifier = Modifier.fillMaxSize()
    ) {

        items(notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                selected = selectedNotes.contains(note),
                showSelected = selectionMode,
                onClick = { onClick(note.id) },
                onLongPress = { onLongPress(note) }
            )
        }

        item(span = StaggeredGridItemSpan.FullLine) {
            Spacer(Modifier.height(96.dp))
        }
    }
}


@Composable
private fun NotesEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .zIndex(0f)
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Storage,
            contentDescription = null,
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "Nenhuma anotação ainda",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "Crie sua primeira anotação para começar",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}


@Composable
private fun NotesLoading() {
    Box(
        modifier = Modifier
            .zIndex(0f)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
private fun NotesTopBar(
    modifier: Modifier = Modifier,
    modeSelection: Boolean,
    sort: Order<NoteEntity>,
    elementsSelectedTotal: Int = 0,
    selectedAll: Boolean = false,
    onSelectedAll: () -> Unit,
    onDelete: () -> Unit,
    onSortChange: (Order<NoteEntity>) -> Unit,
    onToggleSortDirection: () -> Unit
) {

    val optionsOrderBy = listOf(
        OrderByOption(
            label = "Título",
            value = Order(NoteEntity::title)
        ),
        OrderByOption(
            label = "Data de criação",
            value = Order(NoteEntity::createdAt)
        ),
        OrderByOption(
            label = "Data de atualização",
            value = Order(NoteEntity::updatedAt)
        )
    )

    Surface(
        modifier = modifier.zIndex(0f),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (modeSelection) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = selectedAll,
                            onClick = onSelectedAll
                        )
                        Text(
                            text = "Todos",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.size(4.dp))
                        Text(
                            "Excluir ($elementsSelectedTotal)",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp
                        )
                    }
                }

            }

            Spacer(Modifier.weight(1f))

            AppButtonOrderBy(
                options = optionsOrderBy,
                value = sort,
                onValueChange = onSortChange,
                sortDirection = sort.ascending,
                onSortDirectionChange = onToggleSortDirection,
                colorBackground = Color.Transparent
            )
        }
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
            uiState = NotesUiState(),
            onAction = {},
            onEditNavigation = {}
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
            uiState = NotesUiState(),
            onAction = {},
            onEditNavigation = {}
        )
    }
}