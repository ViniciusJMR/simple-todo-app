package dev.vinicius.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.todoapp.domain.SaveTodoItemUseCase
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTodoViewModel @Inject constructor(
    application: Application,
    private val saveTodoItemUseCase: SaveTodoItemUseCase
): AndroidViewModel(application) {

    private val _state = MutableLiveData<State<Unit>>()
    val state: LiveData<State<Unit>> = _state

    fun save(todo: TodoItemDTOInput){
        viewModelScope.launch {
            saveTodoItemUseCase(todo)
                .onStart {
                    _state.postValue(State.Loading)
                }
                .catch {
                    _state.postValue(State.Error(it))
                }
                .collect{
                    _state.postValue(State.Success(it))
                }
        }
    }
}