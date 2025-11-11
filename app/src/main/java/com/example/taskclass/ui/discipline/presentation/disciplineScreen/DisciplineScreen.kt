package com.example.taskclass.ui.discipline.presentation.disciplineScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppCardDefault
import com.example.taskclass.common.composables.AppConfirmDialog
import com.example.taskclass.common.composables.CircleIndicator
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.Discipline
import com.example.taskclass.ui.theme.White


@Composable
fun DisciplineScreen(
    onBack: () -> Unit,
    onCreateDiscipline: () -> Unit,
    onEditDiscipline: (Int) -> Unit,
    viewModel: DisciplineViewModel
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    DisciplineScreen(
        onBack = onBack,
        uiState = uiState,
        onCreateDiscipline = onCreateDiscipline,
        onEditDiscipline = onEditDiscipline,
        onDeleteDiscipline = {
            viewModel.deleteDiscipline(it)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineScreen(
    onBack: () -> Unit,
    onCreateDiscipline: () -> Unit,
    onDeleteDiscipline: (Int) -> Unit,
    uiState: DisciplineUiState,
    onEditDiscipline: (Int) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
                title = {
                    Text(
                        text = "Disciplinas",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateDiscipline,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar",
                    tint = White
                )
            }
        }
    ) { innerPadding ->

        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            DisciplineContent(
                modifier = Modifier.padding(innerPadding).padding(top = 8.dp),
                uiState = uiState,
                onDeleteDiscipline = onDeleteDiscipline,
                onEditDiscipline = onEditDiscipline
            )
        }
    }
}

@Composable
fun DisciplineContent(
    modifier: Modifier = Modifier,
    uiState: DisciplineUiState,
    onDeleteDiscipline: (Int) -> Unit,
    onEditDiscipline: (Int) -> Unit,
) {


    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        when (uiState.disciplines) {
            is Resource.Loading -> {
                CircularProgressIndicator(
                    strokeWidth = 4.dp
                )
            }

            is Resource.Success -> {

                if (uiState.disciplines.data.isNotEmpty()) {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {

                        items(uiState.disciplines.data, key = { it.id }) { discipline ->
                            DisciplineItem(
                                discipline = discipline,
                                onDeleteDiscipline = onDeleteDiscipline,
                                onEditDiscipline = onEditDiscipline
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }

                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Nenhuma disciplina",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            is Resource.Error -> {

                Text(text = uiState.disciplines.message)
            }
        }
    }
}

@Composable
fun DisciplineItem(
    modifier: Modifier = Modifier,
    onDeleteDiscipline: (Int) -> Unit,
    onEditDiscipline: (Int) -> Unit,
    discipline: Discipline
) {
    var openDropdown by rememberSaveable { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    if (confirmDelete) {
        AppConfirmDialog(
            onDismissRequest = { confirmDelete = false },
            title = stringResource(R.string.title_dialog_excluir_disciplina),
            description = stringResource(
                R.string.tem_certeza_que_deseja_excluir_a_disciplina,
                discipline.title
            ),
            onConfirm = {
                confirmDelete = false
                onDeleteDiscipline(discipline.id)
            },
            onCancel = {
                confirmDelete = false
            }

        )
    }

    AppCardDefault(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                CircleIndicator(
                    size = 28.dp,
                    color = discipline.color
                )

                Text(
                    text = discipline.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                )
            }

            Box {
                IconButton(onClick = { openDropdown = !openDropdown }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opções"
                    )
                }
                DropdownMenu(
                    expanded = openDropdown,
                    containerColor = MaterialTheme.colorScheme.surface,
                    onDismissRequest = { openDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            openDropdown = false
                            onEditDiscipline(discipline.id)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Excluir") },
                        onClick = {
                            confirmDelete = true
                            openDropdown = false
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }
    }
}