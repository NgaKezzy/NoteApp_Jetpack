package com.example.noteapp.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.Screen
import com.example.noteapp.ui.screens.home.HomeViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

     // 👇 listen state change
    LaunchedEffect(state.isLoginSuccess) {
        if (state.isLoginSuccess) {
            navController.navigate(Screen.HomeScreen.route)
        }
    }


    LoginScreenContent(
        state = state,
        onUsernameChange = viewModel::onUsernameChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogin = viewModel::login
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.username,
            onValueChange = onUsernameChange,
            label = { Text("Username") },
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            shape = RoundedCornerShape(12.dp),
             visualTransformation = PasswordVisualTransformation()

        )
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = onLogin) {
            Text("Login")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        state = LoginState(
            username = "demo",
            password = "123"
        ),
        onUsernameChange = {},
        onPasswordChange = {},
        onLogin = {}
    )
}