package com.example.to_dolistapp.editTodo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.to_dolistapp.constant
import com.example.to_dolistapp.database.TodoDatabase
import com.example.to_dolistapp.database.model.TodoModel
import com.example.to_dolistapp.databinding.ActivityEditBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class EditActivity : AppCompatActivity() {

    private lateinit var todoModel: TodoModel
    private lateinit var viewBinding: ActivityEditBinding
    private var selectedCalendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityEditBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)

        onClickBack()
        todoModel = intent.getSerializableExtra(constant.Task) as TodoModel

        viewBinding.titleEditText.setText(todoModel.title)
        viewBinding.descriptionEditText.setText(todoModel.description)
        selectedCalendar.time = todoModel.time!!


        viewBinding.selectTimeValue.text = formatDate(selectedCalendar.time)

        viewBinding.calender.setOnClickListener {
            showDatePicker()
        }

        viewBinding.saveBtn.setOnClickListener {
            saveEditedTask()
        }

        viewBinding.calender.setOnClickListener {
            showDatePicker()
        }
    }

    private fun onClickBack() {
        viewBinding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun saveEditedTask() {
        val editedTitle = viewBinding.titleEditText.text.toString()
        val editedDescription = viewBinding.descriptionEditText.text.toString()

        if (editedTitle.isEmpty() || editedDescription.isEmpty()) {
            Toast.makeText(this, "Title and Description are required", Toast.LENGTH_SHORT).show()
        } else {
            todoModel.title = editedTitle
            todoModel.description = editedDescription

            todoModel.time = selectedCalendar.time

            TodoDatabase.getInstance(this).getTodosDao().updateTodo(todoModel)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }


    private fun updateSelectedDateInView() {
        viewBinding.selectTimeValue.text = formatDate(selectedCalendar.time)
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd, HH:mm", Locale.getDefault())
        return dateFormat.format(date)
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
}
