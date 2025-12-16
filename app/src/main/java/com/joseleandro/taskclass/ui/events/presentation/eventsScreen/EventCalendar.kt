package com.joseleandro.taskclass.ui.events.presentation.eventsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun EventCalendar(
    modifier: Modifier = Modifier,
    calendarState: CalendarState,
    events: List<EventEndTypeEventDto>,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onNextMonth: (YearMonth) -> Unit,
    onPreviousMonth: (YearMonth) -> Unit
) {
    val shapeValue = 16.dp

    Column(
        modifier = modifier
            .shadow(
                elevation = .4.dp,
                shape = RoundedCornerShape(shapeValue)
            )
            .background(
                MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(shapeValue)
            )
            .padding(16.dp)


    ) {
        HorizontalCalendar(
            state = calendarState,
            dayContent = { day ->

                val dayEvents by remember(day.date, events) {
                    mutableStateOf(events.filter { it.event.datetime.atZone(ZoneId.systemDefault()).toLocalDate() == day.date })
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
    events: List<EventEndTypeEventDto>,
    selectedDate: LocalDate,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    val isSelected = day.date == selectedDate
    val bgColor = when {
        isSelected -> colorScheme.primary.copy(alpha = 0.3f)
        day.position != DayPosition.MonthDate -> colorScheme.surface.copy(alpha = .6f)
        else -> colorScheme.primary.copy(alpha = .04f)
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
                style = typography.labelSmall
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
                        .background(it.typeEvent.color, RoundedCornerShape(2.dp))
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
        IconButton(
            onClick = onPreviousMonth,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = "Mês anterior",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            text = "$monthTitle ${month.yearMonth.year}",
            color = colorScheme.onBackground,
            style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = onNextMonth,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f)
            )
        ) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Próximo mês",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventCalendarPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        EventCalendar(
            calendarState = rememberCalendarState(),
            onDateSelected = {},
            selectedDate = LocalDate.now(),
            events = emptyList(),
            onNextMonth = {},
            onPreviousMonth = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EventCalendarDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        EventCalendar(
            calendarState = rememberCalendarState(),
            onDateSelected = {},
            selectedDate = LocalDate.now(),
            events = emptyList(),
            onNextMonth = {},
            onPreviousMonth = {}
        )
    }
}


