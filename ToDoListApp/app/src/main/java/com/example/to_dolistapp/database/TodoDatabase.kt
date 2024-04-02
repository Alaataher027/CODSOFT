package com.example.to_dolistapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.to_dolistapp.database.dao.TodoDao
import com.example.to_dolistapp.database.model.TodoModel

@Database(entities = [TodoModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun getTodosDao(): TodoDao
    companion object {
        private val DATABASE_NAME = "Todos-Database"
        private var todoDatabaseInstance: TodoDatabase? = null
        fun getInstance(context: Context): TodoDatabase {
            if (todoDatabaseInstance == null) {
                todoDatabaseInstance = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return todoDatabaseInstance!!
        }

    }
}