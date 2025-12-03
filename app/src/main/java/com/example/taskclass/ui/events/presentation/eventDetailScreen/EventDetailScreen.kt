package com.example.taskclass.ui.events.presentation.eventDetailScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppDialog
import com.example.taskclass.common.composables.CircleIndicator
import com.example.taskclass.common.utils.toFormattedDateTime
import com.example.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.example.taskclass.core.data.model.entity.EventEntity
import com.example.taskclass.core.data.model.entity.TypeEventEntity
import com.example.taskclass.ui.events.domain.statusCurrent
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White
import java.time.Instant

@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    EventDetailScreen(
        onBack = onBack,
        uiState = uiState,
        onToggleComplete = {
            viewModel.updateStatusChecked(it)
        },
        onDelete = viewModel::deleteEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    uiState: EventDetailUiState,
    onDelete: (() -> Unit)? = null,
    onToggleComplete: ((Boolean) -> Unit)? = null
) {

    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    if(uiState.isBackNavigation) onBack()

    if (showConfirmDeleteDialog) {

        ConfirmDeleteDialog(
            onDismissRequest = { showConfirmDeleteDialog = false },
            confirmDelete = {
                onDelete?.invoke()
                showConfirmDeleteDialog = false
            }
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalhes do Evento",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
            )
        },
        floatingActionButton = {
            EventActionsFab(
                onEdit = { /* abrir tela de edição */ },
                onDelete = { showConfirmDeleteDialog = true }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 8.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }

                uiState.event != null -> {
                    EventDetailContent(
                        event = uiState.event,
                        onToggleComplete = {
                            onToggleComplete?.invoke(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EventDetailContent(
    event: EventEndTypeEventDto,
    onToggleComplete: (Boolean) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val isCompleted = event.event.completed

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(state = rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {

        Spacer(Modifier.height(20.dp))

        CircleIndicator(
            color = event.typeEvent.color,
            size = 80.dp
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = event.event.title,
            style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = colorScheme.onSurface
        )

        Text(
            text = event.typeEvent.name,
            style = typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = event.typeEvent.color
            ),
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(Modifier.height(28.dp))

        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(.5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f))
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Text(
                    "Informações do Evento",
                    style = typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = 20.dp,
                    vertical = 22.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                InfoRow(
                    icon = Icons.Default.CalendarMonth,
                    label = "Data",
                    value = event.event.datetime.toFormattedDateTime("dd/MM/yyyy")
                )

                InfoRow(
                    icon = Icons.Default.Timer,
                    label = "Horário",
                    value = event.event.datetime.toFormattedDateTime("HH:mm")
                )

                if (event.event.description.isNotBlank()) {
                    InfoRow(
                        icon = Icons.Default.Description,
                        label = "Descrição",
                        overflowValue = TextOverflow.Ellipsis,
                        value = event.event.description
                    )
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(colorScheme.outlineVariant.copy(alpha = .4f))
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        "Estado do Evento",
                        style = typography.labelLarge,
                        color = colorScheme.onSurfaceVariant
                    )

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = event.event.statusCurrent().label,
                                style = typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor =
                                if (isCompleted)
                                    Color(0xFFC8E6C9).copy(alpha = 0.35f)
                                else
                                    colorScheme.primary.copy(alpha = 0.12f),
                            labelColor =
                                if (isCompleted)
                                    Color(0xFFC8E6C9)
                                else colorScheme.primary
                        ),
                        border = null
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        CompleteEventButton(
            isCompleted = event.event.completed,
            onToggle = { onToggleComplete(!event.event.completed) }
        )

        Spacer(Modifier.height(80.dp))


    }
}

@Composable
fun ConfirmDeleteDialog(
    onDismissRequest: () -> Unit,
    confirmDelete: () -> Unit
) {
    AppDialog(
        onDismissRequest = onDismissRequest,
        title = "Excluir Evento",
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Tem certeza que deseja excluir este evento?",
                style = MaterialTheme.typography.bodyMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {

                TextButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(R.string.btn_cancelar),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                AppButton(
                    label = stringResource(R.string.btn_confirm),
                    onClick = confirmDelete
                )
            }
        }

    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EventActionsFab(
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {

        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn(
                animationSpec = tween(200)
            ) + slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight / 3 },
                animationSpec = tween(250, easing = FastOutSlowInEasing)
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(250, easing = FastOutSlowInEasing)
            ),
            exit = fadeOut(
                animationSpec = tween(150)
            ) + slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight / 3 },
                animationSpec = tween(200, easing = FastOutSlowInEasing)
            ) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(200, easing = FastOutSlowInEasing)
            )
        ) {
            Column(
                modifier = Modifier.padding(end = 4.dp, bottom = 72.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.End
            ) {

                val editColor = Color(0xFFBBDEFB)
                val deleteColor = Color(0xFFFFCDD2)
                val iconColor = White

                FloatingActionButton(
                    onClick = onEdit,
                    containerColor = editColor,
                    contentColor = iconColor
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Editar"
                    )
                }

                FloatingActionButton(
                    onClick = onDelete,
                    containerColor = deleteColor,
                    contentColor = iconColor
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Excluir")
                }
            }
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            AnimatedContent(
                targetState = expanded,
                label = "fab_icon_transition"
            ) { isExpanded ->
                Icon(
                    imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.MoreVert,
                    contentDescription = if (isExpanded) "Fechar ações" else "Mais ações"
                )
            }
        }
    }
}


@Composable
fun CompleteEventButton(
    isCompleted: Boolean,
    onToggle: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val grayColor = Color(0xFFE0E0E0)
    val greenLightColor = Color(0xFFC8E6C9)

    val animatedContainerColor by animateColorAsState(
        targetValue = if (isCompleted) greenLightColor else grayColor,
        animationSpec = tween(durationMillis = 450)
    )

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        onClick = onToggle,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedContainerColor,
            contentColor = Color.Black
        )
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            modifier = Modifier.padding(end = 6.dp)
        )

        Text(
            text = if (isCompleted) "Evento Concluído" else "Marcar como Concluído",
            style = typography.titleSmall
        )
    }
}


@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    overflowValue: TextOverflow = TextOverflow.Clip,
    maxLineValue: Int = Int.MAX_VALUE
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                style = typography.labelSmall,
                color = colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                overflow = overflowValue,
                maxLines = maxLineValue,
                style = typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = colorScheme.onSurface
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
                        color = MaterialTheme.colorScheme.primary,
                        name = "Prova"
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
                        color = MaterialTheme.colorScheme.primary,
                        name = "Prova"
                    )
                )
            )
        )
    }
}
