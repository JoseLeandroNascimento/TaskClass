package com.example.taskclass.ui.typeEvents.apresentation.typeEventCreate

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
fun TypeEventCreateScreen(
    modifier: Modifier = Modifier,
    viewModel: TypeEventCreateViewModel,
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TypeEventCreateScreen(
        uiState = uiState,
        onBack = onBack,
        modifier = modifier,
        updateTitle = viewModel::updateTitle,
        updateColor = viewModel::updateColor,
        onSave = viewModel::save
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeEventCreateScreen(
    modifier: Modifier = Modifier,
    uiState: TypeEventUiState,
    updateTitle: ((String) -> Unit)? = null,
    updateColor: ((Color) -> Unit)? = null,
    onSave: (() -> Unit)? = null,
    onBack: () -> Unit,
) {

    val titleFocus = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        titleFocus.requestFocus()
    }

    if (uiState.isBackNavigation) {
        onBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tipo de evento",
                        style = MaterialTheme.typography.titleMedium
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }
    ) { innerPadding ->

        HideKeyboardOnTap {

            when{

                uiState.isLoading ->{
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                else->{

                    Surface(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(top = 8.dp)
                            .fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),

                            ) {
                            AppInputText(
                                modifier = Modifier.focusRequester(titleFocus),
                                value = uiState.formState.nameTypeEvent.value,
                                isError = uiState.formState.nameTypeEvent.error != null,
                                supportingText = uiState.formState.nameTypeEvent.error,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                                onValueChange = {
                                    updateTitle?.invoke(it)
                                },
                                label = stringResource(R.string.label_nome_do_evento)
                            )

                            AppSelectColor(
                                value = uiState.formState.colorTypeEvent.value,
                                label = stringResource(R.string.label_cor_select),
                                onValueChange = { color ->
                                    updateColor?.invoke(color)
                                }
                            )

                            AppButton(
                                modifier = Modifier.fillMaxWidth(),
                                isLoading = uiState.isLoadingButton,
                                label = stringResource(R.string.btn_salvar),
                                onClick = {
                                    onSave?.invoke()
                                }
                            )
                        }

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TypeEventCreateScreenPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        TypeEventCreateScreen(
            uiState = TypeEventUiState(),
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TypeEventCreateScreenDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        TypeEventCreateScreen(
            uiState = TypeEventUiState(),
            onBack = {}
        )
    }
}

