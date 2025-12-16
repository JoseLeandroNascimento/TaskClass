package com.joseleandro.taskclass.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatLineSpacing
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joseleandro.taskclass.core.data.model.Order
import com.joseleandro.taskclass.ui.theme.TaskClassTheme

data class OrderByOption<T>(
    val label: String,
    val value: T
)

@Composable
fun <T> AppButtonOrderBy(
    modifier: Modifier = Modifier,
    options: List<OrderByOption<Order<T>>> = listOf(),
    value: Order<T>,
    onValueChange: (Order<T>) -> Unit,
    sortDirection: Boolean = true,
    onSortDirectionChange: () -> Unit,
    colorContent: Color = MaterialTheme.colorScheme.onSurface,
    colorBackground: Color = MaterialTheme.colorScheme.background
) {

    var expandedOptions by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min),
        color = colorBackground
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Box() {

                TextButton(
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = colorContent
                    ),
                    contentPadding = PaddingValues(vertical = 2.dp, horizontal = 4.dp),
                    onClick = {
                        expandedOptions = !expandedOptions
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(vertical = 2.dp, horizontal = 8.dp),
                        imageVector = Icons.Default.FormatLineSpacing,
                        contentDescription = null
                    )
                    Text(
                        text = options.find { it.value.selector == value.selector }?.label ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp
                    )
                }

                DropdownMenu(
                    containerColor = MaterialTheme.colorScheme.surface,
                    expanded = expandedOptions,
                    onDismissRequest = {
                        expandedOptions = false
                    }
                ) {

                    options.forEach { opt ->
                        DropdownMenuItem(
                            trailingIcon = if (value.selector == opt.value.selector) {
                                {
                                    Icon(
                                        modifier = Modifier.size(18.dp),
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            } else {
                                null
                            },
                            text = {
                                Text(
                                    text = opt.label,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            onClick = {
                                expandedOptions = false
                                onValueChange(opt.value)
                            }
                        )
                    }
                }
            }

            VerticalDivider(
                modifier = Modifier.height(18.dp),
                color = colorContent.copy(alpha = .8f),
                thickness = .5.dp
            )

            IconButton(

                modifier = Modifier.size(30.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorContent
                ),
                onClick = onSortDirectionChange
            ) {

                Icon(
                    modifier = Modifier
                        .size(20.dp),
                    imageVector = if (sortDirection) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                    contentDescription = null
                )
            }
        }

    }
}

@Preview
@Composable
private fun AppButtonOrderByPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppButtonOrderBy(
            options = listOf(
                OrderByOption(
                    label = "Opção 1",
                    value = Order(String::length)
                ),
                OrderByOption(
                    label = "Opção 2",
                    value = Order(String::length)
                ),
                OrderByOption(
                    label = "Opção 3",
                    value = Order(String::length)
                )
            ),
            value = Order(String::length),
            onValueChange = {},
            onSortDirectionChange = {

            }
        )
    }
}

@Preview
@Composable
private fun AppButtonOrderByDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppButtonOrderBy(
            options = listOf(
                OrderByOption(
                    label = "Opção 1",
                    value = Order(String::length)
                ),
                OrderByOption(
                    label = "Opção 2",
                    value = Order(String::length)
                ),
                OrderByOption(
                    label = "Opção 3",
                    value = Order(String::length)
                )
            ),
            value = Order(String::length),
            onValueChange = {},
            onSortDirectionChange = {

            }
        )
    }
}