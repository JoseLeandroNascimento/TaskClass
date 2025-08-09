package com.example.taskclass.agenda

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.taskclass.agenda.composables.ScheduleGrid
import com.example.taskclass.agenda.composables.sampleEvents


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen(
    modifier: Modifier = Modifier,
    navigationBar: @Composable () -> Unit,
    openNavigationDrawer: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Horário da semana",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = openNavigationDrawer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        bottomBar = {
            navigationBar()
        }

    ) { innerPadding ->

        ScheduleGrid(
            events = sampleEvents,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}