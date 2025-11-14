package com.example.taskclass.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.R
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
    val sizeCircleBoxColor = 48.dp

    Column(
        modifier = modifier.width(IntrinsicSize.Max),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center
        )

        CircleIndicator(
            onClick = { showDialogSelectColor = true },
            size = sizeCircleBoxColor,
            color = value
        )

    }

    if (showDialogSelectColor) {
        SelectColorOptionsDialog(
            currentColor = value,
            onColorSelected = {
                onValueChange(it)
                showDialogSelectColor = false
            },
            onDismiss = { showDialogSelectColor = false }
        )
    }
}

@Composable
fun SelectColorOptionsDialog(
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val predefinedColors = listOf(
        Color(0xFFFF1744), Color(0xFFFF6F00), Color(0xFFFFC400), Color(0xFFEEFF41),
        Color(0xFF76FF03), Color(0xFF00E676), Color(0xFF1DE9B6), Color(0xFF00BFA5),
        Color(0xFF00B8D4), Color(0xFF0091EA), Color(0xFF2962FF), Color(0xFF3D5AFE),
        Color(0xFF651FFF), Color(0xFFAA00FF), Color(0xFFD500F9), Color(0xFFF50057),
        Color(0xFFFF4081), Color(0xFFFF80AB), Color(0xFFFF5252), Color(0xFFFF8A65),
        Color(0xFFFFB74D), Color(0xFFFFD740), Color(0xFFF4FF81), Color(0xFFC6FF00),
        Color(0xFF64DD17), Color(0xFF00C853), Color(0xFF00BFA6), Color(0xFF18FFFF),
        Color(0xFF40C4FF), Color(0xFF82B1FF), Color(0xFFB388FF), Color(0xFFE040FB),
        Color(0xFFFF80FF), Color(0xFFFFB3E5), Color(0xFFFFCDD2), Color(0xFFF8BBD0),
        Color(0xFFFFAB91), Color(0xFFFFCC80), Color(0xFFDCE775), Color(0xFFAED581),
        Color(0xFF80CBC4), Color(0xFF4DB6AC), Color(0xFF4FC3F7), Color(0xFF64B5F6),
        Color(0xFF7986CB), Color(0xFF9575CD), Color(0xFFBA68C8), Color(0xFFE1BEE7),
        Color(0xFFBCAAA4), Color(0xFF90A4AE), Color(0xFFFAFAFA), Color(0xFFF5F5F5),
        Color(0xFFEEEEEE), Color(0xFFE0E0E0), Color(0xFFBDBDBD), Color(0xFF9E9E9E),
        Color(0xFF757575), Color(0xFF616161), Color(0xFF424242), Color(0xFF212121),
        Color(0xFF8BC34A), Color(0xFFCDDC39), Color(0xFFFFEB3B), Color(0xFFFFC107),
        Color(0xFFFF9800), Color(0xFFFF5722), Color(0xFFE91E63), Color(0xFF9C27B0),
        Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3), Color(0xFF03A9F4),
        Color(0xFF00BCD4), Color(0xFF009688), Color(0xFF4CAF50), Color(0xFF8BC34A),
        Color(0xFFCDDC39), Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
        Color(0xFFFF5722), Color(0xFF795548), Color(0xFF9E9E9E), Color(0xFF607D8B),
        Color(0xFF263238), Color(0xFF37474F), Color(0xFF455A64), Color(0xFF546E7A),
        Color(0xFF607D8B), Color(0xFF78909C), Color(0xFF90A4AE), Color(0xFFB0BEC5),
        Color(0xFFCFD8DC), Color(0xFFECEFF1), Color(0xFFFFF9C4), Color(0xFFFFF59D),
        Color(0xFFFFF176), Color(0xFFFFEE58), Color(0xFFFFEB3B), Color(0xFFFDD835),
        Color(0xFFFBC02D), Color(0xFFF9A825), Color(0xFFF57F17), Color(0xFFFFD54F)
    )


    var showCustomPicker by remember { mutableStateOf(false) }

    if (showCustomPicker) {
        SelectColorDialog(
            colorSelect = currentColor,
            changeColorSelect = onColorSelected,
            changeShowPickerColor = { showCustomPicker = false }
        )
        return
    }

    AppDialog(
        title = stringResource(R.string.selecionar_cor),
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.descricao_escolha_uma_cor_ou_personalize_a_sua),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                ),
                textAlign = TextAlign.Center
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    predefinedColors.forEach { color ->

                        CircleIndicator(
                            checked = currentColor == color,
                            size = 42.dp,
                            color = color,
                            onClick = { onColorSelected(color) }
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            TextButton(
                onClick = { showCustomPicker = true },
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ColorLens,
                    contentDescription = stringResource(R.string.description_icon_color)
                )
                Text(
                    stringResource(R.string.btn_escolher_cor_personalizada),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        stringResource(R.string.btn_cancelar),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
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
        title = "Cor Personalizada",
        onDismissRequest = { changeShowPickerColor(false) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            CircleIndicator(
                color = tempColor,
                size = 72.dp
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
                    onClick = { changeShowPickerColor(false) }
                ) {
                    Text("Cancelar", style = MaterialTheme.typography.labelLarge)
                }
                AppButton(
                    modifier = Modifier.weight(1f),
                    label = "Confirmar",
                    onClick = {
                        changeColorSelect(tempColor)
                        changeShowPickerColor(false)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun SelectColorDialogPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        SelectColorDialog(
            changeColorSelect = {},
            changeShowPickerColor = {},
            colorSelect = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun SelectColorDialogDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        SelectColorDialog(
            changeColorSelect = {},
            changeShowPickerColor = {},
            colorSelect = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun SelectColorOptionsDialogPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        SelectColorOptionsDialog(
            currentColor = MaterialTheme.colorScheme.primary,
            onColorSelected = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun SelectColorOptionsDialogDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        SelectColorOptionsDialog(
            currentColor = MaterialTheme.colorScheme.primary,
            onColorSelected = {},
            onDismiss = {},
        )
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