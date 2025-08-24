package com.example.taskclass.schedules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.commons.composables.AppButton
import com.example.taskclass.commons.composables.AppDropdown
import com.example.taskclass.commons.composables.AppInputTime
import com.example.taskclass.discipline.Discipline
import com.example.taskclass.ui.theme.TaskClassTheme

val listDiscipline: List<Discipline> = listOf(
    Discipline(1, "Matemática", Color(0xFF2979FF)),
    Discipline(2, "Química", Color(0xFF9C27B0)),
    Discipline(3, "Geografia", Color(0xFF4CAF50)),
    Discipline(4, "Biologia", Color(0xFFFFC107)),
    Discipline(5, "Sociologia", Color(0xFFFF5722)),
    Discipline(6, "Filosofia", Color(0xFF673AB7)),
    Discipline(7, "Educação Física", Color(0xFFFFEB3B)),
    Discipline(8, "Inglês", Color(0xFF009688)),
    Discipline(9, "História", Color(0xFFF44336))
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScheduleScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    var weekDay by remember { mutableStateOf("") }
    var disciplineSelect by remember { mutableStateOf("") }
    var timeFirst by remember { mutableStateOf("") }
    var lastTime by remember { mutableStateOf("") }

    val daysOfWeek = listOf(
        "Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Novo Horário",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppDropdown(
                    value = weekDay,
                    label = "Dia da Semana *"
                ) {
                    daysOfWeek.forEach { day ->
                        DropdownMenuItem(
                            text = { Text(day) },
                            onClick = { weekDay = day },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }

                AppDropdown(
                    value = disciplineSelect,
                    label = "Disciplina *"
                ) {
                    listDiscipline.forEach { discipline ->
                        DropdownMenuItem(
                            text = { Text(discipline.title) },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(discipline.color, CircleShape)
                                        .border(1.dp, Color.Black.copy(alpha = 0.08f), CircleShape)
                                        .shadow(3.dp, CircleShape, clip = false)
                                )
                            },
                            onClick = { disciplineSelect = discipline.title },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppInputTime(
                        modifier = Modifier.weight(1f),
                        value = timeFirst,
                        onValueChange = { timeFirst = it },
                        label = "Início"
                    )
                    AppInputTime(
                        modifier = Modifier.weight(1f),
                        value = lastTime,
                        onValueChange = { lastTime = it },
                        label = "Fim"
                    )
                }

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    label = "Cadastrar Horário",
                    onClick = { /* salvar */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewScheduleLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NewScheduleScreen(
            onBack = {

            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewScheduleDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        NewScheduleScreen(
            onBack = {

            }
        )
    }
}