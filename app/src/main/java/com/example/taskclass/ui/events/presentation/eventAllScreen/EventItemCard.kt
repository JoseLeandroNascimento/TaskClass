package com.example.taskclass.ui.events.presentation.eventAllScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.common.composables.AppCardDefault
import com.example.taskclass.common.composables.CircleIndicator
import com.example.taskclass.common.utils.toFormattedDateTime
import com.example.taskclass.ui.theme.TaskClassTheme
import java.time.Instant

@Composable
fun EventItemCard(
    title: String,
    color: Color,
    checked: Boolean,
    dateTime: Instant,
    onSelected: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {

    AppCardDefault(
        onSelected = onSelected
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(alpha = if (checked) .4f else 1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircleIndicator(color = color, size = 35.dp)

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "${dateTime.toFormattedDateTime("dd/MM/yyyy")} - ${
                            dateTime.toFormattedDateTime(
                                "HH:mm"
                            )
                        }",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
                    )
                }

                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventItemCardPreview() {
    TaskClassTheme(
        darkTheme = false,
        dynamicColor = false
    ) {
        EventItemCard(
            title = "Event 1",
            color = MaterialTheme.colorScheme.primary,
            checked = true,
            dateTime = Instant.now(),
            onSelected = {}
        ) { }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventItemCardDarkPreview() {
    TaskClassTheme(
        darkTheme = true,
        dynamicColor = false
    ) {
        EventItemCard(
            title = "Event 1",
            color = MaterialTheme.colorScheme.primary,
            checked = true,
            dateTime = Instant.now(),
            onSelected = {}
        ) { }
    }
}