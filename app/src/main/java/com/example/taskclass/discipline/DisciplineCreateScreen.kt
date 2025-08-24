package com.example.taskclass.discipline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.commons.composables.AppButton
import com.example.taskclass.commons.composables.AppDialog
import com.example.taskclass.ui.theme.TaskClassTheme
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineCreateScreen(
    onBack: () -> Unit
) {
    val colorDefault = Color(0xFF9C27B0)
    val controller = rememberColorPickerController()
    var colorSelect by remember { mutableStateOf(colorDefault) }
    var tempColor by remember { mutableStateOf(colorDefault) }
    var showPickerColor by remember { mutableStateOf(false) }
    var subjectName by remember { mutableStateOf("") }
    var teacherName by remember { mutableStateOf("") }

    if (showPickerColor) {
        AppDialog(
            title = "Selecionar Cor",
            onDismissRequest = { showPickerColor = false }
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
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        onClick = { showPickerColor = false }
                    ) {
                        Text("Cancelar", style = MaterialTheme.typography.labelLarge)
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            colorSelect = tempColor
                            showPickerColor = false
                        }
                    ) {
                        Text("Confirmar", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Nova Disciplina",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                ,
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = subjectName,
                    onValueChange = { subjectName = it },
                    label = {
                        Text(
                            "Nome da Disciplina *",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    singleLine = true
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = teacherName,
                    onValueChange = { teacherName = it },
                    label = {
                        Text(
                            "Nome do Professor",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    placeholder = {
                        Text(
                            "Opcional",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                        )
                    },
                    singleLine = true
                )

                Text(
                    text = "Cor atual",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(colorSelect)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )

                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val presetColors = listOf(
                            Color(0xFF9C27B0),
                            Color(0xFF4CAF50),
                            Color(0xFFFFC107),
                            Color(0xFFFF5722),
                            Color(0xFF673AB7),
                            Color(0xFF2979FF)
                        )

                        presetColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(color)
                                    .border(
                                        2.dp,
                                        if (color == colorSelect) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { colorSelect = color }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            Color.Red,
                                            Color(0xFFFF7F00),
                                            Color.Yellow,
                                            Color.Green,
                                            Color.Blue,
                                            Color(0xFF4B0082),
                                            Color(0xFF8F00FF)
                                        )
                                    )
                                )
                                .clickable {
                                    tempColor = colorSelect
                                    showPickerColor = true
                                }
                        )
                    }
                }

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    enabled = subjectName.isNotBlank(),
                    label = "Cadastrar Disciplina",
                    onClick = { /* salvar */ }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DisciplineCreateLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        DisciplineCreateScreen { }
    }
}

@Preview(showBackground = true)
@Composable
private fun DisciplineCreateDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        DisciplineCreateScreen { }
    }
}
