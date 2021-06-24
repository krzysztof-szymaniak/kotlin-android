package com.example.todolist3

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM task")
    fun getAll(): List<Task>

    @Query("SELECT * FROM task WHERE title LIKE :name")
    fun findByName(name: String): Task

    @Insert
    fun insert(task: Task)

    @Query("SELECT COUNT(taskID) FROM task")
    fun getID(): Int

    @Query("DELETE FROM task WHERE taskID LIKE :id")
    fun deleteByID(id: Int)

    @Query("SELECT * FROM task ORDER BY date")
    fun getSortedByDate() : List<Task>

    @Query("SELECT * FROM task ORDER BY type")
    fun getSortedByType() : List<Task>

    @Query("SELECT * FROM task ORDER BY importance DESC")
    fun getSortedByPriority() : List<Task>
}
