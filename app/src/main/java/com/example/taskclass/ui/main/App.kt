package com.example.taskclass.ui.main

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskclass.common.data.NotificationCenter
import com.example.taskclass.ui.bulletin.presentation.bulletinScreen.BulletinScreen
import com.example.taskclass.ui.bulletin.presentation.bulletinScreen.BulletinViewModel
import com.example.taskclass.ui.discipline.presentation.disciplineCreateScreen.DisciplineCreateScreen
import com.example.taskclass.ui.discipline.presentation.disciplineCreateScreen.DisciplineCreateViewModel
import com.example.taskclass.ui.discipline.presentation.disciplineScreen.DisciplineScreen
import com.example.taskclass.ui.discipline.presentation.disciplineScreen.DisciplineViewModel
import com.example.taskclass.ui.events.presentation.eventAllScreen.EventAllScreen
import com.example.taskclass.ui.events.presentation.eventAllScreen.EventAllViewModel
import com.example.taskclass.ui.events.presentation.eventCreateScreen.EventCreateScreen
import com.example.taskclass.ui.events.presentation.eventCreateScreen.EventCreateViewModel
import com.example.taskclass.ui.events.presentation.eventDetailScreen.EventDetailScreen
import com.example.taskclass.ui.notes.presentation.noteEditorScreen.NoteEditScreen
import com.example.taskclass.ui.notes.presentation.noteEditorScreen.NoteEditorViewModel
import com.example.taskclass.ui.schedules.presentation.newSchedule.NewScheduleScreen
import com.example.taskclass.ui.schedules.presentation.newSchedule.NewScheduleViewModel
import com.example.taskclass.ui.schedules.presentation.schedules.SchedulesScreen
import com.example.taskclass.ui.typeEvents.presentation.typeEvent.TypeEventsScreen
import com.example.taskclass.ui.typeEvents.presentation.typeEvent.TypeEventsViewModel
import com.example.taskclass.ui.typeEvents.presentation.typeEventCreate.TypeEventCreateScreen
import com.example.taskclass.ui.typeEvents.presentation.typeEventCreate.TypeEventCreateViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun App() {

    val appNavController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        NotificationCenter.events.collect { event ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = event.message,
                    withDismissAction = true,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        snackbarData = it,
                    )
                }
            )
        },

        ) {

        NavHost(navController = appNavController, startDestination = Screen.MAIN.route) {

            composable(Screen.MAIN.route) {
                MainScreen(
                    onNavigationNewSchedule = {
                        appNavController.navigate(Screen.NEW_SCHEDULES.route)
                    },
                    onNavigationNewEvent = {
                        appNavController.navigate(Screen.NEW_EVENT.route)
                    },
                    onNavigationDrawer = { screen ->
                        appNavController.navigate(screen.route) {
                            if (screen == Screen.MAIN) {
                                popUpTo(Screen.MAIN.route) { inclusive = false }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onEditSchedule = { scheduleId ->
                        appNavController.navigate(Screen.EDIT_SCHEDULES.withArgs(scheduleId.toString()))
                    },
                    onSelectedEvent = { eventId ->
                        appNavController.navigate(Screen.EVENT_DETAIL.withArgs(eventId.toString()))
                    },
                    onNavigateToAllEvents = {
                        appNavController.navigate(Screen.EVENT_ALL.route)
                    },
                    onNavigationNewNote = {
                        appNavController.navigate(Screen.NOTE_CREATE.route)
                    }
                )
            }

            composable(
                route = Screen.EVENT_DETAIL.route,
                arguments = listOf(navArgument("eventId") { type = NavType.IntType })
            ) {
                EventDetailScreen(
                    onBack = { appNavController.navigateUp() },
                    onEdit = {
                        appNavController.navigate(Screen.EVENT_EDIT.withArgs(it.toString()))
                    }
                )
            }


            composable(Screen.DISCIPLINE.route) { backStackEntry ->
                val viewModel = hiltViewModel<DisciplineViewModel>(backStackEntry)
                DisciplineScreen(
                    onBack = {
                        appNavController.navigateUp()
                    },
                    onEditDiscipline = {
                        appNavController.navigate(Screen.DISCIPLINE_EDIT.withArgs(it.toString()))
                    },
                    viewModel = viewModel,
                    onCreateDiscipline = {
                        appNavController.navigate(Screen.DISCIPLINE_CREATE.route) {
                            popUpTo(Screen.DISCIPLINE.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable(Screen.DISCIPLINE_CREATE.route) { backStackEntry ->
                val viewModel = hiltViewModel<DisciplineCreateViewModel>(backStackEntry)
                DisciplineCreateScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

            composable(
                route = Screen.DISCIPLINE_EDIT.route,
                arguments = listOf(navArgument("disciplineId") { type = NavType.StringType })
            ) { backStackEntry ->

                val viewModel = hiltViewModel<DisciplineCreateViewModel>(backStackEntry)
                DisciplineCreateScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

            composable(Screen.SCHEDULES.route) {
                SchedulesScreen {
                    appNavController.navigateUp()
                }
            }

            composable(Screen.NEW_SCHEDULES.route) { backStackEntry ->

                val viewModel = hiltViewModel<NewScheduleViewModel>(backStackEntry)
                NewScheduleScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    },
                    addDiscipline = {
                        appNavController.navigate(Screen.DISCIPLINE_CREATE.route)
                    }
                )
            }

            composable(
                route = Screen.EDIT_SCHEDULES.route,
                arguments = listOf(navArgument("scheduleId") {
                    type = NavType.StringType
                })

            ) { backStackEntry ->

                val viewModel = hiltViewModel<NewScheduleViewModel>(backStackEntry)
                NewScheduleScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    },
                    addDiscipline = {
                        appNavController.navigate(Screen.DISCIPLINE_CREATE.route)
                    }
                )
            }

            composable(Screen.NEW_EVENT.route) { backStackEntry ->

                val viewModel = hiltViewModel<EventCreateViewModel>(backStackEntry)
                EventCreateScreen(
                    viewModel = viewModel,
                    addTypeEvent = {
                        appNavController.navigate(Screen.TYPE_EVENT_CREATE.route)
                    },
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

            composable(
                route = Screen.EVENT_EDIT.route,
                arguments = listOf(navArgument("eventId") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                val viewModel = hiltViewModel<EventCreateViewModel>(backStackEntry)
                EventCreateScreen(
                    viewModel = viewModel,
                    addTypeEvent = {
                        appNavController.navigate(Screen.TYPE_EVENT_CREATE.route)
                    },
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

            composable(Screen.EVENT_ALL.route) { backStackEntry ->

                val viewModel = hiltViewModel<EventAllViewModel>(backStackEntry)

                EventAllScreen(
                    viewModel = viewModel,
                    onCreateNewEvent = {
                        appNavController.navigate(Screen.NEW_EVENT.route)
                    },
                    onEditNavigation = {
                        appNavController.navigate(Screen.EVENT_EDIT.withArgs(it.toString()))
                    },
                    onBack = {
                        appNavController.navigateUp()
                    },
                    onViewEventNavigation = {
                        appNavController.navigate(Screen.EVENT_DETAIL.withArgs(it.toString()))
                    }
                )
            }

            composable(Screen.TYPE_EVENTS.route) { backStackEntry ->
                val viewModel = hiltViewModel<TypeEventsViewModel>(backStackEntry)
                TypeEventsScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    },
                    onEditNavigation = {
                        appNavController.navigate(Screen.TYPE_EVENT_EDIT.withArgs(it.toString()))
                    },
                    onCreateNavigation = {
                        appNavController.navigate(Screen.TYPE_EVENT_CREATE.route)
                    }
                )
            }

            composable(Screen.TYPE_EVENT_CREATE.route) { backStackEntry ->
                val viewModel = hiltViewModel<TypeEventCreateViewModel>(backStackEntry)
                TypeEventCreateScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

            composable(
                route = Screen.TYPE_EVENT_EDIT.route,
                arguments = listOf(navArgument("typeId") {
                    type = NavType.StringType
                })

            ) { backStackEntry ->

                val viewModel = hiltViewModel<TypeEventCreateViewModel>(backStackEntry)
                TypeEventCreateScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

            composable(Screen.NOTE_CREATE.route) { backStackEntry ->

                val viewModel = hiltViewModel<NoteEditorViewModel>(backStackEntry)
                NoteEditScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

            composable(Screen.BULLETINS.route) { backStackEntry ->

                val viewModel = hiltViewModel<BulletinViewModel>(backStackEntry)

                BulletinScreen(
                    viewModel = viewModel,
                    onBack = {
                        appNavController.navigateUp()
                    }
                )
            }

        }
    }
}

