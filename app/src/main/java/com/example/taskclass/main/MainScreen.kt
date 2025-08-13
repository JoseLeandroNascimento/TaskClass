package com.example.taskclass.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
fun MainScreen(
    onNavigationDrawer: (Screen) -> Unit,
    onNavigationNewSchedule:()-> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val startDestination = remember { Screen.AGENDA }
    val mainNavController = rememberNavController()

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: startDestination.route

    val currentScreen = remember(currentRoute) {
        Screen.entries.find { it.route == currentRoute } ?: startDestination
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onCloseDrawer = { scope.launch { drawerState.close() } },
                onNavigationDrawer = { screen ->
                    onNavigationDrawer(screen)
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        MainContent(
            navController = mainNavController,
            startDestination = startDestination,
            currentScreen = currentScreen,
            onNavigationNewSchedule = onNavigationNewSchedule,
            openNavigationDrawer = {
                scope.launch {
                    if (drawerState.isOpen) drawerState.close() else drawerState.open()
                }
            }
        )
    }
}

@Composable
private fun DrawerContent(
    onCloseDrawer: () -> Unit,
    onNavigationDrawer: (Screen) -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(12.dp))

        DrawerItem(Icons.AutoMirrored.Filled.MenuBook, "Disciplinas") {
            onNavigationDrawer(Screen.DISCIPLINE)
            onCloseDrawer()
        }
        DrawerItem(Icons.Default.AccessTime, "Horários de aula") {
            onNavigationDrawer(Screen.SCHEDULES)
            onCloseDrawer()
        }
        DrawerItem(Icons.Default.EventAvailable, "Tipos de eventos") {
            onNavigationDrawer(Screen.TYPE_EVENTS)
            onCloseDrawer()
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            selectedTextColor = MaterialTheme.colorScheme.primary,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        selected = false
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    openNavigationDrawer: () -> Unit,
    startDestination: Screen,
    currentScreen: Screen,
    navController: NavHostController,
    onNavigationNewSchedule:()-> Unit
) {

    Scaffold(
        topBar = {
            MainTopBar(
                currentScreen = currentScreen,
                openNavigationDrawer = openNavigationDrawer
            )
        },
        bottomBar = {
            MainNavigationBar(
                currentRouter = currentScreen,
                onChangeNavigation = { screen ->
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentScreen == Screen.AGENDA) {
                FloatingActionButton(
                    onClick = onNavigationNewSchedule,
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar evento")
                }
            }

            if (currentScreen == Screen.EVENTS) {
                FloatingActionButton(
                    onClick = {},
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar evento")
                }
            }
        }

    ) { innerPadding ->

        NavHost(
            modifier = Modifier
                .padding(innerPadding),
            navController = navController,
            startDestination = startDestination.route,
        ) {
            composable(Screen.AGENDA.route) {
                AgendaScreen()
            }
            composable(Screen.EVENTS.route) {
                EventsScreen()
            }
            composable(Screen.NOTES.route) {
                NotesScreen()
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(currentScreen: Screen, openNavigationDrawer: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = when (currentScreen) {
                    Screen.AGENDA -> "Horários da Semana"
                    Screen.EVENTS -> "Eventos"
                    else -> "Notas"
                },
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = openNavigationDrawer
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        }
    )
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
            onClick = { onChangeNavigation(Screen.AGENDA) },
            label = { Text("Início") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        NavigationBarItem(
            selected = Screen.EVENTS.route == currentRouter.route,
            onClick = { onChangeNavigation(Screen.EVENTS) },
            label = { Text("Eventos") },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) }
        )
        NavigationBarItem(
            selected = Screen.NOTES.route == currentRouter.route,
            onClick = { onChangeNavigation(Screen.NOTES) },
            label = { Text("Anotações") },
            icon = { Icon(Icons.Default.Draw, contentDescription = null) }
        )
    }
}