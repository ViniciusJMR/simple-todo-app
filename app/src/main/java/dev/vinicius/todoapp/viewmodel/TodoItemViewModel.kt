package dev.vinicius.todoapp.viewmodel


import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.todoapp.domain.todousecase.ListTodoItemUseCase
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.domain.todousecase.DeleteTodoItemUseCase
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemViewModel @Inject constructor(
    app: Application,
    private val listTodoItemUseCase: ListTodoItemUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase
): AndroidViewModel(app){
    private val _todoList = MutableLiveData<State<MutableList<TodoItemDTOOutput>>>()
    val todoList: LiveData<State<MutableList<TodoItemDTOOutput>>> = _todoList

    fun getAll() {
        viewModelScope.launch {
            listTodoItemUseCase()
                .onStart {
                    _todoList.postValue(State.Loading)
                }
                .catch {
                    Log.e("VIEWMODEL_TAG", it.message ?: "Erro Estranho")
                    _todoList.postValue(State.Error(it))
                }
                .collect{
                    _todoList.postValue(State.Success(it))
                }
        }
    }

    fun deleteTodo(position: Int) {
        viewModelScope.launch {
            val list = getTodoList()
            val id = list!![position].id
            deleteTodoItemUseCase(id)
                .onStart {
                    _todoList.postValue(State.Loading)
                }
                .catch {

                    _todoList.postValue(State.Error(it))
                }
                .collect{
                    list!!.removeIf{ toBeDeleted ->
                        toBeDeleted.id == id
                    }
                    _todoList.postValue(State.Success(list))
                }
        }
    }

    fun getTodoList() =
        (todoList.value as State.Success)
            .response
}
