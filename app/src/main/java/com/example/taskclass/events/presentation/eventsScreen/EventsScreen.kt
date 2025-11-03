package com.example.taskclass.events.presentation.eventsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.core.data.model.dto.EventWithType
import com.example.taskclass.core.data.model.formatted
import com.example.taskclass.ui.theme.TaskClassTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    onSelectedEvent: (Int) -> Unit,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EventScreen(
        modifier = modifier,
        uiState = uiState,
        onSelectedEvent = {onSelectedEvent(it)},
        onDateSelected = viewModel::onDateSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    uiState: EventsUiState,
    onDateSelected: (LocalDate) -> Unit,
    onSelectedEvent: (Int) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

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
            uiState.events.filter { it.toLocalDate() == uiState.dateSelected }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.loadingEvents) {
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
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    stickyHeader {
                        Column(
                            modifier = Modifier.background(colorScheme.background)
                        ) {
                            CalendarSection(
                                modifier = Modifier
                                    .background(colorScheme.background),
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

                            HorizontalDivider()

                            Text(
                                modifier = Modifier.padding(vertical = 8.dp),
                                text = "Eventos para ${uiState.dateSelected.dayOfMonth} de ${
                                    uiState.dateSelected.month.getDisplayName(
                                        TextStyle.FULL,
                                        Locale.getDefault()
                                    )
                                }",
                                style = typography.titleSmall
                            )
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
                                    tint = colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(bottom = 8.dp)
                                )

                                Text(
                                    text = "Nenhum evento neste dia",
                                    style = typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                                    color = colorScheme.onSurface
                                )

                                Text(
                                    text = "Selecione outra data no calendário para visualizar os eventos.",
                                    style = typography.bodySmall,
                                    color = colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        items(dayEvents) { event ->
                            EventCard(event = event, onSelectedEvent = {
                                onSelectedEvent(it)
                            })
                        }
                    }

                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            }
        }
    }
}

@Composable
private fun CalendarSection(
    modifier: Modifier = Modifier,
    calendarState: com.kizitonwose.calendar.compose.CalendarState,
    events: List<EventWithType>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onNextMonth: (YearMonth) -> Unit,
    onPreviousMonth: (YearMonth) -> Unit
) {
    Column(modifier = modifier.padding(bottom = 16.dp)) {
        HorizontalCalendar(
            state = calendarState,
            dayContent = { day ->
                val dayEvents by remember(day.date, events) {
                    mutableStateOf(events.filter { it.toLocalDate() == day.date })
                }

                DayCell(
                    day = day,
                    events = dayEvents,
                    selectedDate = selectedDate,
                    onClick = { onDateSelected(day.date) }
                )
            },
            monthHeader = { month ->
                MonthHeader(
                    month = month,
                    onPreviousMonth = { onPreviousMonth(month.yearMonth) },
                    onNextMonth = { onNextMonth(month.yearMonth) }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun DayCell(
    day: CalendarDay,
    events: List<EventWithType>,
    selectedDate: LocalDate,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val isSelected = day.date == selectedDate
    val bgColor = when {
        isSelected -> colorScheme.primary.copy(alpha = 0.3f)
        day.position != DayPosition.MonthDate -> colorScheme.surfaceVariant.copy(alpha = 0.1f)
        else -> colorScheme.surface
    }

    val textColor = when {
        isSelected -> colorScheme.onPrimaryContainer
        day.position == DayPosition.MonthDate -> colorScheme.onBackground
        else -> colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .padding(2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .aspectRatio(1f)
            .clickable { onClick() },
        contentAlignment = Alignment.TopCenter
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day.date.dayOfMonth.toString(),
                color = textColor,
                style = typography.bodyMedium
            )

            val maxDots = 3
            val visibleEvents = events.take(maxDots)
            val remaining = events.size - maxDots

            visibleEvents.forEach {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .height(3.dp)
                        .width(16.dp)
                        .background(it.color, RoundedCornerShape(2.dp))
                )
            }

            if (remaining > 0) {
                Text(
                    text = "+$remaining",
                    style = typography.labelSmall,
                    color = colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun MonthHeader(
    month: CalendarMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val monthTitle = month.yearMonth.month
        .getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = "Mês anterior")
        }

        Text(
            text = "$monthTitle ${month.yearMonth.year}",
            color = colorScheme.onBackground,
            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        IconButton(onClick = onNextMonth) {
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = "Próximo mês")
        }
    }
}

@Composable
private fun EventCard(
    event: EventWithType,
    onSelectedEvent: (Int) -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelectedEvent(event.id) },
        color = colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador de cor discreto
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(event.color, RoundedCornerShape(50))
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Título
                Text(
                    text = event.title,
                    style = typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = colorScheme.onSurface
                )

                // Descrição (máximo 1 linha)
                if (event.description.isNotBlank()) {
                    Text(
                        text = event.description,
                        style = typography.bodySmall,
                        color = colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }

                // Hora do evento
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(end = 4.dp)
                    )
                    Text(
                        text = event.time.formatted(),
                        style = typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        color = colorScheme.primary
                    )
                }
            }
        }
    }
}

// ---------- Função utilitária de data ----------

private fun EventWithType.toLocalDate(): LocalDate {
    val y = date.value / 10000
    val m = (date.value % 10000) / 100
    val d = date.value % 100
    return LocalDate.of(y, m, d)
}

// ---------- Previews ----------

@Preview(showBackground = true)
@Composable
private fun EventScreenLightPreview() {
    TaskClassTheme(dynamicColor = false, darkTheme = false) {
        EventScreen(
            uiState = EventsUiState(),
            onDateSelected = {},
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
            onDateSelected = {},
            onSelectedEvent = {}
        )
    }
}
