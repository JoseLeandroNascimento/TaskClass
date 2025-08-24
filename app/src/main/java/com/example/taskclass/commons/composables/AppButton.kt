package com.example.taskclass.commons.composables

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    onClick:()-> Unit
) {
    Button(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Medium
            ),
            color = White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AppButtonLightPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppButton(label = "Botão"){}
    }
}

@Preview(showBackground = true)
@Composable
private fun AppButtonDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppButton(label = "Botão"){}
    }
}