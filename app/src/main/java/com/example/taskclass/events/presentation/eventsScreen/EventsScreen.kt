package com.example.taskclass.events.presentation.eventsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.core.data.model.dto.EventWithType
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
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    EventScreen(
        modifier = modifier,
        uiState = uiState,
        onDateSelected = viewModel::onDateSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    uiState: EventsUiState,
    onDateSelected: (LocalDate) -> Unit
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
            uiState.events.filter {
                val eventDate = LocalDate.of(
                    it.date.value / 10000,
                    (it.date.value % 10000) / 100,
                    it.date.value % 100
                )
                eventDate == uiState.dateSelected
            }
        }
    }

    Column(
        modifier = modifier
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
                    modifier = Modifier.background(color = MaterialTheme.colorScheme.background)
                ) {
                    CalendarSection(
                        modifier = Modifier
                            .background(colorScheme.background),
                        calendarState = calendarState,
                        events = uiState.events,
                        selectedDate = uiState.dateSelected,
                        onDateSelected = onDateSelected,
                        onNextMonth = { scope.launch { calendarState.scrollToMonth(it.plusMonths(1)) } },
                        onPreviousMonth = { scope.launch { calendarState.scrollToMonth(it.minusMonths(1)) } }
                    )

                    HorizontalDivider()

                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Eventos para ${uiState.dateSelected.dayOfMonth} de ${
                            uiState.dateSelected.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
                        }",
                        style = typography.titleSmall
                    )
                }
            }

            items(dayEvents.size) { index ->
                EventCard(event = dayEvents[index])
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
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
                val dayEvents = remember(events) {
                    events.filter {
                        val date = LocalDate.of(
                            it.date.value / 10000,
                            (it.date.value % 10000) / 100,
                            it.date.value % 100
                        )
                        date == day.date
                    }
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
    val bgColor = if (isSelected) colorScheme.primary.copy(alpha = 0.2f)
    else colorScheme.surfaceVariant.copy(alpha = 0.2f)

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
            events.take(3).forEach {
                Box(
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .height(3.dp)
                        .width(16.dp)
                        .background(it.color, RoundedCornerShape(2.dp))
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
private fun EventCard(event: EventWithType) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colorScheme.surfaceVariant)
            .clickable { /* TODO: navegação */ }
            .padding(vertical = 16.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(event.color, RoundedCornerShape(2.dp))
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = event.title,
                color = colorScheme.onSurface,
                style = typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            if (event.description.isNotEmpty()) {
                Text(
                    text = event.description,
                    color = colorScheme.onSurfaceVariant,
                    style = typography.bodySmall
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun EventScreenLightPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        EventScreen(
            uiState = EventsUiState(),
            onDateSelected = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventScreenDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        EventScreen(
            uiState = EventsUiState(),
            onDateSelected = {}
        )
    }
}
