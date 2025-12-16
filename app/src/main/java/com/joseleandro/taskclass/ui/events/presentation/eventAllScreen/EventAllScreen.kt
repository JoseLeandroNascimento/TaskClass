package com.joseleandro.taskclass.ui.events.presentation.eventAllScreen

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.taskclass.R
import com.joseleandro.taskclass.common.composables.AppSearchBarScaffold
import com.joseleandro.taskclass.core.data.model.enums.EEventStatus
import com.joseleandro.taskclass.ui.events.domain.EventFilter
import com.joseleandro.taskclass.ui.events.presentation.components.EventItemCard
import com.joseleandro.taskclass.ui.events.presentation.components.EventSearchItemCard
import com.joseleandro.taskclass.ui.events.presentation.components.FilterEventChip
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.joseleandro.taskclass.ui.theme.White

@Composable
fun EventAllScreen(
    viewModel: EventAllViewModel,
    onCreateNewEvent: () -> Unit,
    onBack: () -> Unit,
    onEditNavigation: (Int) -> Unit,
    onViewEventNavigation: (Int) -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val filter = viewModel.filter.collectAsStateWithLifecycle().value

    EventAllScreen(
        uiState = uiState,
        filter = filter,
        onBack = onBack,
        onCreateNewEvent = onCreateNewEvent,
        filterByStatus = viewModel::filterByStatus,
        updateStatusChecked = viewModel::updateStatusChecked,
        search = viewModel::updateQuery,
        onViewEventNavigation = onViewEventNavigation,
        onEdit = onEditNavigation,
        onDelete = { id ->
            viewModel.deleteEvent(id)
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventAllScreen(
    filter: EventFilter,
    uiState: EventAllUiState,
    filterByStatus: ((EEventStatus?) -> Unit)? = null,
    updateStatusChecked: ((Int, Boolean) -> Unit)? = null,
    search: ((String) -> Unit)? = null,
    onCreateNewEvent: (() -> Unit)? = null,
    onBack: () -> Unit,
    onViewEventNavigation: (Int) -> Unit,
    onDelete: ((Int) -> Unit)? = null,
    onEdit: ((Int) -> Unit)? = null
) {

    var expandedSearch by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(expandedSearch) {

        if (!expandedSearch && filter.query?.isNotEmpty() == true) {
            search?.invoke("")
        }
    }

    AppSearchBarScaffold(
        expanded = expandedSearch,
        onQueryChange = {
            search?.invoke(it)
        },
        searchItem = { item ->
            EventSearchItemCard(
                event = item,
                onNavigation = onViewEventNavigation
            )
        },
        onExpandedChange = {
            expandedSearch = it
        },
        key = { _, item -> item.event.id },
        items = uiState.events.toList(),
        isLoading = uiState.isLoading,
        placeholder = stringResource(R.string.pesquisar_eventos),
        query = filter.query ?: "",
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.eventos),
                            style = MaterialTheme.typography.titleMedium
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
                    actions = {
                        IconButton(
                            onClick = {
                                expandedSearch = !expandedSearch
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
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
                    onClick = {
                        onCreateNewEvent?.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = White
                    )
                }
            }


        ) { innerPadding ->

            EventAllContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                filter = filter,
                uiState = uiState,
                filterByStatus = filterByStatus,
                updateStatusChecked = updateStatusChecked,
                onViewEventNavigation = onViewEventNavigation,
                onDelete = onDelete,
                onEdit = onEdit
            )

        }
    }

}

@Composable
fun EventAllContent(
    modifier: Modifier = Modifier,
    filter: EventFilter,
    uiState: EventAllUiState,
    filterByStatus: ((EEventStatus?) -> Unit)? = null,
    updateStatusChecked: ((Int, Boolean) -> Unit)? = null,
    onViewEventNavigation: (Int) -> Unit,
    onDelete: ((Int) -> Unit)? = null,
    onEdit: ((Int) -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {

            EventAllFilter(
                filter = filter,
                filterByStatus = filterByStatus
            )

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                when {

                    uiState.isLoading -> {

                        CircularProgressIndicator()
                    }

                    uiState.events.isEmpty() -> {

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
                                text = stringResource(R.string.nenhum_evento_agendado),
                                style = MaterialTheme.typography.labelMedium
                            )
                        }

                    }

                    else -> {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {

                            uiState.eventsGroup.forEach { eventGroup ->

                                item {
                                    Text(
                                        text = eventGroup.key.label,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .padding(start = 4.dp)
                                    )
                                }

                                items(
                                    items = eventGroup.value,
                                    key = { it.event.id }
                                ) { event ->

                                    EventItemCard(
                                        id = event.event.id,
                                        title = event.event.title,
                                        color = event.typeEvent.color,
                                        checked = event.event.completed,
                                        dateTime = event.event.datetime,
                                        onCheckedChange = { checked ->
                                            updateStatusChecked?.invoke(event.event.id, checked)
                                        },
                                        typeEvent = event.typeEvent.name,
                                        onSelected = {
                                            onViewEventNavigation(event.event.id)
                                        },
                                        onEdit = {
                                            onEdit?.invoke(it)
                                        },
                                        onDelete = {
                                            onDelete?.invoke(it)
                                        }
                                    )
                                }

                            }


                            item {

                                Spacer(
                                    modifier = Modifier
                                        .height(80.dp)
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
fun EventAllFilter(
    modifier: Modifier = Modifier,
    filter: EventFilter,
    filterByStatus: ((EEventStatus?) -> Unit)? = null,
) {


    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(state = rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                FilterEventChip(
                    selected = filter.status == null,
                    onSelected = {
                        filterByStatus?.invoke(null)
                    },
                    value = null
                )

                EEventStatus.entries.forEach { status ->

                    FilterEventChip(
                        selected = filter.status == status,
                        onSelected = {
                            filterByStatus?.invoke(status)
                        },
                        value = status
                    )
                }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EventAllLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        EventAllScreen(
            onBack = {},
            filter = EventFilter(),
            uiState = EventAllUiState(),
            onViewEventNavigation = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventAllDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        EventAllScreen(
            filter = EventFilter(),
            onBack = {},
            uiState = EventAllUiState(),
            onViewEventNavigation = {}
        )
    }
}