package com.example.taskclass.events.presentation.eventsScreen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    date: Calendar,
    selectedDate: Calendar?,
    onDateChange: (Calendar) -> Unit,
    onDayClick: (Calendar) -> Unit
) {
    // 💡 Otimização: Usar a mesma instância de SimpleDateFormat para evitar recriação.
    val monthFormat = remember { SimpleDateFormat("MMMM yyyy", Locale.getDefault()) }

    // 🚀 Otimização: `derivedStateOf` garante que o cálculo só ocorra quando `date` mudar.
    val monthTitle by remember {
        derivedStateOf {
            monthFormat.format(date.time).replaceFirstChar { it.uppercase() }
        }
    }
    val daysInMonth by remember { derivedStateOf { generateDaysForMonth(date) } }
    val today = remember { Calendar.getInstance() }

    Surface(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    val newDate = date.clone() as Calendar
                    newDate.add(Calendar.MONTH, -1)
                    onDateChange(newDate)
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Mês anterior"
                    )
                }
                Text(
                    text = monthTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                    val newDate = date.clone() as Calendar
                    newDate.add(Calendar.MONTH, 1)
                    onDateChange(newDate)
                }) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Próximo mês"
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Cabeçalho dos dias da semana
            // 🚀 Otimização: Recalcula apenas se a localidade do dispositivo mudar.
            val weekDays = remember(Locale.getDefault()) {
                val weekDayFormat = SimpleDateFormat("E", Locale.getDefault())
                val cal = Calendar.getInstance().apply { firstDayOfWeek = Calendar.SUNDAY }
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                (0..6).map {
                    val dayName = weekDayFormat.format(cal.time)
                    cal.add(Calendar.DAY_OF_WEEK, 1)
                    dayName.first().toString()
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                weekDays.forEach { day ->
                    Text(
                        text = day,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Corpo do calendário
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // `items` já é otimizado por padrão para listas estáveis.
                // Usar uma chave (`key`) ajuda o Compose a identificar os itens de forma única.
                items(daysInMonth, key = { it.timeInMillis }) { calendarDay ->
                    val isFromCurrentMonth =
                        calendarDay.get(Calendar.MONTH) == date.get(Calendar.MONTH)

                    if (isFromCurrentMonth) {
                        val isToday = isSameDay(calendarDay, today)
                        val isSelected = selectedDate?.let { isSameDay(calendarDay, it) } ?: false

                        // Animações e cores são eficientes com `animateColorAsState`.
                        val backgroundColor by animateColorAsState(
                            targetValue = when {
                                isSelected -> MaterialTheme.colorScheme.primary
                                isToday -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                else -> MaterialTheme.colorScheme.surface
                            },
                            label = "bgColorAnim"
                        )

                        val textColor = when {
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            else -> MaterialTheme.colorScheme.onSurface
                        }

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(CircleShape)
                                .background(backgroundColor)
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    onDayClick(calendarDay)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = calendarDay.get(Calendar.DAY_OF_MONTH).toString(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                    color = textColor
                                )
                            )
                        }
                    } else {
                        // Placeholder mantém a grade estável, evitando "saltos" no layout.
                        Box(modifier = Modifier.aspectRatio(1f))
                    }
                }
            }
        }
    }
}

// 🔧 Funções auxiliares
private fun generateDaysForMonth(calendar: Calendar): List<Calendar> {
    val tempCalendar = calendar.clone() as Calendar
    tempCalendar.set(Calendar.DAY_OF_MONTH, 1)

    val days = mutableListOf<Calendar>()
    // Ajusta para o início da semana (Domingo)
    val firstDayOfMonthInWeek = tempCalendar.get(Calendar.DAY_OF_WEEK)
    tempCalendar.add(Calendar.DAY_OF_MONTH, -(firstDayOfMonthInWeek - Calendar.SUNDAY))

    // O calendário sempre exibe 6 semanas (42 dias) para ter uma altura consistente
    repeat(42) {
        days.add(tempCalendar.clone() as Calendar)
        tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
    }

    return days
}

private fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
    return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
}

@Preview(showBackground = true, name = "Light Mode")
@Composable
private fun CalendarLightPreview() {
    var date by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDate by remember { mutableStateOf<Calendar?>(Calendar.getInstance()) }

    TaskClassTheme(darkTheme = false) {
        Calendar(
            date = date,
            selectedDate = selectedDate,
            onDateChange = { date = it },
            onDayClick = { selectedDate = it }
        )
    }
}

@Preview(showBackground = true, name = "Dark Mode")
@Composable
private fun CalendarDarkPreview() {
    var date by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }

    // Simula a seleção de um dia no futuro para teste visual
    LaunchedEffect(Unit) {
        val futureDate = Calendar.getInstance()
        futureDate.add(Calendar.DAY_OF_MONTH, 5)
        selectedDate = futureDate
    }

    TaskClassTheme(darkTheme = true) {
        Calendar(
            date = date,
            selectedDate = selectedDate,
            onDateChange = { date = it },
            onDayClick = { selectedDate = it }
        )
    }
}