package com.joseleandro.taskclass.ui.events.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.common.composables.CircleIndicator
import com.joseleandro.taskclass.core.data.model.dto.EventEndTypeEventDto
import com.joseleandro.taskclass.core.data.model.entity.EventEntity
import com.joseleandro.taskclass.core.data.model.entity.TypeEventEntity
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import java.time.Instant

@Composable
fun EventSearchItemCard(
    modifier: Modifier = Modifier,
    onNavigation: ((Int) -> Unit)? = null,
    event: EventEndTypeEventDto
) {

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = event.event.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    CircleIndicator(
                        color = event.typeEvent.color, size = 18.dp
                    )

                    Text(
                        text = event.typeEvent.name,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold
                    )
                }

            }

            IconButton(
                onClick = {
                    onNavigation?.invoke(event.event.id)
                }) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun EventSearchItemCardPreview() {
    TaskClassTheme(
        dynamicColor = false, darkTheme = false
    ) {
        EventSearchItemCard(
            event = EventEndTypeEventDto(
                event = EventEntity(
                    title = "Evento de teste",
                    typeEventId = 1,
                    description = "",
                    datetime = Instant.now(),
                    completed = false,
                    updatedAt = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis(),
                ), typeEvent = TypeEventEntity(
                    color = MaterialTheme.colorScheme.primary,
                    name = "Prova",
                    updatedAt = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis(),
                )
            )
        )
    }
}

@Preview
@Composable
private fun EventSearchItemCardDarkPreview() {
    TaskClassTheme(
        dynamicColor = false, darkTheme = true
    ) {
        EventSearchItemCard(
            event = EventEndTypeEventDto(
                event = EventEntity(
                    title = "Evento de teste",
                    typeEventId = 1,
                    description = "",
                    datetime = Instant.now(),
                    completed = false,
                    updatedAt = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis(),
                ), typeEvent = TypeEventEntity(
                    color = MaterialTheme.colorScheme.primary,
                    name = "Prova",
                    updatedAt = System.currentTimeMillis(),
                    createdAt = System.currentTimeMillis(),
                )
            )
        )
    }
}