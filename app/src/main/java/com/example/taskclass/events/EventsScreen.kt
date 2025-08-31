package com.example.taskclass.events

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskclass.ui.theme.TaskClassTheme
import java.text.DateFormatSymbols
import java.util.Calendar


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EventsScreen(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }
    val listState = rememberLazyListState()
    val compactThresholdPx = 40
    val isCalendarCompact by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > compactThresholdPx
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            stickyHeader {
                Surface {
                    Column(
                        modifier = Modifier.padding(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        AppCalendar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background),
                            selectedDate = selectedDate,
                            onDateSelected = { selectedDate = it },
                            compactMode = isCalendarCompact
                        )
                        Text(text = "Proximos eventos", style = MaterialTheme.typography.titleMedium)
                    }
                }

            }


            items(10) { index ->
                EventCard(
                    modifier = Modifier
                        .fillMaxWidth(),
                    title = "Evento $index",
                    date = "12 Ago 2025",
                    time = "14:00 - 15:00"
                )
            }

            item {
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }
}

@Composable
fun EventCard(
    modifier: Modifier = Modifier,
    title: String,
    date: String,
    time: String,
    onClick: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isClickable = onClick != null
    val elevation by animateDpAsState(
        if (interactionSource.collectIsPressedAsState().value && isClickable) 2.dp else 0.dp,
        label = "cardElevation"
    )

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .then(
                if (isClickable) Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = { onClick.invoke() }
                ) else Modifier),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = elevation,
        tonalElevation = 1.dp,
        border = if (!isClickable) null else BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.15.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            EventDetailRow(
                icon = Icons.Outlined.CalendarToday,
                text = date
            )

            EventDetailRow(
                icon = Icons.Outlined.Schedule,
                text = time
            )
        }
    }
}

@Composable
private fun EventDetailRow(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.size(14.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.9f)
        )
    }
}

@Composable
fun AppCalendar(
    modifier: Modifier = Modifier,
    selectedDate: Calendar?,
    onDateSelected: (Calendar) -> Unit,
    compactMode: Boolean = false
) {
    val today = remember { Calendar.getInstance() }
    var currentMonth by remember { mutableStateOf(today.clone() as Calendar) }

    // Datas calculadas apenas quando realmente mudam
    val monthDays = remember(currentMonth) {
        generateMonthDays(currentMonth)
    }
    val weekDays = remember(selectedDate) {
        getWeekDates(selectedDate ?: today)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {

            if (!compactMode) {
                MonthHeader(currentMonth) {
                    currentMonth = it
                }
                Spacer(Modifier.height(12.dp))
            }

            DaysOfWeekHeader()
            Spacer(Modifier.height(8.dp))

            // Transição suave e leve
            Crossfade(
                targetState = compactMode,
                animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing),
                label = "calendarCrossfade"
            ) { isCompact ->
                if (isCompact) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        weekDays.forEach { dateCal ->
                            DayItemFlat(dateCal, today, selectedDate, onDateSelected)
                        }
                    }
                } else {
                    MonthDaysGridFlat(monthDays, today, selectedDate, onDateSelected)
                }
            }
        }
    }
}

@Composable
fun MonthHeader(currentMonth: Calendar, onMonthChange: (Calendar) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onMonthChange((currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) })
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                Icons.Filled.ChevronLeft,
                contentDescription = "Mês anterior",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }

        val monthName = DateFormatSymbols().months[currentMonth.get(Calendar.MONTH)]
        Text(
            text = "$monthName ${currentMonth.get(Calendar.YEAR)}",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onSurface
        )

        IconButton(
            onClick = {
                onMonthChange((currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) })
            },
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                Icons.Filled.ChevronRight,
                contentDescription = "Próximo mês",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun DaysOfWeekHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb").forEach {
            Text(
                it,
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun DayItemFlat(
    dateCal: Calendar,
    today: Calendar,
    selectedDate: Calendar?,
    onDateSelected: (Calendar) -> Unit
) {
    val isToday = dateCal.isSameDay(today)
    val isSelected = selectedDate?.isSameDay(dateCal) == true

    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textColor = when {
        isSelected -> MaterialTheme.colorScheme.primary
        isToday -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable { onDateSelected(dateCal) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dateCal.get(Calendar.DAY_OF_MONTH).toString(),
            color = textColor,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
fun MonthDaysGridFlat(
    monthDays: List<Calendar?>,
    today: Calendar,
    selectedDate: Calendar?,
    onDateSelected: (Calendar) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        monthDays.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                week.forEach { day ->
                    if (day != null) {
                        DayItemFlat(day, today, selectedDate, onDateSelected)
                    } else {
                        Spacer(Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}


private fun Calendar.isSameDay(other: Calendar): Boolean {
    return get(Calendar.YEAR) == other.get(Calendar.YEAR) &&
            get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR)
}

private fun getWeekDates(date: Calendar): List<Calendar> {
    val start = (date.clone() as Calendar).apply {
        set(Calendar.DAY_OF_WEEK, firstDayOfWeek)
    }
    return List(7) { i ->
        (start.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, i) }
    }
}

private fun generateMonthDays(month: Calendar): List<Calendar?> {
    val tempCal = (month.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
    val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
    var dayCounter = 1 - firstDayOfWeek

    val daysList = mutableListOf<Calendar?>()
    while (dayCounter <= daysInMonth) {
        for (i in 0..6) {
            if (dayCounter in 1..daysInMonth) {
                daysList.add((month.clone() as Calendar).apply {
                    set(Calendar.DAY_OF_MONTH, dayCounter)
                })
            } else {
                daysList.add(null)
            }
            dayCounter++
        }
    }
    return daysList
}

@Preview(showBackground = true)
@Composable
private fun EventsScreenLightPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        EventsScreen()
    }
}

@Preview
@Composable
private fun EventsScreenDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        EventsScreen()
    }
}
