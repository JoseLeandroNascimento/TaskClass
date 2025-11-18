package com.example.taskclass.ui.agenda.presentation

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.data.Resource
import com.example.taskclass.common.utils.parseHors
import com.example.taskclass.common.utils.toFormattedTime
import com.example.taskclass.common.utils.toTotalMinutes
import com.example.taskclass.core.data.model.dto.ScheduleAndDisciplineDTO
import java.util.Calendar

val dayHeaderHeight = 64.dp
val timelineWidth = 30.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    viewModel: AgendaViewModel,
    onEditSchedule: (Int) -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val resource = LocalContext.current.resources
    val daysOfWeek = resource.getStringArray(R.array.dias_da_semana_abreviado)

    uiState.selectItem?.let { itemSelected ->
        ScheduleDetailsDialog(
            schedule = itemSelected,
            onDismiss = { viewModel.resetItemSelected() },
            onEdit = {
                val id = uiState.selectItem.schedule.id
                viewModel.resetItemSelected()
                onEditSchedule(id)
            },
            onDelete = {
                viewModel.deleteSchedule()
            }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.updateDayOfWeek()
    }

    when (uiState.schedules) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            val firstEvent = uiState.schedules.data.minByOrNull { it.schedule.startTime }
            val scrollGrid = rememberScrollState()

            BoxWithConstraints {

                val startHors = 0
                val hours = startHors..23

                val rowHeight = maxOf(maxHeight / hours.count(), 49.dp)
                val dpPerMinute = rowHeight / 60f

                val density = LocalDensity.current
                val pxPerMinute = with(density) { dpPerMinute.toPx() }

                LaunchedEffect(firstEvent, pxPerMinute) {
                    firstEvent?.let {
                        val minutesFromTop = (it.schedule.startTime.toTotalMinutes()).coerceAtLeast(0)
                        val offsetPx = minutesFromTop * pxPerMinute
                        scrollGrid.animateScrollTo(
                            offsetPx.toInt(),
                            animationSpec = tween(durationMillis = 500)
                        )
                    }
                }

                Surface(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.background
                ) {

                    Column {

                        ScheduleGridHeader(
                            uiState = uiState
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollGrid)
                        ) {
                            Column {
                                hours.forEach { hour ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(rowHeight)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(timelineWidth)
                                                .fillMaxHeight(),
                                            contentAlignment = Alignment.TopCenter
                                        ) {
                                            Text(
                                                text = hour.parseHors(),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 9.sp,
                                                lineHeight = 10.sp,
                                                modifier = Modifier
                                            )
                                        }

                                        daysOfWeek.forEachIndexed { index, _ ->
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = if (index == uiState.currentDayOfWeek) MaterialTheme.colorScheme.primary.copy(
                                                            alpha = 0.1f
                                                        ) else MaterialTheme.colorScheme.background
                                                    )
                                                    .weight(1f)
                                                    .fillMaxHeight()
                                                    .border(
                                                        0.5.dp,
                                                        Color.LightGray.copy(alpha = .1f)
                                                    )

                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(80.dp))
                            }

                            BoxWithConstraints {

                                val boxEventWidth = (maxWidth - timelineWidth) / 7

                                uiState.schedules.data.forEachIndexed { _, event ->

                                    val xPositionWeek =
                                        timelineWidth + event.schedule.dayWeek * boxEventWidth
                                    val yPositionWeek =
                                        rowHeight * ((event.schedule.startTime.toTotalMinutes() / 60f) - startHors)

                                    val eventDurationMinutes =
                                        event.schedule.endTime.toTotalMinutes() - event.schedule.startTime.toTotalMinutes()
                                    val heightBox = eventDurationMinutes * dpPerMinute


                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .offset(x = xPositionWeek, y = yPositionWeek)
                                            .width(boxEventWidth)
                                            .height(heightBox.coerceAtLeast(40.dp))
                                            .padding(horizontal = 2.dp, vertical = 2.dp)
                                            .background(
                                                color = event.discipline.color,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(2.dp)
                                            .clickable {
                                                viewModel.selectedItem(event)
                                            }
                                    ) {
                                        Text(
                                            text = event.discipline.title,
                                            style = MaterialTheme.typography.labelSmall,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 10.sp
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        is Resource.Error -> {

        }
    }

}

@Composable
fun ScheduleDetailsDialog(
    schedule: ScheduleAndDisciplineDTO,
    onDismiss: () -> Unit,
    onEdit: (ScheduleAndDisciplineDTO) -> Unit,
    onDelete: (ScheduleAndDisciplineDTO) -> Unit
) {

    val context = LocalContext.current
    val daysOfWeek = context.resources.getStringArray(R.array.dias_da_semana_completo)

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = schedule.discipline.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Horário",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${schedule.schedule.startTime.toFormattedTime()} - ${
                            schedule.schedule.endTime.toFormattedTime()
                        }",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Dia",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Dia da semana: ${daysOfWeek[schedule.schedule.dayWeek]}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,

                        )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ações
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(onClick = { onEdit(schedule) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                    TextButton(
                        onClick = { onDelete(schedule) },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Excluir")
                    }
                }
            }
        }
    }
}

fun getCurrentWeekDaysWithMonth(timeCurrent: Long): List<Int> {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = timeCurrent
    }
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    calendar.add(Calendar.DAY_OF_MONTH, -dayOfWeek + 1)

    return List(7) { i ->
        if (i > 0) calendar.add(Calendar.DAY_OF_MONTH, 1)
        calendar.get(Calendar.DAY_OF_MONTH)
    }
}

@Composable
fun ScheduleGridHeader(
    modifier: Modifier = Modifier,
    uiState: AgendaUiState
) {

    val context = LocalContext.current
    val daysOfWeek = context.resources.getStringArray(R.array.dias_da_semana_abreviado)

    val currentTime = System.currentTimeMillis()
    val daysWeek = getCurrentWeekDaysWithMonth(currentTime)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(dayHeaderHeight)
    ) {
        Spacer(
            modifier = Modifier
                .width(timelineWidth)
                .height(dayHeaderHeight)
                .background(color = MaterialTheme.colorScheme.surface)
        )
        daysOfWeek.forEachIndexed { index, day ->

            Column(
                modifier = Modifier
                    .background(
                        color = if (index == uiState.currentDayOfWeek) MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.2f
                        ) else MaterialTheme.colorScheme.surface
                    )
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val colorText =
                    if (index == 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                Text(
                    text = daysWeek[index].toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = colorText,
                    lineHeight = 12.sp
                )
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelMedium,
                    color = colorText
                )
            }
        }
    }

}
