package com.example.to_dolistapp.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

@Entity(tableName = "todo_table")
class TodoModel(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    val id: Int? = null,
    @ColumnInfo
    var title: String? = null,
    @ColumnInfo
    var description: String? = null,
    @ColumnInfo
    var isDone: Boolean? = false,
    @ColumnInfo
    var time: Date? = null,
) : Serializable

