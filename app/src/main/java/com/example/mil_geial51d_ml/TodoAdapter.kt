package com.example.mil_geial51d_ml

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mil_geial51d_ml.databinding.ItemTodoBinding

class TodoAdapter(private val todoList: MutableList<Todo>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    fun addTodo(newTodo: Todo) {
        todoList.add(newTodo)
        notifyItemInserted(todoList.size - 1)
    }

    fun deleteTodo() {
        todoList.removeAll { todo -> todo.isChecked}
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(title: TextView, isChecked: Boolean) {
        if(isChecked) {
            title.paintFlags = title.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            title.paintFlags = title.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todoList[position]
        with(holder.binding) {
            textViewTodoTitle.text = currentTodo.title
            checkBoxTodo.isChecked = currentTodo.isChecked
            toggleStrikeThrough(textViewTodoTitle, currentTodo.isChecked)
            checkBoxTodo.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(textViewTodoTitle, isChecked)
                currentTodo.isChecked = !currentTodo.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}