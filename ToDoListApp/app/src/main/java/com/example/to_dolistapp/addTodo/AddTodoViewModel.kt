package com.example.to_dolistapp.addTodo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddTodoViewModel : ViewModel() {
    val title = MutableLiveData<String>("")
    val titleError = MutableLiveData<String>()
    val description = MutableLiveData<String>("")
    val descriptionError = MutableLiveData<String>()

    fun validateFields(): Boolean {

        if (title.value.isNullOrEmpty() || title.value.isNullOrBlank()) {
            titleError.value = "Title Requried"
        } else {
            titleError.value = null
        }
        if (description.value.isNullOrEmpty() || description.value.isNullOrBlank()) {
            descriptionError.value = "Description Required" // Change this line
        } else {
            descriptionError.value = null
        }

        return true
    }
}