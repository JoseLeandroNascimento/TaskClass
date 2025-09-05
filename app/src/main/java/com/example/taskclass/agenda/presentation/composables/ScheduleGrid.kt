package com.example.taskclass.agenda.presentation.composables

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.taskclass.R
import com.example.taskclass.agenda.presentation.AgendaUiState
import com.example.taskclass.common.data.Resource
import com.example.taskclass.common.utils.parseHors
import com.example.taskclass.ui.theme.TaskClassTheme
import java.util.Calendar


val dayHeaderHeight = 64.dp
val timelineWidth = 30.dp

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ScheduleGrid(
    modifier: Modifier = Modifier,
    uiState: AgendaUiState
) {

    val context = LocalContext.current
    val daysOfWeek = context.resources.getStringArray(R.array.dias_da_semana_abreviado)

    when (uiState.schedules) {
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is Resource.Success -> {
            val firstEvent = uiState.schedules.data.minByOrNull { it.startTime }
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
                        val minutesFromTop = (it.startTime).coerceAtLeast(0)
                        val offsetPx = minutesFromTop * pxPerMinute
                        scrollGrid.animateScrollTo(
                            offsetPx.toInt(),
                            animationSpec = tween(durationMillis = 500)
                        )
                    }
                }

                Surface(
                    modifier = modifier,
                    color = MaterialTheme.colorScheme.surface
                ) {

                    Column {

                        ScheduleGridHeader()

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
                                                fontSize = 8.sp,
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }

                                        daysOfWeek.forEachIndexed { index,_ ->
                                            Box(
                                                modifier = Modifier
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

                                uiState.schedules.data.forEachIndexed { index, event ->

                                    val xPositionWeek = timelineWidth + event.dayWeek * boxEventWidth
                                    val yPositionWeek =
                                        rowHeight * ((event.startTime / 60f) - startHors)

                                    val eventDurationMinutes = event.endTime - event.startTime
                                    val heightBox = eventDurationMinutes * dpPerMinute


                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .offset(x = xPositionWeek, y = yPositionWeek)
                                            .width(boxEventWidth)
                                            .height(heightBox.coerceAtLeast(40.dp))
                                            .padding(horizontal = 2.dp, vertical = 2.dp)
                                            .background(
                                                color =event.color,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(2.dp)
                                    ) {
                                        Text(
                                            text = event.disciplineTitle,
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
fun ScheduleGridHeader(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val daysOfWeek = context.resources.getStringArray(R.array.dias_da_semana_abreviado)

    val currentTime = System.currentTimeMillis()
    val daysWeek = getCurrentWeekDaysWithMonth(currentTime)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dayHeaderHeight)
    ) {
        Spacer(modifier = Modifier.width(timelineWidth))
        daysOfWeek.forEachIndexed { index, day ->

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = daysWeek[index].toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    lineHeight = 12.sp
                )
                Text(text = day, style = MaterialTheme.typography.labelMedium)
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun ScheduleGridPreview() {
    TaskClassTheme(
        dynamicColor = true,
        darkTheme = false
    ) {
        ScheduleGrid(uiState = AgendaUiState())
    }
}