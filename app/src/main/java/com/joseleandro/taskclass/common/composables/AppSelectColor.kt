package com.joseleandro.taskclass.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.joseleandro.taskclass.R
import com.joseleandro.taskclass.ui.theme.ColorsSelect
import com.joseleandro.taskclass.ui.theme.TaskClassTheme
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@Composable
fun AppSelectColor(
    modifier: Modifier = Modifier,
    predefinedColors: List<Color> = ColorsSelect.predefinedColors,
    value: Color,
    colorTransparent: Color = MaterialTheme.colorScheme.background,
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
            colorTransparent = colorTransparent,
            predefinedColors = predefinedColors,
            onDismiss = { showDialogSelectColor = false }
        )
    }
}

@Composable
fun SelectColorOptionsDialog(
    currentColor: Color,
    predefinedColors: List<Color> = ColorsSelect.predefinedColors,
    colorTransparent: Color = MaterialTheme.colorScheme.background,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {


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
                            colorTransparent = colorTransparent,
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
            predefinedColors = ColorsSelect.predefinedColors,
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
            predefinedColors = ColorsSelect.predefinedColors,
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