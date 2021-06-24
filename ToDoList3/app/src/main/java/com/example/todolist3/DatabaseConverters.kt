package com.example.todolist3

import androidx.room.TypeConverter
import java.util.*

class DatabaseConverters {
    @TypeConverter
    fun toCalendar(value: Long): Calendar = Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter
    fun fromCalendar(date: Calendar) = date.timeInMillis
}
