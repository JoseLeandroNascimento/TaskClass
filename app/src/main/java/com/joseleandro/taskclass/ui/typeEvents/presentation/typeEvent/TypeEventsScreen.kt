package com.joseleandro.taskclass.ui.typeEvents.presentation.typeEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.taskclass.R
import com.joseleandro.taskclass.common.composables.AppButtonOrderBy
import com.joseleandro.taskclass.common.composables.AppCardDefault
import com.joseleandro.taskclass.common.composables.AppConfirmDialog
import com.joseleandro.taskclass.common.composables.CircleIndicator
import com.joseleandro.taskclass.common.composables.OrderByOption
import com.joseleandro.taskclass.common.composables.SwipeContainer
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.joseleandro.taskclass.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeEventsScreen(
    viewModel: TypeEventsViewModel,
    onCreateNavigation: () -> Unit,
    onEditNavigation: (Int) -> Unit,
    onBack: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val filterSort by viewModel.filterSort.collectAsStateWithLifecycle()

    TypeEventsScreen(
        onBack = onBack,
        uiState = uiState,
        onAction = viewModel::onAction,
        onCreateNavigation = onCreateNavigation,
        filterSort = filterSort,
        onSelectedItemEdit = {
            onEditNavigation(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeEventsScreen(
    onBack: () -> Unit,
    uiState: TypeEventsUiState,
    filterSort: Order<TypeEventEntity>,
    onCreateNavigation: () -> Unit,
    onSelectedItemEdit: ((Int) -> Unit)? = null,
    onAction: (TypeEventAction) -> Unit
) {


    val optionsOrderBy = listOf(
        OrderByOption(
            label = "Nome",
            value = Order(TypeEventEntity::name)
        ),
        OrderByOption(
            label = "Data de criação",
            value = Order(TypeEventEntity::createdAt)
        ),
        OrderByOption(
            label = "Data de atualização",
            value = Order(TypeEventEntity::updatedAt)
        )
    )




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.tipos_de_eventos),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                    actionIconContentColor = White
                )
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = onCreateNavigation
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            when {

                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.typeEvents.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
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
                                        onAction(TypeEventAction.OrderBy(it))
                                    },
                                    sortDirection = filterSort.ascending,
                                    onSortDirectionChange = {
                                        onAction(
                                            TypeEventAction.OrderBy(
                                                filterSort.copy(
                                                    selector = filterSort.selector,
                                                    ascending = !filterSort.ascending
                                                )
                                            )
                                        )
                                    }
                                )
                            }
                        }

                        items(items = uiState.typeEvents, key = { it.id }) { typeEventItem ->
                            TypeEventCardItem(
                                typeEventItem = typeEventItem,
                                onDelete = {
                                    onAction(
                                        TypeEventAction.OnDelete(typeEventItem.id)
                                    )
                                },
                                onSelectedItemEdit = onSelectedItemEdit
                            )
                        }
                    }
                }

                else -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {

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
                                text = "Nenhum tipo de evento encontrado!",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun TypeEventCardItem(
    modifier: Modifier = Modifier,
    typeEventItem: TypeEventEntity,
    onSelectedItemEdit: ((Int) -> Unit)? = null,
    onDelete: (Int) -> Unit
) {

    var openDropdown by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }
    var stateSwipe: SwipeToDismissBoxState? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()


    if (confirmDelete) {
        AppConfirmDialog(
            onDismissRequest = {
                confirmDelete = false
                scope.launch {
                    stateSwipe?.reset()
                }
            },
            onConfirm = {
                onDelete(typeEventItem.id)
                confirmDelete = false
            },
            onCancel = {
                confirmDelete = false

                scope.launch {
                    stateSwipe?.reset()
                }

            },
            title = stringResource(R.string.title_dialog_excluir_tipo_de_evento),
            description = stringResource(
                R.string.description_dialog_tem_certeza_que_deseja_excluir_o_tipo_de_evento,
                typeEventItem.name
            )
        )
    }

    SwipeContainer(
        modifier = Modifier.clip(shape = RoundedCornerShape(12.dp)),
        onRemove = { confirmDelete = true },
        onToggleDone = {
            onSelectedItemEdit?.invoke(typeEventItem.id)
        },
        startIcon = Icons.Default.Delete,
        endIcon = Icons.Default.Edit
    ) {

        stateSwipe = state

        AppCardDefault(
            modifier = modifier.fillMaxWidth(),
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement
                        .spacedBy(8.dp)
                ) {

                    CircleIndicator(
                        color = typeEventItem.color,
                        size = 28.dp
                    )

                    Text(
                        text = typeEventItem.name,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
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
                        containerColor = MaterialTheme.colorScheme.background,
                        onDismissRequest = { openDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                onSelectedItemEdit?.invoke(typeEventItem.id)
                                openDropdown = false
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
                                openDropdown = false
                                confirmDelete = true
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TypeEventsScreenLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        TypeEventsScreen(
            onBack = {},
            filterSort = Order(TypeEventEntity::createdAt),
            uiState = TypeEventsUiState(
                typeEvents = listOf(),
                isLoading = true
            ),
            onCreateNavigation = {},
            onAction = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TypeEventsScreenDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        TypeEventsScreen(
            onBack = {},
            uiState = TypeEventsUiState(),
            filterSort = Order(TypeEventEntity::createdAt),
            onCreateNavigation = {},
            onAction = {}
        )
    }
}


