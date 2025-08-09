package com.example.taskclass.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun App(modifier: Modifier = Modifier) {

    val navControl = rememberNavController()

    NavHost(navController = navControl, startDestination = Screen.MAIN.route) {

        composable(Screen.MAIN.route) {
            MainScreen()
        }
    }
}

