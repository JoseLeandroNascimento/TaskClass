package com.example.taskclass.common.composables

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

/**
 * Intercept touch events to hide the keyboard
 * @author José Leandro da silva Nascimento
 * @param content composables the inputs forms
 * @param modifier modifier default
 */

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