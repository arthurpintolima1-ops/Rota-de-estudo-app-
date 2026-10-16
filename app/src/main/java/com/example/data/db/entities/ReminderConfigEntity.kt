package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder_configs")
data class ReminderConfigEntity(
  @PrimaryKey val id: Int = 1,
  val isReminderEnabled: Boolean = true,
  val reminderHour: Int = 16,
  val reminderMinute: Int = 0,
  val daysOfWeekCsv: String = "1,2,3,4,5",
  val focusDurationMinutes: Int = 25,
  val shortBreakMinutes: Int = 5,
  val rotationStrategy: String = "ALTERNATING_LEVELS",
  val soundEnabled: Boolean = true,
  val vibrateEnabled: Boolean = true
)
