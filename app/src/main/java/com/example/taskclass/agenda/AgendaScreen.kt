package com.example.taskclass.agenda

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.taskclass.agenda.composables.ScheduleGrid
import com.example.taskclass.agenda.composables.sampleEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier
) {


    ScheduleGrid(
        events = sampleEvents,
        modifier = modifier
            .fillMaxSize()
    )

}