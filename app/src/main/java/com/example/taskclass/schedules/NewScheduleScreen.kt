package com.example.taskclass.schedules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.sp
import com.example.taskclass.commons.composables.AppDropdown
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

    var weekDay by remember {
        mutableStateOf("")
    }

    var discipline by remember {
        mutableStateOf("")
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Novo horário", fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Surface(
            modifier = Modifier.padding(innerPadding)
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                AppDropdown(
                    value = weekDay,
                    label = "Dia da Semana *"
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Domingo")
                        },
                        onClick = {
                            weekDay = "Domingo"
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Segunda")
                        },
                        onClick = {
                            weekDay = "Segunda"
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Terça")
                        },
                        onClick = {
                            weekDay = "Terça"
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Quarta")
                        },
                        onClick = {
                            weekDay = "Quarta"
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Quinta")
                        },
                        onClick = {
                            weekDay = "Quinta"
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Sexta")
                        },
                        onClick = {
                            weekDay = "Sexta"
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Sábado")
                        },
                        onClick = {
                            weekDay = "Sábado"
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }

                AppDropdown(
                    value = discipline,
                    label = "Disciplina *"
                ) {
                    listDiscipline.forEach { discipline ->
                        DropdownMenuItem(
                            text = {
                                Text(text = discipline.title)
                            },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(discipline.color, CircleShape)
                                        .border(1.dp, Color.Black.copy(alpha = 0.1f), CircleShape)
                                        .shadow(2.dp, CircleShape, clip = false)
                                )
                            },
                            onClick = {
                                weekDay = discipline.title
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }

            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewSchedulePreview() {

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