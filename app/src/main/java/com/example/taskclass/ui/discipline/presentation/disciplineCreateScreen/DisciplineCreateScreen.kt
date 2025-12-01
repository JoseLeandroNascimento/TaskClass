package com.example.taskclass.ui.discipline.presentation.disciplineCreateScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppButton
import com.example.taskclass.common.composables.AppInputText
import com.example.taskclass.common.composables.AppSelectColor
import com.example.taskclass.common.composables.HideKeyboardOnTap
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White

@Composable
fun DisciplineCreateScreen(
    onBack: () -> Unit,
    viewModel: DisciplineCreateViewModel
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value


    DisciplineCreateScreen(
        onBack = onBack,
        uiState = uiState,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineCreateScreen(
    onBack: () -> Unit,
    uiState: DisciplineCreateUiState,
    onAction: (DisciplineCreateAction) -> Unit,
) {

    val focusTitle = remember { FocusRequester() }



    LaunchedEffect(Unit) {
        focusTitle.requestFocus()
    }

    LaunchedEffect(uiState.isBackNavigation) {

        if (uiState.isBackNavigation) {
            onBack()
        }
    }


    Scaffold(
        topBar = {
            DisciplineCreateTopBar(
                onBack = onBack,
                uiState = uiState,
            )

        }
    ) { innerPadding ->

        HideKeyboardOnTap(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp),
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(Alignment.TopCenter),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        AppInputText(
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusTitle),
                            value = uiState.form.title.value,
                            isError = uiState.form.title.error != null,
                            supportingText = uiState.form.title.error,
                            onValueChange = { onAction(DisciplineCreateAction.UpdateTitle(it)) },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            label = stringResource(R.string.label_nome_da_disciplina),
                        )

                        AppInputText(
                            modifier = Modifier.fillMaxWidth(),
                            value = uiState.form.teacherName.value,
                            onValueChange = { onAction(DisciplineCreateAction.UpdateTeacherName(it)) },
                            label = stringResource(R.string.label_nome_do_professor),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Done
                            ),
                            placeholder = stringResource(R.string.placeholder_opcional),
                        )

                        AppSelectColor(
                            value = uiState.form.colorSelect,
                            label = stringResource(R.string.label_cor_select),
                            onValueChange = { color ->
                                onAction(DisciplineCreateAction.UpdateColorSelect(color))
                            }
                        )

                        AppButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            isLoading = uiState.isLoadingButton,
                            label = uiState.form.idDiscipline?.let { stringResource(R.string.btn_salvar_disciplina) }
                                ?: stringResource(R.string.btn_cadastrar_disciplina),
                            onClick = {
                                onAction(DisciplineCreateAction.OnSave)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisciplineCreateTopBar(
    onBack: () -> Unit,
    uiState: DisciplineCreateUiState,
) {
    TopAppBar(
        title = {
            Text(
                text = uiState.form.idDiscipline?.let { stringResource(R.string.atualizar_disciplina) }
                    ?: stringResource(R.string.nova_disciplina),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = White,
            navigationIconContentColor = White,
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

@Preview
@Composable
private fun DisciplineCreateLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        DisciplineCreateScreen(
            uiState = DisciplineCreateUiState(),
            onBack = {},
            onAction = {},
        )
    }
}

@Preview()
@Composable
private fun DisciplineCreateDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        DisciplineCreateScreen(
            uiState = DisciplineCreateUiState(),
            onBack = {},
            onAction = {},
        )
    }
}
