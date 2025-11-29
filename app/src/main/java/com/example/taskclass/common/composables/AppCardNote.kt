package com.example.taskclass.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.example.taskclass.ui.theme.TaskClassTheme

@Composable
fun AppCardNote(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {

    val CARD_CUT_RADIUS = 30.dp

    Box {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = CutTopEndRoundedShape(
                roundedRadius = CornerSize(8.dp),
                cutSize = CornerSize(CARD_CUT_RADIUS)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),

            ) {
            Surface(
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = CARD_CUT_RADIUS
                ),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    content()
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(CARD_CUT_RADIUS)
                .height(CARD_CUT_RADIUS)
                .padding(top = 4.dp, end = 4.dp)
                .graphicsLayer() {
                    shadowElevation = 6.dp.toPx()
                    shape = CutCornerShape(
                        topStart = 0.dp,
                        topEnd = CARD_CUT_RADIUS,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                    clip = false
                }
                .background(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    shape = CutCornerShape(
                        topStart = 0.dp,
                        topEnd = CARD_CUT_RADIUS,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .width(CARD_CUT_RADIUS)
                .height(CARD_CUT_RADIUS)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = CutCornerShape(
                        topStart = 0.dp,
                        topEnd = CARD_CUT_RADIUS,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        )
    }
}

@Preview
@Composable
private fun AppCardNotePreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppCardNote {
            Text(text = LoremIpsum(80).values.joinToString(" "))
        }
    }
}

@Preview
@Composable
private fun AppCardNoteDarkPreview() {
    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppCardNote {
            Text(
                textAlign = TextAlign.Justify,
                text = LoremIpsum(80).values.joinToString(" ")
            )
        }
    }
}