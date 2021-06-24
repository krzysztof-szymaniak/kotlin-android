package com.example.todolist3

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "channel id"
const val NOTIFICATION_ID = 1

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        val notifyIntent = Intent(context, MainActivity::class.java) // to be able to launch app from notification
        val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0)

        val title = intent?.getStringExtra("taskTitle")
        val description = intent?.getStringExtra("taskDescription")
        val type = intent?.getStringExtra("taskType")
        val importance = intent?.getIntExtra("taskPriority", 0)
        val resource = when (type) {
            Constants.SHOPPING -> R.drawable.shopping_icon
            Constants.SOCIAL -> R.drawable.social_icon
            Constants.CHORES -> R.drawable.house_icon
            else -> null
        }
        val backgroundColor = when (importance) {
            Constants.LOW -> Constants.importantColor
            Constants.MID -> Constants.reallyImportantColor
            Constants.HIGH -> Constants.veryImportantColor
            else -> null
        }
        val builder = Notification.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(resource!!)
            .setContentIntent(pendingIntent)
            .setColor(Color.parseColor(backgroundColor))

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}