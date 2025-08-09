package com.example.taskclass.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.taskclass.R
import com.example.taskclass.agenda.AgendaScreen
import com.example.taskclass.events.EventsScreen
import com.example.taskclass.notes.NotesScreen
import kotlinx.coroutines.launch


@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                HorizontalDivider()
                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Disciplinas")
                    },
                    onClick = {},
                    shape = RoundedCornerShape(0.dp),
                    selected = false
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Horários de aula")
                    },
                    onClick = {},
                    shape = RoundedCornerShape(0.dp),
                    selected = false
                )
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.EventAvailable,
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = "Tipos de eventos")
                    },
                    onClick = {},
                    shape = RoundedCornerShape(0.dp),
                    selected = false
                )
            }
        }
    ) {
        MainContent(
            openNavigationDrawer = {
                scope.launch {
                    drawerState.apply {
                        if (isOpen) {
                            close()
                        } else {
                            open()
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun MainContent(modifier: Modifier = Modifier, openNavigationDrawer: () -> Unit) {

    val startDestination = Screen.AGENDA
    val navController = rememberNavController()
    val navBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStack?.destination?.route ?: startDestination.route

    NavHost(navController = navController, startDestination = startDestination.route) {

        composable(Screen.AGENDA.route) {
            AgendaScreen(
                navigationBar = {
                    MainNavigationBar(
                        currentRouter = Screen.valueOf(currentRoute),
                        onChangeNavigation = {
                            navController.navigate(it.route)
                        }
                    )
                },
                openNavigationDrawer = openNavigationDrawer
            )
        }

        composable(Screen.EVENTS.route) {
            EventsScreen(
                navigationBar = {
                    MainNavigationBar(
                        currentRouter = Screen.valueOf(currentRoute),
                        onChangeNavigation = {
                            navController.navigate(it.route)
                        }
                    )
                },
                openNavigationDrawer = openNavigationDrawer
            )
        }

        composable(Screen.NOTES.route) {
            NotesScreen(
                navigationBar = {
                    MainNavigationBar(
                        currentRouter = Screen.valueOf(currentRoute),
                        onChangeNavigation = {
                            navController.navigate(it.route)
                        }
                    )
                },
                openNavigationDrawer = openNavigationDrawer
            )
        }

    }
}

@Composable
fun MainNavigationBar(
    modifier: Modifier = Modifier,
    currentRouter: Screen,
    onChangeNavigation: (Screen) -> Unit
) {

    NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
        NavigationBarItem(
            selected = Screen.AGENDA.route == currentRouter.route,
            onClick = {
                onChangeNavigation(Screen.AGENDA)
            },
            label = {
                Text(text = "Início")
            },
            icon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = null)
            }
        )
        NavigationBarItem(
            selected = Screen.EVENTS.route == currentRouter.route,
            onClick = {
                onChangeNavigation(Screen.EVENTS)
            },
            label = {
                Text(text = "Eventos")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null
                )
            }
        )
        NavigationBarItem(
            selected = Screen.NOTES.route == currentRouter.route,
            onClick = {
                onChangeNavigation(Screen.NOTES)
            },
            label = {
                Text(text = "Notas")
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Notes,
                    contentDescription = null
                )
            }
        )
    }
}