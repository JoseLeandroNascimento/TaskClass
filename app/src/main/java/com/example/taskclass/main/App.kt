package com.example.taskclass.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.taskclass.discipline.presentation.disciplineCreateScreen.DisciplineCreateScreen
import com.example.taskclass.discipline.presentation.disciplineCreateScreen.DisciplineCreateViewModel
import com.example.taskclass.discipline.presentation.disciplineScreen.DisciplineScreen
import com.example.taskclass.discipline.presentation.disciplineScreen.DisciplineViewModel
import com.example.taskclass.events.presentation.eventCreateScreen.EventCreateScreen
import com.example.taskclass.events.presentation.eventCreateScreen.EventCreateViewModel
import com.example.taskclass.events.presentation.eventDetailScreen.EventDetailScreen
import com.example.taskclass.schedules.presentation.newSchedule.NewScheduleScreen
import com.example.taskclass.schedules.presentation.newSchedule.NewScheduleViewModel
import com.example.taskclass.schedules.presentation.schedules.SchedulesScreen
import com.example.taskclass.typeEvents.apresentation.typeEvent.TypeEventsScreen
import com.example.taskclass.typeEvents.apresentation.typeEvent.TypeEventsViewModel

@Composable
fun App(modifier: Modifier = Modifier) {

    val appNavController = rememberNavController()

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
                }
            )
        }

        composable(
            route = Screen.EVENT_DETAIL.route,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) {
            EventDetailScreen(
                onBack = { appNavController.navigateUp() }
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
                },
                onSaveSuccess = {
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
                },
                onSaveSuccess = {
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
                }
            )
        }

        composable(Screen.NEW_EVENT.route) {backStackEntry->

            val viewModel = hiltViewModel<EventCreateViewModel>(backStackEntry)
            EventCreateScreen(
                viewModel = viewModel
            ) {
                appNavController.navigateUp()
            }
        }

        composable(Screen.TYPE_EVENTS.route) {backStackEntry->
            val viewModel = hiltViewModel<TypeEventsViewModel>(backStackEntry)
            TypeEventsScreen(
                viewModel = viewModel,
                onBack =  {
                    appNavController.navigateUp()
                }
            )
        }

    }
}

