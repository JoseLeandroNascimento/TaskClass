package com.example.taskclass.ui.events.presentation.eventAllScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.common.composables.AppCardDefault
import com.example.taskclass.common.composables.CircleIndicator
import com.example.taskclass.common.composables.SwipeContainer
import com.example.taskclass.common.utils.toFormattedDateTime
import com.example.taskclass.ui.theme.TaskClassTheme
import java.time.Instant

@Composable
fun EventItemCard(
    title: String,
    color: Color,
    typeEvent: String,
    checked: Boolean,
    dateTime: Instant,
    onSelected: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {

    SwipeContainer(
        modifier = Modifier.clip(RoundedCornerShape(14.dp)),
        onRemove = {},
        onToggleDone = {},
        startIcon = Icons.Default.Delete,
        endIcon = Icons.Default.Edit
    ) {

        AppCardDefault(onSelected = onSelected) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .alpha(if (checked) 0.45f else 1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CircleIndicator(
                    color = color,
                    size = 18.dp
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = dateTime.toFormattedDateTime("dd/MM/yyyy · HH:mm"),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = "•",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = typeEvent,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = color,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                    }
                }

                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline
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
            checked = false,
            dateTime = Instant.now(),
            typeEvent = "Prova",
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
            typeEvent = "Prova",
            onSelected = {}
        ) { }
    }
}