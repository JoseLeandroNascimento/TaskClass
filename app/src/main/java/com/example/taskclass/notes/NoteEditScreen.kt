package com.example.taskclass.notes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme
import com.example.taskclass.ui.theme.White
import com.mohamedrejeb.richeditor.model.rememberRichTextState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    onBack: () -> Unit
) {

    val richTextState = rememberRichTextState()
    richTextState.config.linkColor = Color.Blue
    richTextState.config.linkTextDecoration = TextDecoration.Underline

    richTextState.config.codeSpanColor = Color.Yellow
    richTextState.config.codeSpanBackgroundColor = Color.Transparent
    richTextState.config.codeSpanStrokeColor = Color.LightGray

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Anotação",
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
                actions = {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            contentColor = White
                        ),
                        onClick = {}
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp).padding(end = 2.dp),
                            imageVector = Icons.Default.Save,
                            contentDescription = null
                        )
                        Text(
                            text = "Salvar",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                    actionIconContentColor = White,
                )
            )
        }


    ) { innerPadding ->

        NoteEditor(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

        )
    }
}


@Preview(showBackground = true)
@Composable
private fun NoteEditPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        NoteEditScreen(
            onBack = {}
        )
    }
}