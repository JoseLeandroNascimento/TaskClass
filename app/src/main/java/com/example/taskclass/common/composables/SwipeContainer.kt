package com.example.taskclass.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme

@DslMarker
annotation class SwipeDsl

@SwipeDsl
class SwipeContainerScope(val state: SwipeToDismissBoxState)

@Composable
fun SwipeContainer(
    onToggleDone: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    startIcon: ImageVector,
    endIcon: ImageVector,
    content: @Composable SwipeContainerScope.() -> Unit
) {
    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.StartToEnd) {
                onRemove()
            } else if (it == SwipeToDismissBoxValue.EndToStart) {
                onToggleDone()
            }

            it != SwipeToDismissBoxValue.EndToStart
        },
        positionalThreshold = { it * 4 }
    )

    SwipeToDismissBox(
        state = state,
        modifier = modifier.wrapContentSize(),
        backgroundContent = {
            when (state.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                drawRect(lerp(Color.LightGray, Color.Blue, state.progress))
                            }
                            .wrapContentSize(Alignment.CenterEnd)
                            .padding(12.dp),
                        imageVector = endIcon,
                        tint = Color.White,
                        contentDescription = null
                    )
                }

                SwipeToDismissBoxValue.StartToEnd -> {
                    Icon(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(lerp(Color.LightGray, Color.Red, state.progress))
                            .wrapContentSize(Alignment.CenterStart)
                            .padding(12.dp),
                        imageVector = startIcon,
                        tint = Color.White,
                        contentDescription = null
                    )
                }

                SwipeToDismissBoxValue.Settled -> {}
            }
        }

    ) {
        SwipeContainerScope(state).content()
    }
}

@Preview
@Composable
private fun SwipeContainerPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        SwipeContainer(
            startIcon = Icons.Default.Edit,
            endIcon = Icons.Default.Delete,
            onToggleDone = {},
            onRemove = {}
        ) {
            ListItem(
                headlineContent = { Text("Título") },
                supportingContent = { Text("Subtítulo") }
            )
        }
    }
}

@Preview
@Composable
private fun SwipeContainerDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        SwipeContainer(
            startIcon = Icons.Default.Edit,
            endIcon = Icons.Default.Delete,
            onToggleDone = {},
            onRemove = {}
        ) {
            ListItem(
                headlineContent = { Text("Título") },
                supportingContent = { Text("Subtítulo") }
            )
        }
    }
}