package com.example.taskclass.ui.discipline.presentation.disciplineScreen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppButtonOrderBy
import com.example.taskclass.common.composables.AppCardDefault
import com.example.taskclass.common.composables.AppConfirmDialog
import com.example.taskclass.common.composables.AppSearchBarScaffold
import com.example.taskclass.common.composables.CircleIndicator
import com.example.taskclass.common.composables.OrderByOption
import com.example.taskclass.common.composables.SwipeContainer
import com.example.taskclass.core.data.model.Order
import com.example.taskclass.core.data.model.entity.DisciplineEntity
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1


@Composable
fun DisciplineScreen(
    onBack: () -> Unit,
    onCreateDiscipline: () -> Unit,
    onEditDiscipline: (Int) -> Unit,
    viewModel: DisciplineViewModel
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterQuery by viewModel.filterQuery.collectAsStateWithLifecycle()
    val filterSort by viewModel.filterSort.collectAsStateWithLifecycle()

    DisciplineScreen(
        onBack = onBack,
        uiState = uiState,
        onCreateDiscipline = onCreateDiscipline,
        onEditDiscipline = onEditDiscipline,
        onAction = viewModel::onAction,
        filterQuery = filterQuery,
        filterSort = filterSort
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineScreen(
    onBack: () -> Unit,
    onAction: (action: DisciplinesAction) -> Unit,
    onCreateDiscipline: () -> Unit,
    filterQuery: String,
    filterSort: Order<DisciplineEntity>,
    uiState: DisciplineUiState,
    onEditDiscipline: (Int) -> Unit,
) {

    var expandedSearch by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(expandedSearch) {

        if (!expandedSearch && filterQuery.isNotEmpty()) {

            onAction(DisciplinesAction.UpdateQuery(""))
        }
    }

    AppSearchBarScaffold(
        expanded = expandedSearch,
        query = filterQuery,
        onExpandedChange = { expandedSearch = it },
        onQueryChange = {
            onAction(DisciplinesAction.UpdateQuery(it))
        },
        placeholder = stringResource(R.string.placeholder_buscar_disciplina),
        isLoading = uiState.isLoading,
        items = uiState.disciplines,
        key = { _, item -> item.id },
        searchItem = { discipline ->
            DisciplineSearchCardItem(
                discipline = discipline,
                onEditDiscipline = onEditDiscipline
            )
        }
    ) {
        Scaffold(
            topBar = {
                DisciplineTopBar(
                    onBack = onBack,
                    onSearchActivity = {
                        expandedSearch = !expandedSearch
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onCreateDiscipline,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar",
                        tint = White
                    )
                }
            }
        ) { innerPadding ->

            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(top = 8.dp)

            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    when {

                        uiState.isLoading -> {
                            CircularProgressIndicator()
                        }

                        uiState.disciplines.isNotEmpty() -> {

                            DisciplineContent(
                                modifier = Modifier.align(alignment = Alignment.TopCenter),
                                uiState = uiState,
                                updateFilterSort = {
                                    onAction(
                                        DisciplinesAction.UpdateFilterSort(
                                            it
                                        )
                                    )
                                },

                                onDeleteDiscipline = {
                                    onAction(DisciplinesAction.OnDeleteDiscipline(it))
                                },
                                filterSort = filterSort,
                                onEditDiscipline = onEditDiscipline
                            )
                        }

                        else -> {
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
                                    text = "Nenhuma disciplina encontrada!",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisciplineSearchCardItem(
    discipline: DisciplineEntity,
    modifier: Modifier = Modifier,
    onEditDiscipline: (Int) -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                CircleIndicator(
                    color = discipline.color,
                    size = 30.dp
                )

                Text(
                    text = discipline.title,
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(
                onClick = {
                    onEditDiscipline(discipline.id)
                }
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineTopBar(
    onBack: () -> Unit,
    onSearchActivity: () -> Unit
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = White,
            navigationIconContentColor = White,
            actionIconContentColor = White
        ),
        title = {
            Text(
                text = "Disciplinas",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        actions = {
            IconButton(
                onClick = onSearchActivity
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        }
    )
}

@Composable
fun DisciplineContent(
    modifier: Modifier = Modifier,
    uiState: DisciplineUiState,
    filterSort: Order<DisciplineEntity>,
    updateFilterSort: (order: Order<DisciplineEntity>) -> Unit,
    onDeleteDiscipline: (Int) -> Unit,
    onEditDiscipline: (Int) -> Unit,
) {

    val optionsOrderBy = listOf(
        OrderByOption(
            label = "Nome",
            value = Order(DisciplineEntity::title)
        ),
        OrderByOption(
            label = "Data de criação",
            value = Order(DisciplineEntity::createdAt)
        ),
        OrderByOption(
            label = "Data de atualização",
            value = Order(DisciplineEntity::updatedAt)
        )
    )



    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        item {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AppButtonOrderBy(
                    options = optionsOrderBy,
                    value = filterSort,
                    onValueChange = {
                        updateFilterSort(it)
                    },
                    sortDirection = filterSort.ascending,
                    onSortDirectionChange = {
                        updateFilterSort(
                            filterSort.copy(
                                ascending = !filterSort.ascending
                            )
                        )
                    }
                )
            }
        }

        items(uiState.disciplines, key = { it.id }) { discipline ->
            DisciplineItem(
                modifier = Modifier.animateItem(),
                discipline = discipline,
                onDeleteDiscipline = onDeleteDiscipline,
                onEditDiscipline = onEditDiscipline
            )
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun DisciplineItem(
    modifier: Modifier = Modifier,
    onDeleteDiscipline: (Int) -> Unit,
    onEditDiscipline: (Int) -> Unit,
    discipline: DisciplineEntity
) {
    var openDropdown by rememberSaveable { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    var lastSwipeState: SwipeToDismissBoxState? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()

    if (confirmDelete) {
        AppConfirmDialog(
            onDismissRequest = {
                confirmDelete = false

                scope.launch {
                    lastSwipeState?.reset()
                }
            },
            title = stringResource(R.string.title_dialog_excluir_disciplina),
            description = stringResource(
                R.string.tem_certeza_que_deseja_excluir_a_disciplina,
                discipline.title
            ),
            onConfirm = {
                confirmDelete = false
                onDeleteDiscipline(discipline.id)
            },
            onCancel = {
                confirmDelete = false
                scope.launch {
                    lastSwipeState?.reset()
                }
            }

        )
    }

    SwipeContainer(
        modifier = Modifier.clip(shape = RoundedCornerShape(12.dp)),
        onRemove = {
            confirmDelete = true
        },
        onToggleDone = {
            onEditDiscipline(discipline.id)
        },
        startIcon = Icons.Default.Delete,
        endIcon = Icons.Default.Edit,
    ) {

        lastSwipeState = state

        AppCardDefault(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CircleIndicator(
                        size = 28.dp,
                        color = discipline.color
                    )

                    Text(
                        text = discipline.title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    )
                }

                Box {
                    IconButton(onClick = { openDropdown = !openDropdown }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opções"
                        )
                    }
                    DropdownMenu(
                        expanded = openDropdown,
                        containerColor = MaterialTheme.colorScheme.surface,
                        onDismissRequest = { openDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                openDropdown = false
                                onEditDiscipline(discipline.id)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Excluir") },
                            onClick = {
                                confirmDelete = true
                                openDropdown = false
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun DisciplineScreenPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        DisciplineScreen(
            onBack = {},
            uiState = DisciplineUiState(),
            onCreateDiscipline = {},
            onAction = {},
            onEditDiscipline = {},
            filterQuery = "",
            filterSort = Order(DisciplineEntity::createdAt),
        )
    }
}

@Preview
@Composable
private fun DisciplineScreenDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        DisciplineScreen(
            onBack = {},
            uiState = DisciplineUiState(),
            onCreateDiscipline = {},
            filterSort = Order(DisciplineEntity::createdAt),
            onAction = {},
            onEditDiscipline = {},
            filterQuery = "",
        )
    }
}