package com.example.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.model.StudyReminderConfig
import com.example.receiver.StudyReminderReceiver
import java.util.Calendar

object AlarmScheduler {

  private const val ALARM_REQUEST_CODE = 2002

  fun scheduleDailyReminder(context: Context, config: StudyReminderConfig) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

    val intent = Intent(context, StudyReminderReceiver::class.java).apply {
      action = "com.aistudio.rotaestudo.ACTION_STUDY_REMINDER"
    }

    val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
    val pendingIntent = PendingIntent.getBroadcast(
      context,
      ALARM_REQUEST_CODE,
      intent,
      flags
    )

    if (!config.isReminderEnabled) {
      alarmManager.cancel(pendingIntent)
      return
    }

    val calendar = Calendar.getInstance().apply {
      set(Calendar.HOUR_OF_DAY, config.reminderHour)
      set(Calendar.MINUTE, config.reminderMinute)
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
      if (before(Calendar.getInstance())) {
        add(Calendar.DAY_OF_YEAR, 1)
      }
    }

    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        alarmManager.setExactAndAllowWhileIdle(
          AlarmManager.RTC_WAKEUP,
          calendar.timeInMillis,
          pendingIntent
        )
      } else {
        alarmManager.setExact(
          AlarmManager.RTC_WAKEUP,
          calendar.timeInMillis,
          pendingIntent
        )
      }
    } catch (_: SecurityException) {
      // Fallback if exact alarm permission is not granted
      alarmManager.set(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
      )
    }
  }

  fun cancelReminder(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
    val intent = Intent(context, StudyReminderReceiver::class.java)
    val flags = PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
    val pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, intent, flags)
    alarmManager.cancel(pendingIntent)
  }
}
