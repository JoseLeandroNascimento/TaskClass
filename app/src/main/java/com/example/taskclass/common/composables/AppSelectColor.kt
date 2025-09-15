package com.example.taskclass.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun AppSelectColor(
    modifier: Modifier = Modifier,
    value: Color,
    label: String,
    onValueChange: (Color) -> Unit
) {

    var showDialogSelectColor by remember { mutableStateOf(false) }


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.labelMedium)
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(value)
                .clickable {
                    showDialogSelectColor = !showDialogSelectColor
                }
                .border(2.dp, value.copy(alpha = 0.5f), CircleShape)
        )
    }

    if (showDialogSelectColor) {
        SelectColorDialog(
            colorSelect = value,
            changeColorSelect = onValueChange,
            changeShowPickerColor = {
                showDialogSelectColor = it
            },
        )
    }
}

@Composable
private fun SelectColorDialog(
    colorSelect: Color,
    changeColorSelect: (Color) -> Unit,
    changeShowPickerColor: (Boolean) -> Unit
) {

    val controller = rememberColorPickerController()
    var tempColor by remember { mutableStateOf(colorSelect) }

    AppDialog(
        title = "Selecionar Cor",
        onDismissRequest = { changeShowPickerColor(false) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Escolha uma cor para representar a disciplina",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(tempColor)
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            listOf(Color.White, Color.Gray.copy(alpha = 0.5f))
                        ),
                        shape = CircleShape
                    )
                    .shadow(elevation = 6.dp, shape = CircleShape, clip = false)
            )

            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                initialColor = colorSelect,
                controller = controller,
                onColorChanged = { envelope: ColorEnvelope ->
                    tempColor = envelope.color
                }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    onClick = { changeShowPickerColor?.invoke(false) }
                ) {
                    Text("Cancelar", style = MaterialTheme.typography.labelLarge)
                }
                Button(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        changeColorSelect(tempColor)
                        changeShowPickerColor.invoke(false)
                    }
                ) {
                    Text("Confirmar", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AppSelectColorLightPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppSelectColor(
            value = Color.Blue,
            label = "Selecione a cor",
            onValueChange = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun AppSelectColorDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppSelectColor(
            value = Color.Blue,
            label = "Selecione a cor",
            onValueChange = {}
        )
    }
}