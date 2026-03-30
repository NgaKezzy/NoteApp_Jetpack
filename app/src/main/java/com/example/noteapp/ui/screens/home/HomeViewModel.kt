package com.example.noteapp.ui.screens.home

import android.R.attr.value
import androidx.lifecycle.ViewModel
import com.example.noteapp.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.LocalDateTime

data class HomeState(
    val titleNote: String = "",
    val descriptionNote: String = "",
    val notes: List<Note> = emptyList(),
    val isShowDialog: Boolean = false,

)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun addNote() {
        _state.update {
            it.copy(
                notes = it.notes + Note(
                    it.titleNote,
                    it.descriptionNote,
                    LocalDateTime.now()
                ),
                titleNote = "",
                descriptionNote = ""
            )
        }
    }

    fun  onTitleChange(value :String){
         _state.update { it.copy(titleNote = value) }
        println(value)
    }


     fun  onDescriptionChange(value :String){
         _state.update { it.copy(descriptionNote = value) }
           println(value)
    }


    fun showDialog(){
        _state.update { it.copy(isShowDialog = true) }
    }

     fun closeDialog(){
        _state.update { it.copy(isShowDialog = false) }
    }


}
