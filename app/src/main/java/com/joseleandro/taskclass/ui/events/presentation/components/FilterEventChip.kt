package com.joseleandro.taskclass.ui.events.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.core.data.model.enums.EEventStatus
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.joseleandro.taskclass.ui.theme.White

@Composable
fun FilterEventChip(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onSelected: () -> Unit,
    value: EEventStatus?,
    labelDefault: String = "Todos"
) {

    FilterChip(
        modifier = modifier,
        border = null,
        leadingIcon = {
            if (selected) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    tint = White,
                    imageVector = Icons.Default.Check,
                    contentDescription = null
                )
            }
        },
        selected = selected,
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .1f),
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = .8f
            )
        ),
        label = {
            Text(
                text = value?.label ?: labelDefault,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium,
                color = if (selected) White else MaterialTheme.colorScheme.primary
            )
        },
        onClick = onSelected
    )
}

@Preview(showBackground = true)
@Composable
private fun FilterEventChipPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        FilterEventChip(
            onSelected = {

            },
            value = EEventStatus.AGENDADO
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun FilterEventChipDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        FilterEventChip(
            onSelected = {

            },
            value = EEventStatus.AGENDADO
        )
    }
}