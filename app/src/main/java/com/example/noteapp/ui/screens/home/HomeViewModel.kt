package com.example.noteapp.ui.screens.home

import android.R.attr.value
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.models.Note
import com.example.noteapp.models.Post
import com.example.noteapp.services.ApiService
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

data class HomeState(
    val titleNote: String = "",
    val descriptionNote: String = "",
    val detailTitleNote: String = "",
    val detailDescriptionNote: String = "",
    val notes: List<Note> = emptyList(),
    val isShowDialog: Boolean = false,
    val posts: List<Post> = emptyList()

    )

@HiltViewModel
open class HomeViewModel @Inject constructor(
     private val api: ApiService
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun addNote() {
        _state.update {
            it.copy(
                notes = it.notes + Note(
                    it.titleNote,
                    it.descriptionNote,
                    LocalDate.now()
                ),
                titleNote = "",
                descriptionNote = ""
            )
        }
    }

    fun onTitleChange(value: String) {
        _state.update { it.copy(titleNote = value) }
        println(value)
    }


    fun onDescriptionChange(value: String) {
        _state.update { it.copy(descriptionNote = value) }
        println(value)
    }


    fun showDialog() {
        _state.update { it.copy(isShowDialog = true) }
    }

    fun closeDialog() {
        _state.update { it.copy(isShowDialog = false) }
    }

    fun onDetailTitleChange(value: String, index: Int) {
        _state.update { currentState ->
            val newList = currentState.notes.toMutableList()

            val oldNote = newList[index]
            newList[index] = oldNote.copy(title = value, createdAt = LocalDate.now())

            currentState.copy(notes = newList, detailTitleNote = value)
        }
    }

    fun onDetailDescriptionChange(value: String, index: Int) {
            _state.update { currentState ->
            val newList = currentState.notes.toMutableList()

            val oldNote = newList[index]
            newList[index] = oldNote.copy(description = value, createdAt = LocalDate.now())

            currentState.copy(notes = newList, detailDescriptionNote = value)
        }
    }

    fun  initDetailNote(note: Note){
        _state.update {
            it.copy(detailTitleNote = note.title, detailDescriptionNote = note.description)
        }

    }

    fun removeNote(index: Int){
        _state.update { currentState -> val newList = currentState.notes.toMutableList()

        if (index in newList.indices) {
            newList.removeAt(index)
        }

        currentState.copy(notes = newList)
        }

    }

    fun getPosts(){
        viewModelScope.launch {
            try {
                val response = api.getPosts()
                _state.update { it.copy(posts = response) }
            Log.i("call api", response.toString())


            }catch (
                e: Exception
            ){
               Log.e("ERROR", e.toString())
            }
        }

    }


}
