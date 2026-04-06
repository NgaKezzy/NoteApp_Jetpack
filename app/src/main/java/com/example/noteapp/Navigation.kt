package com.example.noteapp

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.ui.screens.home.DetailNoteScreen
import com.example.noteapp.ui.screens.home.HomeScreen
import com.example.noteapp.ui.screens.home.HomeViewModel
import com.example.noteapp.ui.screens.login.LoginScreen

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home-screen")
    object LoginScreen : Screen("login-screen")
    object DetailNoteScreen : Screen("detail-note-screen")
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home_graph"
    ) {

        // Nhóm Home + Detail vào chung 1 navigation graph
        // để share cùng 1 HomeViewModel
        navigation(
            startDestination = Screen.HomeScreen.route,
            route = "home_graph"
        ) {
            composable(Screen.HomeScreen.route) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry("home_graph")
                }
                val viewModel: HomeViewModel = hiltViewModel(parentEntry)
                HomeScreen(navController, viewModel)
            }

            composable(Screen.DetailNoteScreen.route) {
                val parentEntry = remember(it) {
                    navController.getBackStackEntry("home_graph")
                }
                val viewModel: HomeViewModel = hiltViewModel(parentEntry)
                DetailNoteScreen(navController, viewModel)
            }
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(navController, hiltViewModel())
        }
    }
}
