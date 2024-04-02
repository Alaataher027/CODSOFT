package com.example.to_dolistapp.addTodo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.to_dolistapp.mainActivity.TodosListAdapter
import com.example.to_dolistapp.database.TodoDatabase
import com.example.to_dolistapp.database.model.TodoModel
import com.example.to_dolistapp.databinding.ActivityAddTodoBinding
import java.text.SimpleDateFormat
import java.util.*

class AddTodoActivity : AppCompatActivity() {

    private lateinit var viewModel: AddTodoViewModel
    private lateinit var selectedCalendar: Calendar
    private lateinit var adapter: TodosListAdapter
    private lateinit var viewBinding: ActivityAddTodoBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddTodoBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        viewModel = ViewModelProvider(this).get(AddTodoViewModel::class.java)
        adapter = TodosListAdapter(null)

        onClickBack()
        initViews()
    }

    private fun onClickBack() {
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initViews() {
        viewBinding.addTodoBtn.setOnClickListener {
            addTodoToDataBase()
        }

        viewBinding.calender.setOnClickListener {
            showDatePicker()
        }

        viewBinding.titleEditText.addTextChangedListener {
            viewModel.title.value = it.toString()
        }

        viewBinding.descriptionEditText.addTextChangedListener {
            viewModel.description.value = it.toString()
        }
        selectedCalendar = Calendar.getInstance()

        updateSelectedDateInView()

        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.title.observe(this, { value ->
            Log.e("TAG", "onChanged:$value")
        })
    }

    private fun updateSelectedDateInView() {
        viewBinding.selectTimeValue.text = formatDate(selectedCalendar.time)
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedCalendar.set(Calendar.YEAR, year)
                selectedCalendar.set(Calendar.MONTH, month)
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                showTimePicker()
            },
            selectedCalendar.get(Calendar.YEAR),
            selectedCalendar.get(Calendar.MONTH),
            selectedCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showTimePicker() {
        val timePicker = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedCalendar.set(Calendar.MINUTE, minute)
                updateSelectedDateInView()
            },
            selectedCalendar.get(Calendar.HOUR_OF_DAY),
            selectedCalendar.get(Calendar.MINUTE),
            false
        )
        timePicker.show()
    }

    private fun addTodoToDataBase() {
        if (viewModel.validateFields()) {
            val title = viewModel.title.value
            val description = viewModel.description.value

            if (title != null && description != null) {
                if (title.isEmpty() || description.isEmpty()) {
                    // Show an error message indicating empty fields
                    Toast.makeText(this, "Title and Description are required", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val newTask = TodoModel(
                        title = title,
                        description = description,
                        isDone = false,
                        time = selectedCalendar.time
                    )
                    TodoDatabase.getInstance(this).getTodosDao().insertTodo(newTask)

                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        }
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd, HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }
}