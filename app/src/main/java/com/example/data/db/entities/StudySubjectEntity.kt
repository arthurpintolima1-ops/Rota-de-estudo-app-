package com.example.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_subjects")
data class StudySubjectEntity(
  @PrimaryKey val id: String,
  val name: String,
  val educationLevel: String,
  val category: String,
  val colorHex: String,
  val iconKey: String,
  val shortTip: String,
  val topicsJson: String,
  val questionsJson: String,
  val isEnabled: Boolean = true,
  val priorityWeight: Int = 1,
  val sessionsCompleted: Int = 0,
  val minutesStudied: Int = 0
)
