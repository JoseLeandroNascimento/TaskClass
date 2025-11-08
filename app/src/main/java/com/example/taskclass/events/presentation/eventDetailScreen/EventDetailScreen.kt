package com.example.taskclass.events.presentation.eventDetailScreen

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.taskclass.common.composables.CircleIndicator
import com.example.taskclass.core.data.model.DateInt
import com.example.taskclass.core.data.model.EEventStatus
import com.example.taskclass.core.data.model.Time
import com.example.taskclass.core.data.model.dto.EventWithType
import com.example.taskclass.core.data.model.formatted
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White

@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    viewModel: EventDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    EventDetailScreen(
        onBack = onBack,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    onBack: () -> Unit,
    uiState: EventDetailUiState
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Evento", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding).padding(top = 8.dp)
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
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(24.dp)
                    )
                }

                uiState.event != null -> {
                    EventDetailContent(event = uiState.event!!)
                }
            }
        }
    }
}

@Composable
fun EventDetailContent(event: EventWithType) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        CircleIndicator(
            color = event.color,
            size = 68.dp
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = event.title,
            style = typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        event.typeEventName?.let {
            Text(
                text = it,
                style = typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                color = event.color,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(Modifier.height(28.dp))

        ElevatedCard(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                InfoRow(
                    icon = Icons.Default.CalendarMonth,
                    label = "Data",
                    value = event.date.formatted()
                )

                InfoRow(
                    icon = Icons.Default.Schedule,
                    label = "Horário",
                    value = event.time.formatted()
                )

                if (event.description.isNotBlank()) {
                    InfoRow(
                        icon = Icons.Default.Description,
                        label = "Descrição",
                        value = event.description
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Chip de status
        val statusText =  "Pendente"
        val statusColor = colorScheme.primary

        AssistChip(
            onClick = { },
            label = {
                Text(
                    text = statusText,
                    style = typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                )
            },
            border = null,
            colors = AssistChipDefaults.assistChipColors(
                containerColor = statusColor.copy(alpha = 0.1f),
                labelColor = statusColor,
                leadingIconContentColor = statusColor
            )
        )
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
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
                event = EventWithType(
                    date = DateInt(20251103),
                    description = "Teasdasd as das",
                    id = 1,
                    time = Time(830),
                    title = "Teste",
                    typeEventColor = MaterialTheme.colorScheme.primary,
                    typeEventId = 2,
                    typeEventName = "Prova",
                    status = EEventStatus.PENDENTE

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
                event = EventWithType(
                    date = DateInt(20251103),
                    description = "Teasdasd as das",
                    id = 1,
                    time = Time(830),
                    title = "Teste",
                    typeEventColor = MaterialTheme.colorScheme.primary,
                    typeEventId = 2,
                    typeEventName = "Prova",
                    status = EEventStatus.PENDENTE
                )
            )
        )
    }
}
