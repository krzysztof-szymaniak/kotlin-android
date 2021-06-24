package com.example.todolist3

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todolist3.databinding.ActivityAddTaskBinding
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTaskBinding
    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedType: String? = null
    private var selectedPrio: Int? = null
    private var isDateSelected = false


    private fun sendTaskBack(task: Task) {
        val intent = Intent().apply {
            putExtra("task", task)
        }
        setResult(Activity.RESULT_OK, intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()  // hide title bar
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // listeners
        binding.dateButton.setOnClickListener { pickDateTime() }
        binding.addButton.setOnClickListener { addTask() }
        binding.highPrio.setOnClickListener {
            selectedPrio = Constants.HIGH
            binding.colorImage.setBackgroundColor(Color.parseColor(Constants.veryImportantColor))
        }
        binding.midPrio.setOnClickListener {
            selectedPrio = Constants.MID
            binding.colorImage.setBackgroundColor(Color.parseColor(Constants.reallyImportantColor))
        }
        binding.lowPrio.setOnClickListener {
            selectedPrio = Constants.LOW
            binding.colorImage.setBackgroundColor(Color.parseColor(Constants.importantColor))
        }
        binding.chores.setOnClickListener {
            selectedType = Constants.CHORES
            binding.taskImage.setImageResource(R.drawable.house_icon)
        }
        binding.shopping.setOnClickListener {
            selectedType = Constants.SHOPPING
            binding.taskImage.setImageResource(R.drawable.shopping_icon)
        }
        binding.social.setOnClickListener {
            selectedType = Constants.SOCIAL
            binding.taskImage.setImageResource(R.drawable.social_icon)
        }
    }

    private fun addTask() {
        if (binding.titleInput.text.trim().isEmpty()
            || binding.descriptionInput.text.trim().isEmpty()
            || selectedPrio == null
            || selectedType == null
            || !isDateSelected
        ) {
            Toast.makeText(this, "Choose every parameter", Toast.LENGTH_LONG).show()
        } else {
            val newTask = Task(
                date = selectedDate,
                title = binding.titleInput.text.trim().toString(),
                description = binding.descriptionInput.text.trim().toString(),
                type = selectedType!!,
                importance = selectedPrio!!
            )

            sendTaskBack(newTask)  // send task back to main activity so it can be added
            Toast.makeText(this, "Task created successfully", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun updateText() {
        binding.datetimePlaceholder.text = String.format(
            "%02d.%02d.%d\nat %02d:%02d",
            selectedDate.get(Calendar.DAY_OF_MONTH),
            selectedDate.get(Calendar.MONTH) + 1,  // months start from 0 for some reason
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.HOUR_OF_DAY),
            selectedDate.get(Calendar.MINUTE)
        )

    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
        val startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val startMinute = currentDateTime.get(Calendar.MINUTE)

        DatePickerDialog(this@AddTaskActivity, { _, year, month, day ->
            TimePickerDialog(this@AddTaskActivity, { _, hour, minute ->
                selectedDate.set(year, month, day, hour, minute)
                isDateSelected = true
                updateText()
            }, startHour, startMinute, true).show()
        }, startYear, startMonth, startDay).show()
    }
}