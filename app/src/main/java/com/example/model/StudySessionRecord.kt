package com.example.model

data class StudySessionRecord(
  val id: Long = 0,
  val subjectId: String,
  val subjectName: String,
  val educationLevel: EducationLevel,
  val durationMinutes: Int,
  val timestampEpochMillis: Long,
  val completedAtFormatted: String,
  val notes: String = ""
)
