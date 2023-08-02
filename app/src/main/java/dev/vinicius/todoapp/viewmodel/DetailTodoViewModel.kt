package dev.vinicius.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.todoapp.domain.todousecase.GetTodoDetailByIdUseCase
import dev.vinicius.todoapp.domain.subtodousecase.UpdateSubTodoItemUseCase
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailTodoViewModel @Inject constructor(
    app: Application,
    private val getTodoDetailById: GetTodoDetailByIdUseCase,
    private val updateSubTodoItemUseCase: UpdateSubTodoItemUseCase
): AndroidViewModel(app){

    private val _stateTodo = MutableLiveData<State<TodoItemDTOOutput>>()
    val stateTodo: LiveData<State<TodoItemDTOOutput>> = _stateTodo

    private val _stateSubTodo = MutableLiveData<State<MutableList<SubTodoItemShow>>>()
    val stateSubTodo = _stateSubTodo

    fun getTodoDetail(id:Long){
        viewModelScope.launch {
            getTodoDetailById(id)
                .onStart {
                    _stateTodo.postValue(State.Loading)
                    _stateSubTodo.postValue(State.Loading)
                }
                .catch {
                    _stateTodo.postValue(State.Error(it))
                    _stateSubTodo.postValue(State.Error(it))
                }
                .collect{
                    _stateTodo.postValue(State.Success(it.todoItemOutput))
                    _stateSubTodo.postValue(State.Success(it.subTodoList))
                }
        }
    }

    fun updateSubTodo(subTodo: SubTodoItemShow){
        viewModelScope.launch {
            val oldList = getSubTodoList()
            updateSubTodoItemUseCase(subTodo)
                .onStart {
                    _stateSubTodo.postValue(State.Loading)
                }
                .catch {
                    _stateSubTodo.postValue(State.Error(it))
                }
                .collect{ newSubTodo ->
                    val oldSubTodo = oldList?.find { subTodo ->
                        subTodo.id == newSubTodo.id
                    }
                    oldSubTodo?.name = newSubTodo.name
                    oldSubTodo?.done = newSubTodo.done

                    _stateSubTodo.postValue(State.Success(oldList))
                }
        }
    }

    fun deleteSubTodo(todo: SubTodoItemShow){
    }

    fun getSubTodoList() =
        (stateSubTodo.value as State.Success)
            .response
}