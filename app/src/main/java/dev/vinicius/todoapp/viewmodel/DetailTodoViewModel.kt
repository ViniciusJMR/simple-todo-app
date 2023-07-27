package dev.vinicius.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.todoapp.domain.GetTodoDetailByIdUseCase
import dev.vinicius.todoapp.domain.dto.TodoItemDTODetail
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTodoViewModel @Inject constructor(
    app: Application,
    private val getTodoDetailById: GetTodoDetailByIdUseCase
): AndroidViewModel(app){

    private val _state = MutableLiveData<State<TodoItemDTODetail>>()
    val state: LiveData<State<TodoItemDTODetail>> = _state

    fun getTodoDetail(id:Long){
        viewModelScope.launch {
            getTodoDetailById(id)
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