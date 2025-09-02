package com.example.taskclass.agenda.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskclass.agenda.presentation.composables.ScheduleGrid


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    viewModel: AgendaViewModel
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    ScheduleGrid(
        uiState = uiState,
        modifier = modifier
            .fillMaxSize()
    )

}