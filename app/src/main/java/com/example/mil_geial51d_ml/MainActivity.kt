package com.example.mil_geial51d_ml

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mil_geial51d_ml.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: TodoAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter(mutableListOf())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerViewTodoItems.adapter = todoAdapter
        binding.recyclerViewTodoItems.layoutManager = LinearLayoutManager(this)

        binding.buttonAddTodo.setOnClickListener {
            val todoTitle = binding.editTextTodoTitle.text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                binding.editTextTodoTitle.text.clear()
            }
        }

        binding.buttonDeleteTodo.setOnClickListener {
            todoAdapter.deleteTodo()
        }
    }
}