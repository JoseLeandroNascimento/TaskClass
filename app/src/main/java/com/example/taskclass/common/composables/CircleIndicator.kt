package com.example.taskclass.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun CircleIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .background(
                color = color.copy(alpha = 0.25f),
                shape = CircleShape
            ),
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