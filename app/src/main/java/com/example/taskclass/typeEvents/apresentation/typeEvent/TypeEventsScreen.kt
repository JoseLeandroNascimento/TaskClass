package com.example.taskclass.typeEvents.apresentation.typeEvent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.R
import com.example.taskclass.common.composables.AppCardDefault
import com.example.taskclass.common.composables.AppConfirmDialog
import com.example.taskclass.common.data.Resource
import com.example.taskclass.core.data.model.TypeEvent
import com.example.taskclass.ui.theme.TaskClassTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeEventsScreen(viewModel: TypeEventsViewModel, onBack: () -> Unit) {

    val formState = viewModel.formState.collectAsStateWithLifecycle().value
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    TypeEventsScreen(
        onBack = onBack,
        formState = formState,
        uiState = uiState,
        updateNameTypeEvent = viewModel::updateTitle,
        updateColorTypeEvent = viewModel::updateColor,
        onSave = viewModel::save,
        onDelete = viewModel::delete,
        changeBottomSheetState = viewModel::changeBottomSheetState,
        onSelectedItemEdit = viewModel::onSelectedItemEdit,
        sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TypeEventsScreen(
    onBack: () -> Unit,
    formState: TypeEventFormState,
    uiState: TypeEventsUiState,
    updateNameTypeEvent: ((String) -> Unit)? = null,
    updateColorTypeEvent: ((Color) -> Unit)? = null,
    changeBottomSheetState: ((Boolean) -> Unit)? = null,
    onSelectedItemEdit: ((Int) -> Unit)? = null,
    onSave: () -> Unit,
    onDelete: (Int) -> Unit,
    sheetState: SheetState

) {


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.tipos_de_eventos),
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
                            changeBottomSheetState?.invoke(true)
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
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                uiState.typeEvents?.let { typesEvents ->

                    when (typesEvents) {

                        is Resource.Loading -> {
                            CircularProgressIndicator()
                        }

                        is Resource.Success -> {

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(alignment = Alignment.TopCenter),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                items(items = typesEvents.data, key = { it.id }) { typeEventItem ->
                                    TypeEventCardItem(
                                        typeEventItem = typeEventItem,
                                        onDelete = onDelete,
                                        onSelectedItemEdit = onSelectedItemEdit
                                    )
                                }
                            }
                        }

                        is Resource.Error -> {

                        }
                    }
                }
            }

            if (uiState.showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        changeBottomSheetState?.invoke(false)
                    },
                    sheetState = sheetState,

                    ) {
                    TypeEventForm(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),

                        formState = formState,
                        updateNameTypeEvent = updateNameTypeEvent,
                        updateColorTypeEvent = updateColorTypeEvent,
                        onCancel = {
                            changeBottomSheetState?.invoke(false)
                        },
                        onSave = {
                            onSave()
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun TypeEventCardItem(
    modifier: Modifier = Modifier,
    typeEventItem: TypeEvent,
    onSelectedItemEdit: ((Int) -> Unit)? = null,
    onDelete: (Int) -> Unit
) {

    var openDropdown by remember { mutableStateOf(false) }
    var confirmDelete by remember { mutableStateOf(false) }

    if (confirmDelete) {
        AppConfirmDialog(
            onDismissRequest = { confirmDelete = false },
            onConfirm = {
                onDelete(typeEventItem.id)
                confirmDelete = false
            },
            onCancel = {
                confirmDelete = false
            },
            title = stringResource(R.string.title_dialog_excluir_tipo_de_evento),
            description = stringResource(
                R.string.description_dialog_tem_certeza_que_deseja_excluir_o_tipo_de_evento,
                typeEventItem.name
            )
        )
    }

    AppCardDefault(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement
                    .spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = typeEventItem.color,
                            shape = CircleShape
                        )
                )
                Text(
                    text = typeEventItem.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
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
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    onDismissRequest = { openDropdown = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            onSelectedItemEdit?.invoke(typeEventItem.id)
                            openDropdown = false
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
                            openDropdown = false
                            confirmDelete = true
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


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TypeEventsScreenLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        TypeEventsScreen(
            onBack = {},
            uiState = TypeEventsUiState(
                typeEvents = Resource.Loading()
            ),
            formState = TypeEventFormState(),
            onSave = {},
            onDelete = {},
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TypeEventsScreenDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        TypeEventsScreen(
            onBack = {},
            uiState = TypeEventsUiState(),
            formState = TypeEventFormState(),
            onSave = {},
            onDelete = {},
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        )
    }
}


