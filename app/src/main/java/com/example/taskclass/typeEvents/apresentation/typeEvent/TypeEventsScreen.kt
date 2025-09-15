package com.example.taskclass.typeEvents.apresentation.typeEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun TypeEventsScreen(viewModel: TypeEventsViewModel, onBack: () -> Unit) {
    val formState = viewModel.formState.collectAsStateWithLifecycle().value
    TypeEventsScreen(
        onBack = onBack,
        formState = formState,
        updateNameTypeEvent = viewModel::updateTitle,
        updateColorTypeEvent = viewModel::updateColor,
        resetForm = viewModel::resetForm
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeEventsScreen(
    onBack: () -> Unit,
    formState: TypeEventFormState,
    updateNameTypeEvent: ((String) -> Unit)? = null,
    updateColorTypeEvent: ((Color) -> Unit)? = null,
    resetForm: (() -> Unit)? = null
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tipos de eventos",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
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
                actions = {
                    IconButton(
                        onClick = {
                            showBottomSheet = true
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = .1f),
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Text(text = "Tipos de eventos")

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                        resetForm?.invoke()
                    },
                    sheetState = sheetState
                ) {
                    TypeEventForm(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        formState = formState,
                        updateNameTypeEvent = updateNameTypeEvent,
                        updateColorTypeEvent = updateColorTypeEvent,
                        onCancel = {
                            showBottomSheet = false
                            resetForm?.invoke()
                        },
                        onSave = {
                            showBottomSheet = false
                            resetForm?.invoke()
                        }
                    )
                }
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun TypeEventsScreenLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        TypeEventsScreen(
            onBack = {},
            formState = TypeEventFormState()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TypeEventsScreenDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        TypeEventsScreen(
            onBack = {},
            formState = TypeEventFormState()
        )
    }
}


