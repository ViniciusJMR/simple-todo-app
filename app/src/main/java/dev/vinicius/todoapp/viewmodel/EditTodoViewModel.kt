package dev.vinicius.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.todoapp.domain.dto.TodoItemDTOInput
import dev.vinicius.todoapp.domain.todousecase.UpdateTodoItemUseCase
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTodoViewModel @Inject constructor(
    app: Application,
    private val updateTodoItemUseCase: UpdateTodoItemUseCase
): AndroidViewModel(app){

    private val _updateState = MutableLiveData<State<Unit>>()
    val updateState: LiveData<State<Unit>> = _updateState

    fun save(todo: TodoItemDTOInput){
        viewModelScope.launch {
            updateTodoItemUseCase(todo)
                .onStart{
                    _updateState.postValue(State.Loading)
                }
                .catch {
                    _updateState.postValue(State.Error(it))
                }
                .collect{
                    _updateState.postValue(State.Success(it))
                }
        }

    }

}