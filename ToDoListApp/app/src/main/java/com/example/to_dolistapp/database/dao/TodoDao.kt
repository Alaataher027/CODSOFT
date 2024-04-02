package com.example.to_dolistapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.to_dolistapp.database.model.TodoModel
import java.util.Date

@Dao
interface TodoDao {
    @Insert
    fun insertTodo(todo: TodoModel)

    @Delete
    fun deleteTodo(todo: TodoModel)

    @Update
    fun updateTodo(todo: TodoModel)

    @Query("SELECT * FROM todo_table")
    fun getAllTodos(): List<TodoModel>

    @Query("SELECT * FROM todo_table WHERE time = :time")
    fun getTodosByDate(time: Date): List<TodoModel>
}
