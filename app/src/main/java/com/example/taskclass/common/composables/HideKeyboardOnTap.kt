package com.example.taskclass.common.composables

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Composable
fun HideKeyboardOnTap(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    keyboardController?.hide()
                }
            }
    ) {
        content()
    }
}