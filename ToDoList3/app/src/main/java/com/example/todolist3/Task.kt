package com.example.todolist3

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.io.Serializable
import java.util.*

@Entity
data class Task(
    @ColumnInfo(name = "taskID")
    @PrimaryKey(autoGenerate = true) val taskID: Int = 0,
    var title: String,
    var description: String,
    @TypeConverters(DatabaseConverters::class)
    var date: Calendar,
    var importance: Int,
    var type: String
) : Serializable {
}


