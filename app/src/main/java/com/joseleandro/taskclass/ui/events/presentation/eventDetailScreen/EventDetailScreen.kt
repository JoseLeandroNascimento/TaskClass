package com.joseleandro.taskclass.ui.events.presentation.eventDetailScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joseleandro.taskclass.common.composables.AppCardDefault
import com.joseleandro.taskclass.common.composables.CircleIndicator
import com.joseleandro.taskclass.common.utils.toFormattedDateTime
import com.joseleandro.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.joseleandro.taskclass.core.data.model.entity.EventEntity
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import com.joseleandro.taskclass.ui.events.presentation.components.ConfirmDeleteDialog
import com.joseleandro.taskclass.ui.theme.GreenCompleted
import com.joseleandro.taskclass.ui.theme.OnSurfaceDark
import com.joseleandro.taskclass.ui.theme.OnSurfaceLight
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.joseleandro.taskclass.ui.theme.White
import java.time.Instant

@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel(),
    onEdit: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    EventDetailScreen(
        onBack = onBack,
        uiState = uiState,
        onToggleComplete = {
            viewModel.updateStatusChecked(it)
        },
        onDelete = viewModel::deleteEvent,
        onEdit = onEdit
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    uiState: EventDetailUiState,
    onDelete: (() -> Unit)? = null,
    onEdit: ((Int) -> Unit)? = null,
    onToggleComplete: ((Boolean) -> Unit)? = null
) {

    var menuActionExpanded by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    if(confirmDelete){
        ConfirmDeleteDialog(
            onDismissRequest = {
                confirmDelete = false
            },
            confirmDelete = {
                onDelete?.invoke()
            }
        )
    }

    LaunchedEffect(uiState.isBackNavigation) {

        if(uiState.isBackNavigation){
            onBack()
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.event?.let { (event, typeEvent) ->
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircleIndicator(
                                    color = typeEvent.color,
                                    size = 24.dp
                                )
                                Text(
                                    text = typeEvent.name,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = White.copy(alpha = .8f)
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
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
                    Box() {
                        IconButton(
                            onClick = {
                                menuActionExpanded = !menuActionExpanded
                            }
                        ) {
                            Icon(

                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = menuActionExpanded,
                            onDismissRequest = {
                                menuActionExpanded = false
                            },
                            containerColor = MaterialTheme.colorScheme.background
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Editar",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    uiState.event?.let { (event, _) ->
                                        onEdit?.invoke(event.id)
                                        menuActionExpanded = false
                                    }
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Excluir",
                                        style = MaterialTheme.typography.labelLarge
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier.size(20.dp),
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    confirmDelete = !confirmDelete
                                    menuActionExpanded = false
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                    actionIconContentColor = White
                )
            )
        }) { innerPadding ->
        EventDetailContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onToggleComplete = onToggleComplete
        )
    }

}

@Composable
fun EventDetailContent(
    modifier: Modifier = Modifier,
    uiState: EventDetailUiState,
    onToggleComplete: ((Boolean) -> Unit)? = null
) {

    val completed by derivedStateOf { uiState.event?.event?.completed ?: false }

    val transition = updateTransition(
        targetState = completed,
        label = "statusTransition"
    )

    val backgroundColor by transition.animateColor(label = "bg") {
        if (it) GreenCompleted else OnSurfaceDark
    }

    val textColor by transition.animateColor(label = "text") {
        if (it) White else OnSurfaceLight
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        uiState.event?.let { (event, typeEvent) ->

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                AppCardDefault(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        EventInfoRow(
                            label = "Data",
                            value = event.datetime.toFormattedDateTime("dd/MM/yyyy"),
                            icon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        EventInfoRow(
                            label = "Horário",
                            value = event.datetime.toFormattedDateTime("HH:mm"),
                            icon = {
                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    imageVector = Icons.Default.AccessTimeFilled,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Status do evento",
                                style = MaterialTheme.typography.labelLarge
                            )

                            Box(
                                modifier = Modifier
                                    .background(
                                        color = backgroundColor,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .animateContentSize()
                            ) {
                                AnimatedContent(
                                    targetState = completed,
                                    transitionSpec = {
                                        fadeIn() + slideInHorizontally() togetherWith
                                                fadeOut() + slideOutHorizontally()
                                    },
                                    label = "statusText"
                                ) { isCompleted ->
                                    Text(
                                        text = if (isCompleted) "Concluído" else "Pendente",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = textColor
                                        )
                                    )
                                }
                            }

                        }
                    }
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = backgroundColor,
                    ),
                    onClick = {
                        onToggleComplete?.invoke(!completed)
                    }
                ) {

                    AnimatedContent(
                        targetState = completed,
                        transitionSpec = {
                            fadeIn() + scaleIn() togetherWith
                                    fadeOut() + scaleOut()
                        },
                        label = "buttonContent"
                    ) { isCompleted ->
                        Row(
                            modifier = Modifier.padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = textColor
                            )
                            Text(
                                modifier = Modifier.padding(start = 8.dp),
                                text = if (isCompleted)
                                    "Evento concluído"
                                else
                                    "Marcar como concluído",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventInfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        icon()

        Column(
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = .8f),
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EventDetailLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        EventDetailScreen(
            onBack = {},
            uiState = EventDetailUiState(
                error = null,
                isLoading = false,
                event = EventEndTypeEventDto(
                    event = EventEntity(
                        description = "Teasdasd as das",
                        title = "Teste",
                        completed = false,
                        typeEventId = 1,
                        datetime = Instant.now(),
                        updatedAt = System.currentTimeMillis(),
                        createdAt = System.currentTimeMillis(),
                    ),
                    typeEvent = TypeEventEntity(
                        color = Color.Green,
                        name = "Prova",
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventDetailDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        EventDetailScreen(
            onBack = {},
            uiState = EventDetailUiState(
                error = null,
                isLoading = false,
                event = EventEndTypeEventDto(
                    event = EventEntity(
                        description = "Teasdasd as das",
                        title = "Teste",
                        completed = false,
                        typeEventId = 1,
                        datetime = Instant.now(),
                        updatedAt = System.currentTimeMillis(),
                        createdAt = System.currentTimeMillis(),
                    ),
                    typeEvent = TypeEventEntity(
                        color = Color.Green,
                        name = "Prova",
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
            )
        )
    }
}
