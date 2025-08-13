package com.example.taskclass.events

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskclass.ui.theme.TaskClassTheme
import java.text.DateFormatSymbols
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(modifier: Modifier = Modifier) {
    var selectedDate by remember { mutableStateOf<Calendar?>(null) }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            AppCalendar(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                item {
                    EventCard(
                        title = "Reunião de Projeto",
                        date = "12 Ago 2025",
                        time = "14:00 - 15:00",
                    )
                }
                item {
                    EventCard(
                        title = "Consulta Médica",
                        date = "15 Ago 2025",
                        time = "09:00",
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(
    title: String,
    date: String,
    time: String,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "$date • $time",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
//
        }
    }
}

@Composable
fun AppCalendar(
    modifier: Modifier = Modifier,
    selectedDate: Calendar?,
    onDateSelected: (Calendar) -> Unit
) {
    val today = Calendar.getInstance()
    var currentMonth by remember { mutableStateOf(today.clone() as Calendar) }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 3.dp
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    currentMonth = (currentMonth.clone() as Calendar).apply {
                        add(Calendar.MONTH, -1)
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Mês anterior")
                }

                val monthName = DateFormatSymbols().months[currentMonth.get(Calendar.MONTH)]
                Text(
                    text = "$monthName ${currentMonth.get(Calendar.YEAR)}",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )

                IconButton(onClick = {
                    currentMonth = (currentMonth.clone() as Calendar).apply {
                        add(Calendar.MONTH, 1)
                    }
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Próximo mês")
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb").forEach {
                    Text(
                        it,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            val tempCal = (currentMonth.clone() as Calendar).apply { set(Calendar.DAY_OF_MONTH, 1) }
            val firstDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1
            val daysInMonth = currentMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
            var dayCounter = 1 - firstDayOfWeek

            while (dayCounter <= daysInMonth) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (i in 0..6) {
                        if (dayCounter in 1..daysInMonth) {
                            val dateCal = (currentMonth.clone() as Calendar).apply {
                                set(Calendar.DAY_OF_MONTH, dayCounter)
                            }

                            val isToday = dateCal.isSameDay(today)
                            val isSelected = selectedDate?.isSameDay(dateCal) == true

                            val backgroundColor by animateColorAsState(
                                targetValue = when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    isToday -> MaterialTheme.colorScheme.secondaryContainer
                                    else -> MaterialTheme.colorScheme.surface
                                },
                                label = "dayBackgroundColor"
                            )

                            val textColor = when {
                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                isToday -> MaterialTheme.colorScheme.onSecondaryContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .clickable { onDateSelected(dateCal) },
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = backgroundColor,
                                    tonalElevation = if (isSelected) 4.dp else 0.dp
                                ) {
                                    Box(
                                        modifier = Modifier.size(36.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = dayCounter.toString(),
                                            color = textColor,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(Modifier.size(42.dp))
                        }
                        dayCounter++
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

@Preview(showBackground = true)
@Composable
private fun EventsScreenPreview() {
    TaskClassTheme(darkTheme = false) {
        EventsScreen()
    }
}