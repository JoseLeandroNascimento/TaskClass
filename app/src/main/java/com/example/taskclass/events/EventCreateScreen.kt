package com.example.taskclass.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.commons.composables.AppInputTime
import com.example.taskclass.ui.theme.TaskClassTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventCreateScreen(
    onBack: ()-> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
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
                title = {
                    Text(text = "Novo evento", style = MaterialTheme.typography.titleMedium)
                }
            )
        }
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    label = {
                        Text(text = "Título", style = MaterialTheme.typography.labelMedium)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    value = "",
                    onValueChange = {}
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        label = {
                            Text(text = "Data", style = MaterialTheme.typography.labelMedium)
                        },
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null
                            )
                        },
                        value = "",
                        onValueChange = {}
                    )

                    AppInputTime(
                        modifier = Modifier.weight(1f),
                        label = "Hora",
                        value = ""
                    ) { }
                }

                OutlinedTextField(
                    label = {
                        Text(text = "Descrição", style = MaterialTheme.typography.labelMedium)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4,
                    maxLines = 4,
                    value = "",
                    onValueChange = {}
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventCreateScreenLightPreview() {

    TaskClassTheme(
        dynamicColor = true,
        darkTheme = false
    ) {
        EventCreateScreen(
            onBack = {}
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun EventCreateScreenDarkPreview() {

    TaskClassTheme(
        dynamicColor = true,
        darkTheme = true
    ) {
        EventCreateScreen(
            onBack = {}
        )
    }

}