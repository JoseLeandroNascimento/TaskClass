package com.example.taskclass.ui.events.presentation.eventAllScreen

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.ui.events.domain.EventFilter
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White

@Composable
fun EventAllScreen(
    modifier: Modifier = Modifier,
    viewModel: EventAllViewModel,
    onCreateNewEvent: () -> Unit,
    onBack: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val filter = viewModel.filter.collectAsStateWithLifecycle().value

    EventAllScreen(
        modifier = modifier,
        uiState = uiState,
        filter = filter,
        onBack = onBack,
        onCreateNewEvent = onCreateNewEvent,
        filterByStatus = viewModel::filterByStatus,
        updateStatusChecked = viewModel::updateStatusChecked
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventAllScreen(
    modifier: Modifier = Modifier,
    filter: EventFilter,
    uiState: EventAllUiState,
    filterByStatus: ((EEventStatus?) -> Unit)? = null,
    updateStatusChecked: ((Int, Boolean) -> Unit)? = null,
    onCreateNewEvent: (() -> Unit)? = null,
    onBack: () -> Unit
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Eventos",
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
                        onClick = {}
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

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = MaterialTheme.colorScheme.background
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {

                Surface(
                    modifier = Modifier
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

                        Box(
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            IconButton(
                                onClick = {},
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                                    contentColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }

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
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                            ) {
                                Icon(
                                    modifier = Modifier.size(40.dp),
                                    imageVector = Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "Nenhum evento encontrado",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }

                        }

                        else -> {

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {


                                items(
                                    items = uiState.events,
                                    key = { it.id }
                                ) { event ->

                                    EventItemCard(
                                        title = event.title,
                                        color = event.color,
                                        checked = event.status == EEventStatus.CONCLUIDA,
                                        date = event.date,
                                        time = event.time,
                                        onCheckedChange = { checked ->
                                            updateStatusChecked?.invoke(event.id, checked)
                                        }
                                    )
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
            uiState = EventAllUiState()
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
            uiState = EventAllUiState()
        )
    }
}