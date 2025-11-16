package com.example.taskclass.common.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.taskclass.R
import com.example.taskclass.ui.theme.TaskClassTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppSearchBarScaffold(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    onExpandedChange: (Boolean) -> Unit,
    query: String,
    placeholder: String? = null,
    onQueryChange: (String) -> Unit,
    isLoading: Boolean = false,
    items: List<T> = emptyList(),
    searchItem: @Composable (T) -> Unit,
    key: ((Int, T) -> Any)? = null,
    searchEmpty: (@Composable () -> Unit)? = null,
    searchEmptyMessage: String? = null,
    searchNotFound: (@Composable () -> Unit)? = null,
    searchNotFoundMessage: String? = null,
    content: @Composable () -> Unit
) {

    val focusSearch = remember { FocusRequester() }

    LaunchedEffect(expanded) {
        if (expanded) {
            focusSearch.requestFocus()
        } else {
            focusSearch.freeFocus()
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            if (!expanded) {
                content()
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
            ) {

                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            modifier = Modifier.focusRequester(focusRequester = focusSearch),
                            query = query,
                            onSearch = {},
                            leadingIcon = {
                                IconButton(
                                    onClick = {
                                        onQueryChange("")
                                        onExpandedChange(false)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            },
                            placeholder = {
                                Text(
                                    text = placeholder
                                        ?: stringResource(R.string.search_bar_pesquisar),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { onQueryChange("") },
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null
                                    )
                                }
                            },
                            onExpandedChange = onExpandedChange,
                            expanded = expanded,
                            onQueryChange = onQueryChange,
                        )
                    },
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        dividerColor = MaterialTheme.colorScheme.background,
                    ),

                    onExpandedChange = onExpandedChange,
                    expanded = expanded

                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        when {

                            isLoading -> {
                                CircularProgressIndicator()
                            }

                            query.isEmpty() -> {

                                if (searchEmpty != null)
                                    searchEmpty()
                                else
                                    AppSearchEmpty(
                                        message = searchEmptyMessage
                                            ?: stringResource(R.string.comece_digitando_para_buscar)
                                    )

                            }

                            else -> {

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .align(alignment = Alignment.TopCenter),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {

                                    if (items.isEmpty()) {
                                        item {

                                            if (searchNotFound != null)
                                                searchNotFound()
                                            else
                                                AppSearchNotFound(
                                                    message = searchNotFoundMessage
                                                        ?: stringResource(R.string.nenhuma_informa_o_foi_encontrada)
                                                )

                                        }
                                        return@LazyColumn
                                    }

                                    itemsIndexed(
                                        items = items,
                                        key = key
                                    ) { index, item ->

                                        searchItem(item)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppSearchEmpty(
    message: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.Default.Keyboard,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,

        )
        Text(
            message,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AppSearchNotFound(
    modifier: Modifier = Modifier,
    message: String
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                modifier = Modifier.size(40.dp),
                painter = painterResource(id = R.drawable.search),
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Text(
                text = message,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview
@Composable
private fun AppSearchBarScaffoldPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = false
    ) {
        AppSearchBarScaffold(
            isLoading = false,
            items = emptyList<String>(),
            onQueryChange = {},
            expanded = true,
            onExpandedChange = {},
            query = "",
            searchItem = { item ->

            }
        ) {
            Text(text = "Content")
        }
    }
}

@Preview
@Composable
private fun AppSearchBarScaffoldDarkPreview() {

    TaskClassTheme(
        dynamicColor = false,
        darkTheme = true
    ) {
        AppSearchBarScaffold(
            isLoading = false,
            items = listOf("Item 1", "Item 2", "Item 3"),
            onQueryChange = {},
            expanded = true,
            onExpandedChange = {},
            query = "",
            searchItem = { item ->
                Text(item)
            }
        ) {
            Text(text = "Content")
        }
    }
}