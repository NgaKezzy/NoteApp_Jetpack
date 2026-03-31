package com.example.noteapp

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        startDestination = Screen.HomeScreen.route
    ) {

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController,hiltViewModel())
        }

         composable(Screen.LoginScreen.route) {
             LoginScreen(navController,hiltViewModel())
        }

        composable(Screen.DetailNoteScreen.route) {
            DetailNoteScreen(navController, hiltViewModel())
        }


    }
}
