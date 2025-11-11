package com.example.taskclass.ui.discipline.presentation.disciplineCreateScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppInputText
import com.example.taskclass.common.composables.AppSelectColor
import com.example.taskclass.common.data.Resource
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White

@Composable
fun DisciplineCreateScreen(
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    viewModel: DisciplineCreateViewModel
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    when {

        uiState.disciplineResponse is Resource.Success -> {
            onSaveSuccess()
        }

    }

    DisciplineCreateScreen(
        onBack = onBack,
        uiState = uiState,
        updateTitle = {
            viewModel.updateTitle(it)
        },
        updateTeacherName = {
            viewModel.updateTeacherName(it)
        },
        updateColorSelect = {
            viewModel.updateColorSelect(it)
        },
        onSave = {
            viewModel.save()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineCreateScreen(
    onBack: () -> Unit,
    uiState: DisciplineCreateUiState,
    updateTitle: ((String) -> Unit)? = null,
    updateTeacherName: ((String) -> Unit)? = null,
    updateColorSelect: ((Color) -> Unit)? = null,
    onSave: () -> Unit
) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.idDiscipline?.let { stringResource(R.string.atualizar_disciplina) }
                            ?: stringResource(R.string.nova_disciplina),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                R.string.descricao_voltar
                            )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding).padding(top = 8.dp),
        ) {

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppInputText(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.title.value,
                    isError = uiState.title.error != null,
                    supportingText = uiState.title.error,
                    onValueChange = { updateTitle?.invoke(it) },
                    label = stringResource(R.string.label_nome_da_disciplina),
                )

                AppInputText(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.teacherName.value,
                    onValueChange = { updateTeacherName?.invoke(it) },
                    label = stringResource(R.string.label_nome_do_professor),
                    placeholder = stringResource(R.string.placeholder_opcional),
                )

                AppSelectColor(
                    value = uiState.colorSelect,
                    label = stringResource(R.string.label_cor_select),
                    onValueChange = { color ->
                        updateColorSelect?.invoke(color)
                    }
                )

                AppButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isLoading = uiState.disciplineResponse is Resource.Loading,
                    label = uiState.idDiscipline?.let { stringResource(R.string.btn_salvar_disciplina) }
                        ?: stringResource(R.string.btn_cadastrar_disciplina),
                    onClick = onSave
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
        DisciplineCreateScreen(
            uiState = DisciplineCreateUiState(),
            onBack = {},
            updateTitle = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DisciplineCreateDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        DisciplineCreateScreen(
            uiState = DisciplineCreateUiState(),
            onBack = {},
            updateTitle = {},
            onSave = {}

        )
    }
}
