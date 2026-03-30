package com.example.noteapp.ui.screens.login

import androidx.lifecycle.ViewModel
import com.example.noteapp.models.User
import com.example.noteapp.ui.screens.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginState(
    val username: String = "",
    val password: String = "",
    val users: List<User> = listOf(User("ngakezzy", "123")),
    val isLoginSuccess: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()


    fun onUsernameChange(value: String) {
        _state.update { it.copy(username = value) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value) }
    }

    fun login() {
        val currentState = _state.value
        var isLoginSuccess = false

        for (user in currentState.users) {
            if (user.userName == currentState.username &&
                user.password == currentState.password
            ) {
                isLoginSuccess = true
                break
            }
        }

        if (isLoginSuccess) {
            _state.update {
                it.copy(isLoginSuccess = true)
            }
            println("Login success")
        } else {
            println("Login failed")
        }


    }
}