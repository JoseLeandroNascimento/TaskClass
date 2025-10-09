package com.example.taskclass.events.presentation.eventsScreen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.core.data.model.EventEntity
import com.example.taskclass.ui.theme.TaskClassTheme
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

@Composable
fun EventsScreen(
    modifier: Modifier = Modifier,
    viewModel: EventsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    var isCalendarCompact by remember { mutableStateOf(false) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (source == NestedScrollSource.UserInput) {
                    if (available.y < 0) {
                        isCalendarCompact = true
                    }
                    if (available.y > 0 && listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0) {
                        isCalendarCompact = false
                    }
                }
                return Offset.Zero
            }
        }
    }

    val onDateSelected = remember { viewModel::filterByDate }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            uiState.isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(nestedScrollConnection),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)
                ) {
                    stickyHeader {
                        CalendarHeader(
                            selectedDate = uiState.selectedDate,
                            onDateSelected = onDateSelected, // Passando a referência estável
                            compactMode = isCalendarCompact,
                            events = uiState.events
                        )
                    }

                    if (uiState.events.isEmpty()) {
                        item {
                            Box(
                                Modifier
                                    .fillParentMaxSize()
                                    .padding(top = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Nenhum evento encontrado 😕",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        items(items = uiState.events, key = { it.id }) { event ->
                            EventCard(
                                title = event.title,
                                date = formatDate(event.date.value),
                                time = formatTime(event.time.minutes)
                            )
                        }

                        item { Spacer(modifier = Modifier.height(60.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarHeader(
    selectedDate: Calendar?,
    onDateSelected: (Calendar) -> Unit,
    compactMode: Boolean,
    events: List<EventEntity>
) {
    val today = remember { Calendar.getInstance() }
    var currentMonth by remember { mutableStateOf(today.clone() as Calendar) }

    val monthDays = remember(currentMonth, events) { generateMonthDays(currentMonth, events) }
    val weekDays = remember(selectedDate, today) { getWeekDates(selectedDate ?: today) }

    val onMonthChange = remember { { newMonth: Calendar -> currentMonth = newMonth } }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            // Header do mês só aparece no modo expandido
            if (!compactMode) {
                MonthHeader(currentMonth, onMonthChange) // Passa a função estável
                Spacer(Modifier.height(8.dp))
            }

            DaysOfWeekHeader()
            Spacer(Modifier.height(4.dp))

            if (compactMode) {
                // Semana compacta
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weekDays.forEach { dateCal ->
                        DayItemFlat(dateCal, today, selectedDate, onDateSelected)
                    }
                }
            } else {
                // Modo mensal completo
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                        .padding(top = 4.dp, bottom = 8.dp)
                ) {
                    MonthDaysGridFlat(monthDays, today, selectedDate, onDateSelected)
                }
            }
        }
    }
}

fun formatDate(dateInt: Int): String {
    if (dateInt <= 0) return ""
    val year = dateInt / 10000
    val month = (dateInt % 10000) / 100
    val day = dateInt % 100
    if (month !in 1..12) return "Data inválida"
    val monthName = DateFormatSymbols(Locale.getDefault()).shortMonths[month - 1]
    return "%02d %s %d".format(day, monthName.replaceFirstChar { it.uppercase() }, year)
}

fun formatTime(minutes: Int): String {
    val hour = minutes / 60
    val min = minutes % 60
    return "%02d:%02d".format(hour, min)
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
    val isPressed by interactionSource.collectIsPressedAsState()
    val isClickable = onClick != null

    val elevation by animateDpAsState(
        targetValue = if (isPressed && isClickable) 4.dp else 0.dp,
        label = "cardElevation"
    )

    val clickableModifier = if (isClickable) {
        Modifier.clickable(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
            onClick = onClick
        )
    } else {
        Modifier
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .then(clickableModifier),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = elevation,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            EventDetailRow(Icons.Outlined.CalendarToday, date)
            EventDetailRow(Icons.Outlined.Schedule, time)
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
fun MonthHeader(currentMonth: Calendar, onMonthChange: (Calendar) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Otimização: Usar remember para as lambdas de onClick para garantir estabilidade.
        val onPreviousMonth = remember(currentMonth, onMonthChange) {
            { onMonthChange((currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, -1) }) }
        }
        val onNextMonth = remember(currentMonth, onMonthChange) {
            { onMonthChange((currentMonth.clone() as Calendar).apply { add(Calendar.MONTH, 1) }) }
        }

        IconButton(
            onClick = onPreviousMonth,
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
            onClick = onNextMonth,
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
    val bgColorSelect =  MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
    val txtColorSelect = MaterialTheme.colorScheme.primary
    val txtColorToday = MaterialTheme.colorScheme.primary
    val txtColor = MaterialTheme.colorScheme.onSurfaceVariant

    // Otimização: Usa `derivedStateOf` para recalcular a cor apenas quando o estado `isSelected` mudar.
    val backgroundColor by remember(isSelected) {
        derivedStateOf {
            if (isSelected) bgColorSelect
            else Color.Transparent
        }
    }

    val textColor by remember(isSelected, isToday) {
       derivedStateOf {
            when {
                isSelected -> txtColorSelect
                isToday -> txtColorToday
                else -> txtColor
            }
        }
    }

    // Otimização: Garante que a lambda do `onClick` seja estável.
    val onClick = remember(dateCal, onDateSelected) { { onDateSelected(dateCal) } }

    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick),
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
    monthDays: List<DayWithEvents>,
    today: Calendar,
    selectedDate: Calendar?,
    onDateSelected: (Calendar) -> Unit
) {
    // Implementação de um grid. Exemplo com Column e Rows, mas um LazyVerticalGrid seria ideal
    // para meses muito grandes, embora não seja estritamente necessário para um calendário.
    val totalWeeks = (monthDays.size + 6) / 7
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (week in 0 until totalWeeks) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (dayOfWeek in 0..6) {
                    val index = week * 7 + dayOfWeek
                    if (index < monthDays.size) {
                        val day = monthDays[index]

                        // --- CORREÇÃO APLICADA AQUI ---
                        // Verifica se a data não é nula antes de renderizar o item do dia.
                        if (day.date != null) {
                            DayItemFlat(
                                dateCal = day.date, // Correto: usa a propriedade 'date'
                                today = today,
                                selectedDate = selectedDate,
                                onDateSelected = onDateSelected
                            )
                        } else {
                            // Renderiza um espaço vazio para dias fora do mês.
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    } else {
                        // Espaço para preencher o grid se necessário.
                        Spacer(modifier = Modifier.size(40.dp))
                    }
                }
            }
        }
    }
}


@Composable
private fun DayItemWithIndicator(
    day: DayWithEvents,
    today: Calendar,
    selectedDate: Calendar?,
    onDateSelected: (Calendar) -> Unit
) {
    val dateInt = day.date?.toIntDate() ?: 0
    val todayInt = today.toIntDate()
    val selectedInt = selectedDate?.toIntDate() ?: 0

    val isToday = dateInt == todayInt
    val isSelected = dateInt == selectedInt

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
            .clickable { day.date?.let(onDateSelected) },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = day.date?.get(Calendar.DAY_OF_MONTH)?.toString() ?: "",
                color = textColor,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            if (day.hasEvent) {
                Spacer(Modifier.height(2.dp))
                Box(
                    Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(MaterialTheme.colorScheme.primary)
                )
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

data class DayWithEvents(
    val date: Calendar?,
    val hasEvent: Boolean = false
)

private fun generateMonthDays(
    month: Calendar,
    events: List<EventEntity> = emptyList()
): List<DayWithEvents> {
    val tempCal = (month.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
    val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1
    val daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH)
    var dayCounter = 1 - firstDayOfWeek

    val daysList = mutableListOf<DayWithEvents>()

    while (dayCounter <= daysInMonth) {
        for (i in 0..6) {
            if (dayCounter in 1..daysInMonth) {
                val dayCal =
                    (month.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, dayCounter) }

                val dateInt = dayCal.toIntDate()
                val hasEvent = events.any { it.date.value == dateInt }

                daysList.add(DayWithEvents(dayCal, hasEvent))
            } else {
                daysList.add(DayWithEvents(null))
            }
            dayCounter++
        }
    }
    return daysList
}

private fun Calendar.toIntDate(): Int {
    val year = get(Calendar.YEAR)
    val month = get(Calendar.MONTH) + 1
    val day = get(Calendar.DAY_OF_MONTH)
    return year * 10000 + month * 100 + day
}

@Preview(showBackground = true)
@Composable
private fun EventsScreenLightPreview() {
    TaskClassTheme (
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
