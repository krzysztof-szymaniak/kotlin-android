package com.example.todolist3

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class RecyclerListAdapter(private var dataSet: MutableList<Task>, private val taskDao: TaskDao) :
        RecyclerView.Adapter<RecyclerListAdapter.ViewHolder>()  {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskImage : ImageView = view.findViewById(R.id.task_image)
        val titleTextView : TextView = view.findViewById(R.id.title_textview)
        val descriptionTextView : TextView = view.findViewById(R.id.desription_textview)
        val dateTimeTextView : TextView = view.findViewById(R.id.datetime_textview)
        val importanceTextView : TextView = view.findViewById(R.id.importance_textview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = dataSet[position]
        holder.titleTextView.text = task.title
        holder.descriptionTextView.text = task.description
        holder.dateTimeTextView.text = String.format("%02d.%02d.%d\nat %02d:%02d",
            task.date.get(Calendar.DAY_OF_MONTH),
            task.date.get(Calendar.MONTH) + 1,  // months start from 0 for some reason
            task.date.get(Calendar.YEAR),
            task.date.get(Calendar.HOUR_OF_DAY),
            task.date.get(Calendar.MINUTE)
        )

        val prio = when (task.importance) {
            Constants.LOW -> "important"
            Constants.MID -> "really important"
            Constants.HIGH -> "very important"
            else -> null
        }
        holder.importanceTextView.text = prio

        val backgroundColor = when (task.importance) {
            Constants.LOW -> Constants.importantColor
            Constants.MID -> Constants.reallyImportantColor
            Constants.HIGH -> Constants.veryImportantColor
            else -> null
        }
        holder.itemView.setBackgroundColor(Color.parseColor(backgroundColor));

        val resource = when (task.type) {
            Constants.SHOPPING -> R.drawable.shopping_icon
            Constants.SOCIAL -> R.drawable.social_icon
            Constants.CHORES -> R.drawable.house_icon
            else -> null
        }
        holder.taskImage.setImageResource(resource!!)
        holder.itemView.setOnLongClickListener{
            taskDao.deleteByID(task.taskID!!)
            notifyNewData(taskDao.getAll())
            true
        }
    }

    fun notifyNewData(newData: List<Task>) {
        dataSet.clear()
        dataSet.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataSet.size
}