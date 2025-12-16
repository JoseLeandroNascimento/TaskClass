package com.joseleandro.taskclass.ui.notes.presentation.notesScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.taskclass.common.composables.OrderByOption
import com.joseleandro.taskclass.ui.theme.TaskClassTheme

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
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    onEditNavigation: (Int) -> Unit,
    uiState: NotesUiState
) {

    val valueDefault = "name"
    val optionsOrderBy = listOf(
        OrderByOption(
            label = "Nome",
            value = "name"
        ),
        OrderByOption(
            label = "Data de criação",
            value = "dateCreate"
        ),
        OrderByOption(
            label = "Data de atalização",
            value = "dateUpdate"
        )
    )

    var orderBy by remember { mutableStateOf(valueDefault) }
    var sortDirection by remember { mutableStateOf(true) }


    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {

        Column(modifier = Modifier.fillMaxWidth()) {


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

                            item(
                                span = StaggeredGridItemSpan.FullLine
                            ) {
                                Row(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
//                                    AppButtonOrderBy(
//                                        options = optionsOrderBy,
//                                        value = orderBy,
//                                        onValueChange = {
//                                            orderBy = it
//                                        },
//                                        sortDirection = sortDirection,
//                                        onSortDirectionChange = {
//                                            sortDirection = !sortDirection
//                                        }
//
//                                    )
                                }
                            }

                            items(items = uiState.notes, key = { it.id }) { note ->
                                NoteCard(
                                    onClick = {
                                        onEditNavigation(note.id)
                                    },
                                    note = note
                                )
                            }

                            item(
                                span = StaggeredGridItemSpan.FullLine
                            ) {
                                Spacer(
                                    modifier = Modifier.height(80.dp)
                                )
                            }
                        }
                    }
                }
            }
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
            onEditNavigation = {}
        )
    }
}