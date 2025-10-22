package com.example.taskclass.events.presentation.eventsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.taskclass.ui.theme.TaskClassTheme
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

data class Event(
    val id: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val color: Color
)

@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    EventScreen(
        modifier = modifier,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(
    modifier: Modifier = Modifier,
    uiState: EventsUiState,
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(6) }
    val endMonth = remember { currentMonth.plusMonths(6) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    val events = listOf(
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(1, "Reunião", "Equipe de design", LocalDate.now(), Color(0xFF4CAF50)),
        Event(2, "Entrega do projeto", "Sprint 12", LocalDate.now().plusDays(1), Color(0xFFFF9800)),
        Event(3, "Dentista", "Consulta", LocalDate.now().plusDays(3), Color(0xFFF44336))
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
            .padding(horizontal = 8.dp)
    ) {
        val daysOfWeek = remember { daysOfWeek() }

        HorizontalCalendar(
            state = rememberCalendarState(
                startMonth = startMonth,
                endMonth = endMonth,
                firstVisibleMonth = currentMonth,
                firstDayOfWeek = daysOfWeek.first()
            ),
            dayContent = { day ->
                DayCell(
                    day = day,
                    events = events.filter { it.date == day.date },
                    selectedDate = selectedDate,
                    onClick = { selectedDate = day.date }
                )
            },
            monthHeader = { month ->
                MonthHeader(month)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        val dayEvents = remember(selectedDate, events) {
            events.filter { it.date == selectedDate }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dayEvents.size) { index ->
                EventCard(dayEvents[index], {})
            }
        }
    }
}

@Composable
fun DayCell(
    day: CalendarDay,
    events: List<Event>,
    selectedDate: LocalDate,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val isSelected = day.date == selectedDate
    val bgColor = if (isSelected)
        colorScheme.primary.copy(alpha = 0.4f)
    else
        colorScheme.surfaceVariant

    val textColor = when {
        isSelected -> colorScheme.onPrimaryContainer
        day.position == DayPosition.MonthDate -> colorScheme.onBackground
        else -> colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() }
            .padding(2.dp)
            .background(bgColor, RoundedCornerShape(6.dp)),
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
fun MonthHeader(month: CalendarMonth) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val monthTitle = month.yearMonth.month
        .getDisplayName(TextStyle.FULL, Locale.getDefault())
        .replaceFirstChar { it.titlecase(Locale.getDefault()) }

    Text(
        text = "$monthTitle ${month.yearMonth.year}",
        color = colorScheme.onBackground,
        style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun EventCard(event: Event, onClick: (Event) -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(8.dp))
            .background(colorScheme.surfaceVariant)
            .clickable { onClick(event) }
            .padding(vertical = 8.dp, horizontal = 12.dp),
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
            Text(
                text = event.description,
                color = colorScheme.onSurfaceVariant,
                style = typography.bodySmall
            )
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
            uiState = EventsUiState()
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
            uiState = EventsUiState()
        )
    }
}
