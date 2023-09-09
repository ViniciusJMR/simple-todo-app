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
import dev.vinicius.todoapp.domain.subtodousecase.DeleteSubTodoUseCase
import dev.vinicius.todoapp.domain.subtodousecase.SaveSubTodoUseCase
import dev.vinicius.todoapp.domain.todousecase.DeleteTodoItemUseCase
import dev.vinicius.todoapp.util.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.lang.Math.ceil
import javax.inject.Inject

@HiltViewModel
class DetailTodoViewModel @Inject constructor(
    app: Application,
    private val getTodoDetailById: GetTodoDetailByIdUseCase,
    private val updateSubTodoItemUseCase: UpdateSubTodoItemUseCase,
    private val saveSubTodoUseCase: SaveSubTodoUseCase,
    private val deleteSubTodoUseCase: DeleteSubTodoUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase
) : AndroidViewModel(app) {

    private val _stateTodo = MutableLiveData<State<TodoItemDTOOutput>>()
    val stateTodo: LiveData<State<TodoItemDTOOutput>> = _stateTodo

    private val _stateSubTodo = MutableLiveData<State<MutableList<SubTodoItemShow>>>()
    val stateSubTodo: LiveData<State<MutableList<SubTodoItemShow>>> = _stateSubTodo

    private val _deleteState = MutableLiveData<State<Unit>>()
    val deleteState: LiveData<State<Unit>> = _deleteState

    fun getTodoDetail(id: Long) {
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
                .collect {
                    _stateTodo.postValue(State.Success(it.todoItemOutput))
                    _stateSubTodo.postValue(State.Success(it.subTodoList))
                }
        }
    }

    fun deleteTodo(id: Long) {
        viewModelScope.launch {
            deleteTodoItemUseCase(id)
                .onStart {
                    _deleteState.postValue(State.Loading)
                }
                .catch {
                    _deleteState.postValue(State.Error(it))
                }
                .collect {
                    _deleteState.postValue(State.Success(it))
                }
        }
    }

    fun updateSubTodo(subTodo: SubTodoItemShow) {
        viewModelScope.launch {
            val oldList = getSubTodoList()
            updateSubTodoItemUseCase(subTodo)
                .onStart {
                    _stateSubTodo.postValue(State.Loading)
                }
                .catch {
                    _stateSubTodo.postValue(State.Error(it))
                }
                .collect { newSubTodo ->

                    oldList?.find { subTodo ->
                        subTodo.id == newSubTodo.id
                    }.also { oldSubTodo ->
                        oldSubTodo?.apply {
                            name = newSubTodo.name
                            done = newSubTodo.done
                        }
                    }

                    _stateSubTodo.postValue(State.Success(oldList))
                }
        }
    }

    fun addSubTodo(subTodoName: String) {
        viewModelScope.launch {
            val id = getTodo()!!.id
            val subTodo = SubTodoItemShow(name = subTodoName, done = false)
            val pair = Pair(id, subTodo)
            val list = getSubTodoList()
            saveSubTodoUseCase(pair)
                .onStart {
                    _stateSubTodo.postValue(State.Loading)
                }
                .catch {
                    _stateSubTodo.postValue(State.Error(it))
                }
                .collect {
                    list?.add(it)
                    _stateSubTodo.postValue(State.Success(list))
                }
        }

    }

    fun deleteSubTodo(subTodo: SubTodoItemShow) {
        viewModelScope.launch {
            val list = getSubTodoList()
            deleteSubTodoUseCase(subTodo.id)
                .onStart {
                    _stateSubTodo.postValue(State.Loading)
                }
                .catch {
                    _stateSubTodo.postValue(State.Error(it))
                }
                .collect {
                    list?.removeIf {
                        it.id == subTodo.id
                    }
                    _stateSubTodo.postValue(State.Success(list))
                }
        }
    }

    fun getSubTodoList() =
        (stateSubTodo.value as State.Success)
            .response

    fun getSubTodoProgress(): Int {
        val list = getSubTodoList()
        val ONE_HUNDRED_PERCENT = 100.0
        var count = 0
        list?.filter { it.done }?.forEach { _ ->
            count++
        }

        if (count == 0)
            return 0

        val percentage = kotlin.math.ceil(ONE_HUNDRED_PERCENT / list!!.size * count)
        return percentage.toInt()
    }


    fun getTodo() =
        (stateTodo.value as State.Success)
            .response
}