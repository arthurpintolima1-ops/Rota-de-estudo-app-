package com.example.model

data class StudySubject(
  val id: String,
  val name: String,
  val educationLevel: EducationLevel,
  val category: String,
  val colorHex: String,
  val iconKey: String,
  val shortTip: String,
  val topics: List<String>,
  val questions: List<QuizQuestion> = emptyList(),
  val isEnabled: Boolean = true,
  val priorityWeight: Int = 1,
  val sessionsCompleted: Int = 0,
  val minutesStudied: Int = 0
)
