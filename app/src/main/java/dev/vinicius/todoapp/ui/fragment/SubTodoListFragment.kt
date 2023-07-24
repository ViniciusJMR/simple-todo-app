package dev.vinicius.todoapp.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dev.vinicius.todoapp.databinding.FragmentSubTodoListBinding
import dev.vinicius.todoapp.domain.dto.SubTodoItemShow
import dev.vinicius.todoapp.ui.adapter.SubTodoItemAdapter

class SubTodoListFragment: Fragment() {

    private lateinit var binding:FragmentSubTodoListBinding

    private val adapter by lazy { SubTodoItemAdapter() }
    var subTodoList: MutableList<SubTodoItemShow> = mutableListOf()

    var handleOnAddClick: (SubTodoItemShow) -> (Unit) = {}
    var handleOnDeleteClick: (SubTodoItemShow) -> (Unit) = {}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentSubTodoListBinding.inflate(inflater, container, false)
        binding.rvSubTodoList.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvSubTodoList.layoutManager = layoutManager
        binding.lifecycleOwner = this

        return binding.root
    }

    fun addSubTodo(v: View){
        val x = SubTodoItemShow(0, "TESTE", false)
        Log.d("ADD CLICKED", x.toString())
        handleOnAddClick(x)
        adapter.submitList(subTodoList)
    }
}