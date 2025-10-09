package com.example.taskclass.common.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun AppCardDefault(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Box(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppCardItemLightPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppCardDefault {
            Column {
                Text("Titulo")
                Text("Conteudo do card")
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun AppCardItemDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppCardDefault {
            Column {
                Text("Titulo")
                Text("Conteudo do card")
            }
        }
    }
}