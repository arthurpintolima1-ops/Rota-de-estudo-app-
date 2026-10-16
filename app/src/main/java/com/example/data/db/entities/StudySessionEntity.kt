package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
  @PrimaryKey(autoGenerate = true) val id: Long = 0,
  val subjectId: String,
  val subjectName: String,
  val educationLevel: String,
  val durationMinutes: Int,
  val timestampEpochMillis: Long,
  val completedAtFormatted: String,
  val notes: String = ""
)
