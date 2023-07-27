package dev.vinicius.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    app: Application
) : AndroidViewModel(app){
    private val _selectedItem = MutableLiveData<Any>()
    val selectedItem: LiveData<Any> get() =_selectedItem

    fun selectItem(item: Any) {
        _selectedItem.value = item
    }

    fun getSelectedAsLong() = selectedItem.value as Long

}