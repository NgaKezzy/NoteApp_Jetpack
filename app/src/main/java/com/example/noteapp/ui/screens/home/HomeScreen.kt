package com.example.noteapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.Screen
import com.example.noteapp.models.Note
import com.example.noteapp.ui.screens.login.LoginScreenContent
import com.example.noteapp.ui.screens.login.LoginState
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    HomeScreenContent(
        navController = navController,
        state = state,
        viewModel,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onAddNote = viewModel::addNote
    )

}

@Composable
fun HomeScreenContent(
    navController: NavHostController,
    state: HomeState, viewModel: HomeViewModel? = null,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddNote: () -> Unit
) {


    // 👇 DIALOG ĐẶT Ở ĐÂY
    if (state.isShowDialog) {

        val titleFocusRequester = FocusRequester()
        val descriptionFocusRequester = FocusRequester()
        val focusManager = LocalFocusManager.current

        // 👇 auto focus khi mở dialog
        LaunchedEffect(Unit) {
            titleFocusRequester.requestFocus()
        }
        AlertDialog(
            onDismissRequest = { viewModel?.closeDialog() },
            title = { Text("Add Note") },
            text = {
                Column {
                    OutlinedTextField(
                        value = state.titleNote,
                        onValueChange = onTitleChange,
                        label = { Text("Title") },
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next // nút Enter thành Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                descriptionFocusRequester.requestFocus() // 👉 chuyển xuống description
                            }
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = state.descriptionNote,
                        onValueChange = onDescriptionChange,
                        label = { Text("Description") },
                        shape = RoundedCornerShape(12.dp),
                        minLines = 3,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus() // ẩn keyboard
                            }
                        )


                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    onAddNote()
                    viewModel?.closeDialog()
                }) {
                    Text("Add")
                }
            }
        )
    }

    Scaffold(

        floatingActionButton = {
            Button(onClick = {
                viewModel?.showDialog()
            }) {
                Text("Add")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding) // 👈 áp padding
        ) {
            items(state.notes.size) { index ->   // 👈 dùng items thay vì itemsIndexed
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.Cyan)
                            .clickable {
                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.apply {
                                        set("selected_note", state.notes[index])
                                        set("index", index)
                                    }
                                navController.navigate(Screen.DetailNoteScreen.route)
                            }
                            .padding(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Tittle: ${state.notes[index].title}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.W600
                                )
                                Text(
                                    text = "Time: ${state.notes[index].createdAt}",
                                    fontSize = 14.sp,
                                )
                            }
                            Text(text = "Description: ${state.notes[index].description}")
                        }

                    }
                    if (index < state.notes.size - 1)
                        Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenContent(
        navController = rememberNavController(),
        state = HomeState(
            isShowDialog = false,
            titleNote = "",
            descriptionNote = "",
            notes = listOf(
                Note(
                    "123",
                    "fsdfsd",
                    LocalDate.now()
                )
            ) // fake data cho preview đẹp hơn
        ),
        onTitleChange = {},
        onDescriptionChange = {},
        onAddNote = {}
    )
}
