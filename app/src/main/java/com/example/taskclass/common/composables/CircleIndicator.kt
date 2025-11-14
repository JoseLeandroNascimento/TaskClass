package com.example.taskclass.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun CircleIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    checked: Boolean = false,
    onClick: (() -> Unit)? = null,
    color: Color
) {

    val clickModifier = if (onClick != null) {
        Modifier.clickable { onClick() }
    } else {
        Modifier
    }

    val borderModifier = if (checked) {
        Modifier.border(1.dp, color = color, shape = CircleShape)
    } else {
        Modifier
    }


    Box(
        modifier = modifier
            .size(size)
            .clip(shape = CircleShape)
            .then(borderModifier)
            .background(
                color = color.copy(alpha = 0.25f),
            )

            .then(clickModifier),

        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size * .6f)
                .background(
                    color = color,
                    shape = CircleShape
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleIndicatorPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        CircleIndicator(
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CircleIndicatorCheckedPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        CircleIndicator(
            color = MaterialTheme.colorScheme.primary,
            checked = true
        )
    }
}