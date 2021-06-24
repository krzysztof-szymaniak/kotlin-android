package com.example.todolist3

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.todolist3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val taskList = ArrayList<Task>()

    private lateinit var mRecycler : RecyclerView
    private lateinit var database: AppDatabase
    private lateinit var taskDao: TaskDao
    private lateinit var listAdapter: RecyclerListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database"
        ).allowMainThreadQueries()  // terrible practice but in terms of small app its sufficient
            .build()
        taskDao = database.taskDao()
        mRecycler = binding.recyclerView3
        listAdapter = RecyclerListAdapter(taskList, taskDao)
        mRecycler.adapter = listAdapter
        mRecycler.layoutManager = LinearLayoutManager(this)

        listAdapter.notifyNewData(taskDao.getAll())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_layout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_time -> {
                listAdapter.notifyNewData(taskDao.getSortedByDate())
            }
            R.id.sort_by_priority -> {
                listAdapter.notifyNewData(taskDao.getSortedByPriority())
            }
            R.id.sort_by_type -> {
                listAdapter.notifyNewData(taskDao.getSortedByType())
            }
        }
        return true
    }

    fun startNewTask(view: View) {
        val intent = Intent(this, AddTaskActivity::class.java)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                val task = data!!.getSerializableExtra("task") as? Task
                taskDao.insert(task!!)
                listAdapter.notifyNewData(taskDao.getAll())
                val id = taskDao.getID()
                setAlarmfor(task, id)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
    private fun setAlarmfor(task: Task, id: Int) {
        val notifyIntent = Intent(this, Receiver::class.java).apply {
            putExtra("taskTitle", task.title)
            putExtra("taskDescription", task.description)
            putExtra("taskPriority", task.importance)
            putExtra("taskType", task.type)
        }
        val pendingIntent = PendingIntent.getBroadcast(this, id, notifyIntent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, task.date.timeInMillis, pendingIntent)
    }
}