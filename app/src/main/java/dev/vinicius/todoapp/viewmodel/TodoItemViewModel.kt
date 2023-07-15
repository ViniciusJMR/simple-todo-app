package dev.vinicius.todoapp.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.vinicius.todoapp.data.local.repository.Repository
import dev.vinicius.todoapp.data.local.repository.impl.TodoItemRepository
import dev.vinicius.todoapp.data.model.TodoItem
import dev.vinicius.todoapp.domain.ListTodoItemUseCase
import dev.vinicius.todoapp.domain.dto.TodoItemDTOOutput
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoItemViewModel @Inject constructor(
    app: Application,
    private val listTodoItemUseCase: ListTodoItemUseCase,
): AndroidViewModel(app){
    private val _todoList = MutableLiveData<State<List<TodoItemDTOOutput>>>()
    val todoList: LiveData<State<List<TodoItemDTOOutput>>> = _todoList

    fun getAll() {
        viewModelScope.launch {
            listTodoItemUseCase()
                .onStart {
                    _todoList.postValue(State.Loading)
                }
                .catch {
                    _todoList.postValue(State.Error(it))
                }
                .collect{
                    _todoList.postValue(State.Success(it))
                }
        }
    }
}
