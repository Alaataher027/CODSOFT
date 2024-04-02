package com.example.to_dolistapp.mainActivity

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolistapp.addTodo.AddTodoActivity
import com.example.to_dolistapp.constant
import com.example.to_dolistapp.database.TodoDatabase
import com.example.to_dolistapp.database.model.TodoModel
import com.example.to_dolistapp.databinding.ActivityMainBinding
import com.example.to_dolistapp.databinding.OnClickItemDialogBinding
import com.example.to_dolistapp.editTodo.EditActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity(), OnItemClicked, OnItemDeleteClick {

    private lateinit var todosRecyclerView: RecyclerView
    private lateinit var adapter: TodosListAdapter
    private lateinit var viewBinding: ActivityMainBinding
    private val ADD_TODO_REQUEST_CODE = 1
    private val EDIT_TODO_REQUEST_CODE = 1
    private var originalTasks: List<TodoModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        initializeRecyclerView()

        viewBinding.addButton.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivityForResult(intent, ADD_TODO_REQUEST_CODE)
        }

        viewBinding.calendar.setOnClickListener {
            showDatePickerDialog()
        }

        viewBinding.tasksTitle.setOnClickListener {
            loadAllTasks()
        }


        updateTodayDay()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TODO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshRecyclerView()
        } else if (requestCode == EDIT_TODO_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshRecyclerView()
        }
    }

    private fun loadAllTasks() {
        val allTasks = TodoDatabase.getInstance(this).getTodosDao().getAllTodos()
        adapter.updateData(allTasks)

        if (allTasks.isEmpty()) {
            viewBinding.noTaskImg.visibility = View.VISIBLE
            viewBinding.noTaskFound.visibility = View.VISIBLE
        } else {
            viewBinding.noTaskImg.visibility = View.INVISIBLE
            viewBinding.noTaskFound.visibility = View.INVISIBLE
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            filterTasksByDate(selectedDate)
        }

        datePicker.show(supportFragmentManager, "Datepicker")
    }


    private fun filterTasksByDate(selectedDate: Date) {
        val allTasks = TodoDatabase.getInstance(this).getTodosDao().getAllTodos()

        val filteredTasks = allTasks.filter { task ->
            val taskDate = task.time
            isSameDate(taskDate, selectedDate)
        }

        if (filteredTasks.isNullOrEmpty()) {
            adapter.updateData(emptyList())
        } else {
            adapter.updateData(filteredTasks)
        }
    }


    private fun isSameDate(date1: Date?, date2: Date): Boolean {
        return date1?.let {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFormat.format(it) == dateFormat.format(date2)
        } ?: false
    }


    private fun initializeRecyclerView() {
        todosRecyclerView = viewBinding.todosRecyclerView
        adapter = TodosListAdapter(null)
        todosRecyclerView.adapter = adapter

        val items = TodoDatabase.getInstance(this).getTodosDao().getAllTodos()
        adapter.updateData(items)
        adapter.onItemClicked = this
        adapter.onItemDeleteClick = this

        if (items.isEmpty()) {
            viewBinding.noTaskImg.visibility = View.VISIBLE
            viewBinding.noTaskFound.visibility = View.VISIBLE
        } else {
            viewBinding.noTaskImg.visibility = View.INVISIBLE
            viewBinding.noTaskFound.visibility = View.INVISIBLE
        }
    }


    override fun onItemClick(todoModel: TodoModel) {
        val dialogBinding = OnClickItemDialogBinding.inflate(layoutInflater)
        val alertDialogBuilder = AlertDialog.Builder(this)
            .setView(dialogBinding.root)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        dialogBinding.option1Button.setOnClickListener {
            updateTodoTask(todoModel)
            alertDialog.dismiss()

        }
        dialogBinding.option2Button.setOnClickListener {
            makeDone(todoModel, true)
            alertDialog.dismiss()
        }
    }


    override fun onItemDeleteClick(position: Int, task: TodoModel) {
        deleteTask(task)
    }

    private fun refreshRecyclerView() {
        val newListOfTasks = TodoDatabase.getInstance(this).getTodosDao().getAllTodos()
        adapter.updateData(newListOfTasks)

        originalTasks = newListOfTasks

        if (newListOfTasks.isEmpty()) {
            viewBinding.noTaskImg.visibility = View.VISIBLE
            viewBinding.noTaskFound.visibility = View.VISIBLE
        } else {
            viewBinding.noTaskImg.visibility = View.INVISIBLE
            viewBinding.noTaskFound.visibility = View.INVISIBLE
        }
    }


    private fun updateTodoTask(todoModel: TodoModel) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(constant.Task, todoModel)
        startActivityForResult(intent, EDIT_TODO_REQUEST_CODE)

    }

    private fun deleteTask(todoModel: TodoModel) {
        TodoDatabase.getInstance(this).getTodosDao().deleteTodo(todoModel)
        refreshRecyclerView()
    }

    private fun makeDone(todoModel: TodoModel, markDone: Boolean) {
        todoModel.isDone = markDone
        TodoDatabase.getInstance(this).getTodosDao().updateTodo(todoModel)
        refreshRecyclerView()
    }

    private fun updateTodayDay() {
        val calendar = Calendar.getInstance()
        val dayOfWeek =
            calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault())
        viewBinding.todayDay.text = dayOfWeek
    }
}