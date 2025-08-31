package com.example.taskclass.common.composables

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdown(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    content: @Composable ColumnScope.()-> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = MenuAnchorType.PrimaryEditable),
            value = value,
            onValueChange = { },
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium
                )
            },
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                )
            },
            singleLine = true,
        )

        ExposedDropdownMenu(
            modifier = Modifier.heightIn(max = 300.dp),
            matchTextFieldWidth = true,
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            content()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun AppDropdownPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppDropdown(
            label = "Label de teste",
            value = "Teste",
        ){
            DropdownMenuItem(
                text = {
                    Text(text = "Domingo")
                },
                onClick = {},
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Segunda")
                },
                onClick = {},
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Terça")
                },
                onClick = {},
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Quarta")
                },
                onClick = {},
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Quinta")
                },
                onClick = {},
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Sexta")
                },
                onClick = {},
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Sábado")
                },
                onClick = {},
                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
            )
        }
    }
}