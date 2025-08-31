package com.example.taskclass.agenda.composables

import android.annotation.SuppressLint
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.example.taskclass.common.utils.parseHors
import com.example.taskclass.ui.theme.TaskClassTheme
import java.util.Calendar

data class ScheduleEvent(
    val dayOfWeek: Int,
    val startMinutes: Int, // ex: 6*60 + 30 = 390
    val durationMinutes: Int,
    val title: String,
    val color: Color = Color(0xFF90CAF9)
)

val sampleEvents = listOf(
    ScheduleEvent(
        dayOfWeek = 1,
        startMinutes = 7 * 60 + 30,    // 07:30
        durationMinutes = 90,
        title = "Matemática",
        color = Color(0xFF2979FF) // Azul vivo
    ),
    ScheduleEvent(
        dayOfWeek = 1,
        startMinutes = 10 * 60,         // 10:00
        durationMinutes = 60,
        title = "História",
        color = Color(0xFF9C27B0) // Roxo vibrante
    ),
    ScheduleEvent(
        dayOfWeek = 1,
        startMinutes = 14 * 60 + 30,    // 14:30
        durationMinutes = 45,
        title = "Inglês",
        color = Color(0xFF4CAF50) // Verde vivo
    ),
    ScheduleEvent(
        dayOfWeek = 2,
        startMinutes = 8 * 60,          // 08:00
        durationMinutes = 90,
        title = "Química",
        color = Color(0xFFFFC107) // Amarelo forte
    ),
    ScheduleEvent(
        dayOfWeek = 2,
        startMinutes = 13 * 60 + 15,    // 13:15
        durationMinutes = 75,
        title = "Educação Física",
        color = Color(0xFFFF5722) // Laranja vibrante
    ),
    ScheduleEvent(
        dayOfWeek = 3,
        startMinutes = 9 * 60 + 45,     // 09:45
        durationMinutes = 60,
        title = "Geografia",
        color = Color(0xFF673AB7) // Índigo intenso
    ),
    ScheduleEvent(
        dayOfWeek = 3,
        startMinutes = 16 * 60,         // 16:00
        durationMinutes = 30,
        title = "Redação",
        color = Color(0xFFFFEB3B) // Amarelo vibrante
    ),
    ScheduleEvent(
        dayOfWeek = 4,
        startMinutes = 11 * 60,         // 11:00
        durationMinutes = 60,
        title = "Biologia",
        color = Color(0xFF009688) // Verde água forte
    ),
    ScheduleEvent(
        dayOfWeek = 5,
        startMinutes = 7 * 60 + 15,          // 07:00
        durationMinutes = 60,
        title = "Sociologia",
        color = Color(0xFFF44336) // Vermelho vivo
    ),
    ScheduleEvent(
        dayOfWeek = 6,
        startMinutes = 10 * 60 + 30,    // 10:30
        durationMinutes = 90,
        title = "Filosofia",
        color = Color(0xFF607D8B) // Azul acinzentado forte
    )
)

val dayHeaderHeight = 64.dp
val timelineWidth = 40.dp
val daysOfWeek = listOf("dom", "seg", "ter", "qua", "qui", "sex", "sáb")

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun ScheduleGrid(
    modifier: Modifier = Modifier,
    events: List<ScheduleEvent>
) {

    BoxWithConstraints {

        val firstEvent = events.sortedBy { it.startMinutes }
        val startHors = if (firstEvent.isEmpty()) 0 else (firstEvent[0].startMinutes / 60)

        val hours = startHors..23

        val rowHeight = maxOf(maxHeight / hours.count(), 49.dp)

        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.surface
        ) {

            Column {

                ScheduleGridHeader()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
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
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }

                                daysOfWeek.forEach { _ ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            .border(0.5.dp, Color.LightGray.copy(alpha = .1f))
                                    )
                                }
                            }
                        }
                    }

                    BoxWithConstraints {

                        val boxEventWidth = (maxWidth - timelineWidth) / 7

                        events.forEachIndexed { index, event ->

                            val xPositionWeek = timelineWidth + event.dayOfWeek * boxEventWidth
                            val yPositionWeek = rowHeight * ((event.startMinutes / 60f) - startHors)
                            val heightBox =
                                boxEventWidth * (((event.startMinutes / 60f) + (event.durationMinutes / 60f)) - (event.startMinutes / 60f))

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .offset(x = xPositionWeek, y = yPositionWeek)
                                    .width(boxEventWidth)
                                    .height(heightBox)
                                    .padding(horizontal = 2.dp)
                                    .background(event.color, shape = RoundedCornerShape(4.dp))
                            ) {
                                Text(
                                    text = event.title,
                                    fontSize = 10.sp,
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
        ScheduleGrid(events = sampleEvents)
    }
}