package com.joseleandro.taskclass.ui.events.presentation.eventsScreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.joseleandro.taskclass.R
import com.joseleandro.taskclass.common.composables.AppCardDefault
import com.joseleandro.taskclass.common.composables.CircleIndicator
import com.joseleandro.taskclass.common.utils.toFormattedDateTime
import com.joseleandro.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    onSelectedEvent: (Int) -> Unit,
    onNavigateToAllEvents: () -> Unit,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EventScreen(
        modifier = modifier,
        uiState = uiState,
        onNavigateToAllEvents = onNavigateToAllEvents,
        onSelectedEvent = { onSelectedEvent(it) },
        onCheckedStatusEvent = viewModel::onCheckedStatusEvent,
        onDateSelected = viewModel::onDateSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    uiState: EventsUiState,
    onDateSelected: (LocalDate) -> Unit,
    onNavigateToAllEvents: () -> Unit,
    onSelectedEvent: (Int) -> Unit,
    onCheckedStatusEvent: (Int, Boolean) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val stateList = rememberLazyListState()

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(6) }
    val endMonth = remember { currentMonth.plusMonths(6) }
    val daysOfWeek = remember { daysOfWeek() }
    val scope = rememberCoroutineScope()

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = daysOfWeek.first()
    )

    val dayEvents by remember(uiState.dateSelected, uiState.events) {
        derivedStateOf {
            uiState.events.filter {
                it.event.datetime.atZone(ZoneId.systemDefault())
                    .toLocalDate() == uiState.dateSelected
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = modifier
                    .align(Alignment.TopStart)
                    .fillMaxSize()
                    .background(colorScheme.background)
                    .padding(horizontal = 8.dp)
            ) {

                LazyColumn(
                    state = stateList,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {

                    item {

                        Column(
                            modifier = Modifier.background(colorScheme.background)
                        ) {
                            EventCalendar(
                                calendarState = calendarState,
                                events = uiState.events,
                                selectedDate = uiState.dateSelected,
                                onDateSelected = onDateSelected,
                                onNextMonth = {
                                    scope.launch {
                                        calendarState.animateScrollToMonth(it.plusMonths(1))
                                    }
                                },
                                onPreviousMonth = {
                                    scope.launch {
                                        calendarState.animateScrollToMonth(it.minusMonths(1))
                                    }
                                }
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Eventos para ${uiState.dateSelected.dayOfMonth} de ${
                                        uiState.dateSelected.month.getDisplayName(
                                            TextStyle.FULL,
                                            Locale.getDefault()
                                        )
                                    }",
                                    fontWeight = FontWeight.SemiBold,
                                    style = typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                TextButton(
                                    onClick = onNavigateToAllEvents
                                ) {

                                    Icon(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(end = 4.dp),
                                        imageVector = Icons.Default.Event,
                                        contentDescription = null
                                    )

                                    Text(
                                        text = "Ver todos",
                                        fontWeight = FontWeight.SemiBold,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                        }
                    }

                    if (dayEvents.isEmpty()) {

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 60.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EventBusy,
                                    contentDescription = null,
                                    tint = colorScheme.primary,
                                    modifier = Modifier
                                        .size(35.dp)
                                        .padding(bottom = 8.dp)
                                )

                                Text(
                                    text = stringResource(R.string.nenhum_evento_neste_dia),
                                    style = typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                                    fontWeight = FontWeight.Bold,
                                    color = colorScheme.onSurface
                                )

                                Text(
                                    text = stringResource(R.string.selecione_outra_data_no_calend_rio_para_visualizar_os_eventos),
                                    style = typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                                )
                            }
                        }
                    } else {

                        items(dayEvents, key = { it.event.id }) { event ->
                            EventCard(
                                event = event,
                                onSelectedEvent = {
                                    onSelectedEvent(it)
                                },
                                onCheckedStatusEvent = onCheckedStatusEvent
                            )
                        }
                    }

                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            }
        }
    }
}


@Composable
private fun EventCard(
    event: EventEndTypeEventDto,
    onSelectedEvent: (Int) -> Unit,
    onCheckedStatusEvent: (Int, Boolean) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography


    AppCardDefault(
        onSelected = {
            onSelectedEvent(event.event.id)
        }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer{
                    alpha =  if(event.event.completed) .5f else 1f
                }
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircleIndicator(
                color = event.typeEvent.color,
                size = 28.dp
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.event.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = event.event.datetime.toFormattedDateTime(pattern = "HH:mm"),
                        style = typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = colorScheme.primary
                    )
                }
            }

            Checkbox(
                checked = event.event.completed,
                onCheckedChange = {
                    onCheckedStatusEvent(event.event.id, it)
                }
            )

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EventScreenLightPreview() {
    TaskClassTheme(dynamicColor = false, darkTheme = false) {
        EventScreen(
            uiState = EventsUiState(),
            onCheckedStatusEvent = { id, isChecked -> },
            onDateSelected = {},
            onNavigateToAllEvents = {},
            onSelectedEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventScreenDarkPreview() {
    TaskClassTheme(dynamicColor = false, darkTheme = true) {
        EventScreen(
            uiState = EventsUiState(),
            onCheckedStatusEvent = { id, isChecked -> },
            onDateSelected = {},
            onNavigateToAllEvents = {},
            onSelectedEvent = {}
        )
    }
}
