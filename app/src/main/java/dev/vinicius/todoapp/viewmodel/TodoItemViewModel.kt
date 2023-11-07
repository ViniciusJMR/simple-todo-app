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
import dev.vinicius.todoapp.domain.dto.TodoItemDTOFinishTodo
import dev.vinicius.todoapp.domain.todousecase.DeleteTodoItemUseCase
import dev.vinicius.todoapp.domain.todousecase.GetAllNotFinishedTodoUseCase
import dev.vinicius.todoapp.domain.todousecase.GetAllTodoDetailUseCase
import dev.vinicius.todoapp.domain.todousecase.UpdateTodoItemDoneUseCase
import dev.vinicius.todoapp.domain.todousecase.UpdateTodoItemUseCase
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoItemViewModel @Inject constructor(
    app: Application,
    private val getAllNotFinishedTodoUseCase: GetAllNotFinishedTodoUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val updateTodoItemDoneUseCase: UpdateTodoItemDoneUseCase
): AndroidViewModel(app){

    companion object {
        val TAG = "TodoItemViewModel"
    }
    private val _todoList = MutableLiveData<State<MutableList<TodoItemDTODetail>>>()
    val todoList: LiveData<State<MutableList<TodoItemDTODetail>>> = _todoList

    private val _state = MutableLiveData<State<Unit>>()
    val state: LiveData<State<Unit>> = _state

    private val _filteredTodoList = MutableLiveData<MutableList<TodoItemDTODetail>?>()
    val filteredTodoList: LiveData<MutableList<TodoItemDTODetail>?> = _filteredTodoList

    private val _subTodoList = MutableLiveData<State<List<SubTodoItemShow>>>()
    val subTodoList: LiveData<State<List<SubTodoItemShow>>> = _subTodoList

    fun getAll() {
        viewModelScope.launch {
            getAllNotFinishedTodoUseCase()
                .onStart {
                    _todoList.postValue(State.Loading)
                }
                .catch {
                    Log.e(TAG, it.message ?: "Erro Estranho")
                    _todoList.postValue(State.Error(it))
                }
                .collect {
                    Log.d(TAG, it.toString())
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
                    list.removeIf{ toBeDeleted ->
                        toBeDeleted.todoItemOutput!!.id == id
                    }
                    _todoList.postValue(State.Success(list))
                }
        }
    }

    fun updateTodoDone(todoItem: TodoItemDTODetail, forceSave: Boolean = false) {
        viewModelScope.launch {
            val list = getTodoList()
            val todoDoneDTO = TodoItemDTOFinishTodo(todoItem, forceSave)
            updateTodoItemDoneUseCase(todoDoneDTO)
                .catch {
                    Log.d(TAG, "updateTodoDone(Error): ${it.message}")
//                    todoItem.todoItemOutput!!.done = false
                    _state.postValue(State.Error(it))
//                    _todoList.postValue(State.Success(list))
                }
                .collect{
                    Log.d(TAG, "updateTodoDone: updated successfully")
                    list?.removeIf{ todo ->
                        todo.todoItemOutput?.id == todoItem.todoItemOutput?.id
                    }
                    _todoList.postValue(State.Success(list))
                    _state.postValue(State.Success(it))
                }
        }
    }

    fun getTodoList() =
        (todoList.value as State.Success)
            .response

    fun filterTodoList(query: String){
        val list = getTodoList()
        val filteredList = list?.filter {
            it.todoItemOutput?.name?.contains(query, true)!!
        }?.toMutableList()
        _filteredTodoList.postValue(filteredList)
    }
}
