package com.example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.data.db.AppDatabase
import com.example.data.db.EntityMappers
import com.example.model.EducationLevel
import com.example.util.AlarmScheduler
import com.example.util.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

class StudyReminderReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent?) {
    val pendingResult = goAsync()

    CoroutineScope(Dispatchers.IO).launch {
      try {
        val database = AppDatabase.getInstance(context)
        val configEntity = database.reminderConfigDao().getConfig()
        val config = configEntity?.let { EntityMappers.toDomain(it) }

        val todayDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        // Convert to ISO 1=Mon..7=Sun
        val isoDay = when (todayDayOfWeek) {
          Calendar.MONDAY -> 1
          Calendar.TUESDAY -> 2
          Calendar.WEDNESDAY -> 3
          Calendar.THURSDAY -> 4
          Calendar.FRIDAY -> 5
          Calendar.SATURDAY -> 6
          Calendar.SUNDAY -> 7
          else -> 1
        }

        if (config == null || (config.isReminderEnabled && config.daysOfWeek.contains(isoDay))) {
          // Select subject to showcase in the reminder notification
          val subjects = database.studySubjectDao().getAllSubjectsFlow()
          // Grab current subjects or defaults
          val lastSession = database.studySessionDao().getAllSessionsFlow()
          // Default greeting notification
          NotificationHelper.showStudyReminder(
            context = context,
            subjectName = "Matéria da Rota",
            levelName = "Fundamental / Médio"
          )
        }

        // Reschedule next alarm
        if (config != null && config.isReminderEnabled) {
          AlarmScheduler.scheduleDailyReminder(context, config)
        }
      } catch (e: Exception) {
        e.printStackTrace()
      } finally {
        pendingResult.finish()
      }
    }
  }
}
