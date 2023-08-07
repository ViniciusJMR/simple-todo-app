package dev.vinicius.todoapp.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.domain.dto.TodoItemDTODetail
import dev.vinicius.todoapp.domain.todousecase.DeleteTodoItemUseCase
import dev.vinicius.todoapp.domain.todousecase.GetAllTodoDetailUseCase
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemViewModel @Inject constructor(
    app: Application,
    private val getAllTodoDetailUseCase: GetAllTodoDetailUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
): AndroidViewModel(app){
    private val _todoList = MutableLiveData<State<MutableList<TodoItemDTODetail>>>()
    val todoList: LiveData<State<MutableList<TodoItemDTODetail>>> = _todoList

    private val _subTodoList = MutableLiveData<State<List<SubTodoItemShow>>>()
    val subTodoList: LiveData<State<List<SubTodoItemShow>>> = _subTodoList

    fun getAll() {
        viewModelScope.launch {
            getAllTodoDetailUseCase()
                .onStart {
                    _todoList.postValue(State.Loading)
                }
                .catch {
                    Log.e("VIEWMODEL_TAG", it.message ?: "Erro Estranho")
                    _todoList.postValue(State.Error(it))
                }
                .collect{
                    Log.d("VIEWMODEL", it.toString())
                    _todoList.postValue(State.Success(it))
                }
        }
    }

    fun deleteTodo(position: Int) {
        viewModelScope.launch {
            val list = getTodoList()
            val id = list!![position].todoItemOutput!!.id
            deleteTodoItemUseCase(id)
                .onStart {
                    _todoList.postValue(State.Loading)
                }
                .catch {

                    _todoList.postValue(State.Error(it))
                }
                .collect{
                    list!!.removeIf{ toBeDeleted ->
                        toBeDeleted.todoItemOutput!!.id == id
                    }
                    _todoList.postValue(State.Success(list))
                }
        }
    }


    fun getTodoList() =
        (todoList.value as State.Success)
            .response
}
