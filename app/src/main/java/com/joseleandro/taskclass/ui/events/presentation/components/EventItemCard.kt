package com.joseleandro.taskclass.ui.events.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.taskclass.common.composables.AppCardDefault
import com.joseleandro.taskclass.common.composables.CircleIndicator
import com.joseleandro.taskclass.common.composables.SwipeContainer
import com.joseleandro.taskclass.common.utils.toFormattedDateTime
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun EventItemCard(
    id: Int,
    title: String,
    color: Color,
    typeEvent: String,
    checked: Boolean,
    dateTime: Instant,
    onDelete: ((Int) -> Unit)? = null,
    onEdit: ((Int) -> Unit)? = null,
    onSelected: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {

    var confirmDelete by remember { mutableStateOf(false) }
    var swipeState by remember { mutableStateOf<SwipeToDismissBoxState?>(null) }
    val scope = rememberCoroutineScope()

    if (confirmDelete) {

        ConfirmDeleteDialog(
            onDismissRequest = {
                confirmDelete = false
                swipeState?.let {
                    scope.launch {
                        it.reset()
                    }
                }
            },
            confirmDelete = {
                confirmDelete = false
                swipeState?.let {
                    scope.launch {
                        it.reset()
                        onDelete?.invoke(id)
                    }
                }
            }
        )
    }

    SwipeContainer(
        modifier = Modifier.clip(RoundedCornerShape(14.dp)),
        onRemove = {
            confirmDelete = true
        },
        onToggleDone = {
            onEdit?.invoke(id)
        },
        startIcon = Icons.Default.Delete,
        endIcon = Icons.Default.Edit
    ) {

        swipeState = state

        AppCardDefault(onSelected = onSelected) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer(
                        alpha = if (checked) .4f else 1f
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .8f)
                            )
                            Text(
                                text = dateTime.toFormattedDateTime("dd/MM/yyyy"),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = .8f
                                    )
                                )
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Default.AccessTimeFilled,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = .8f)
                            )
                            Text(
                                text = dateTime.toFormattedDateTime("HH:mm"),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                        alpha = .8f
                                    )
                                )
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(
                                    color = color.copy(alpha = .1f),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = typeEvent,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = color
                                )
                            )
                        }
                    }
                }

                Checkbox(
                    checked = checked,
                    onCheckedChange = onCheckedChange
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
            id = 1,
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
            id = 1,
            title = "Event 1",
            color = MaterialTheme.colorScheme.primary,
            checked = true,
            dateTime = Instant.now(),
            typeEvent = "Prova",
            onSelected = {}
        ) { }
    }
}