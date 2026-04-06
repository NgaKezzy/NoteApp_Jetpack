package com.example.noteapp.ui.screens.home

import android.R.attr.label
import android.R.attr.minLines
import android.R.attr.value
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.noteapp.models.Note
import com.example.noteapp.services.ApiService

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberInComposition")
@Composable
fun DetailNoteScreen(
    navController: NavHostController,
    viewModel: HomeViewModel
) {
    val state by viewModel.state.collectAsState()
    val note = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Note>("selected_note")
    val index = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<Int>("index")

    println("index : $index")

    val titleFocusRequester = FocusRequester()
    val descriptionFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    // 👇 Chỉ gọi initDetailNote 1 lần khi note thay đổi
    LaunchedEffect(note) {
        if (note != null) {
            viewModel.initDetailNote(note)
        }
        titleFocusRequester.requestFocus()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Detail Note")

                },
                actions = {
                    IconButton(
                        onClick = {
                            if (index != null) {
                                viewModel.removeNote(index)
                                navController.popBackStack()
                            }
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.Red
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(titleFocusRequester)
                    .fillMaxWidth(),
                value = state.detailTitleNote,
                onValueChange = { value ->
                    viewModel.onDetailTitleChange(value, index ?: 0)
                },
                label = { Text(text = "Title") },
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
                value = state.detailDescriptionNote,
                modifier = Modifier
                    .focusRequester(descriptionFocusRequester)
                    .fillMaxWidth(),
                onValueChange = { value ->
                    viewModel.onDetailDescriptionChange(value, index ?: 0)
                },
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
    }

}
