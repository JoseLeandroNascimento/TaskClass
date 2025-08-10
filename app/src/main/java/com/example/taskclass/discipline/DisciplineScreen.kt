package com.example.taskclass.discipline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Discipline(
    val id: Int,
    val title: String,
    val color: Color
)

val listDiscipline: List<Discipline> = listOf(
    Discipline(
        id = 1,
        title = "Matemática",
        color = Color(0xFF2979FF)
    ),
    Discipline(
        id = 2,
        title = "Química",
        color = Color(0xFF9C27B0)
    ),
    Discipline(
        id = 3,
        title = "Geografia",
        color = Color(0xFF4CAF50)
    ),
    Discipline(
        id = 4,
        title = "Biologia",
        color = Color(0xFFFFC107)
    ),
    Discipline(
        id = 5,
        title = "Sociologia",
        color = Color(0xFFFF5722)
    ),
    Discipline(
        id = 6,
        title = "Filosofia",
        color = Color(0xFF673AB7)
    ),
    Discipline(
        id = 7,
        title = "Educação física",
        color = Color(0xFFFFEB3B)
    ),
    Discipline(
        id = 8,
        title = "Inglês",
        color = Color(0xFF009688)
    ),
    Discipline(
        id = 9,
        title = "História",
        color = Color(0xFFF44336)
    ),
    Discipline(
        id = 10,
        title = "Matemática",
        color = Color(0xFF2979FF)
    ),
    Discipline(
        id = 11,
        title = "Química",
        color = Color(0xFF9C27B0)
    ),
    Discipline(
        id = 12,
        title = "Geografia",
        color = Color(0xFF4CAF50)
    ),
    Discipline(
        id = 13,
        title = "Biologia",
        color = Color(0xFFFFC107)
    ),
    Discipline(
        id = 14,
        title = "Sociologia",
        color = Color(0xFFFF5722)
    ),
    Discipline(
        id = 15,
        title = "Filosofia",
        color = Color(0xFF673AB7)
    ),
    Discipline(
        id = 16,
        title = "Educação física",
        color = Color(0xFFFFEB3B)
    ),
    Discipline(
        id = 17,
        title = "Inglês",
        color = Color(0xFF009688)
    ),
    Discipline(
        id = 18,
        title = "História",
        color = Color(0xFFF44336)
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Disciplinas", fontSize = 16.sp, fontWeight = FontWeight.SemiBold
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
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        DisciplineContent(
            modifier = Modifier
                .padding(innerPadding)
        )
    }

}

@Composable
fun DisciplineContent(modifier: Modifier = Modifier) {


    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(items = listDiscipline, key = { it.id }) { discipline ->
            DisciplineItem(discipline = discipline)
        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun DisciplineItem(modifier: Modifier = Modifier, discipline: Discipline) {

    var openDropdown by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(color = discipline.color, shape = CircleShape)
                )
                Text(
                    text = discipline.title,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                )
            }

            Box {
                IconButton(
                    onClick = { openDropdown = !openDropdown }
                ) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
                DropdownMenu(
                    expanded = openDropdown,
                    containerColor = MaterialTheme.colorScheme.background,
                    onDismissRequest = {
                        openDropdown = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(text = "Editar")
                        },
                        onClick = {
                            openDropdown = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Excluir")
                        },
                        onClick = {
                            openDropdown = false
                        }
                    )
                }
            }
        }
    }
}