package com.example.data.db

import com.example.data.db.entities.ReminderConfigEntity
import com.example.data.db.entities.StudySessionEntity
import com.example.data.db.entities.StudySubjectEntity
import com.example.model.EducationLevel
import com.example.model.QuizQuestion
import com.example.model.RotationStrategy
import com.example.model.StudyReminderConfig
import com.example.model.StudySessionRecord
import com.example.model.StudySubject
import org.json.JSONArray
import org.json.JSONObject

object EntityMappers {

  fun toDomain(entity: StudySubjectEntity): StudySubject {
    val topicsList = mutableListOf<String>()
    try {
      val arr = JSONArray(entity.topicsJson)
      for (i in 0 until arr.length()) {
        topicsList.add(arr.getString(i))
      }
    } catch (_: Exception) {}

    val questionsList = mutableListOf<QuizQuestion>()
    try {
      val qArr = JSONArray(entity.questionsJson)
      for (i in 0 until qArr.length()) {
        val obj = qArr.getJSONObject(i)
        val optArr = obj.getJSONArray("options")
        val options = mutableListOf<String>()
        for (j in 0 until optArr.length()) {
          options.add(optArr.getString(j))
        }
        questionsList.add(
          QuizQuestion(
            id = obj.optString("id", i.toString()),
            question = obj.getString("question"),
            options = options,
            correctOptionIndex = obj.getInt("correctOptionIndex"),
            explanation = obj.optString("explanation", "")
          )
        )
      }
    } catch (_: Exception) {}

    return StudySubject(
      id = entity.id,
      name = entity.name,
      educationLevel = try {
        EducationLevel.valueOf(entity.educationLevel)
      } catch (_: Exception) {
        EducationLevel.FUNDAMENTAL
      },
      category = entity.category,
      colorHex = entity.colorHex,
      iconKey = entity.iconKey,
      shortTip = entity.shortTip,
      topics = topicsList,
      questions = questionsList,
      isEnabled = entity.isEnabled,
      priorityWeight = entity.priorityWeight,
      sessionsCompleted = entity.sessionsCompleted,
      minutesStudied = entity.minutesStudied
    )
  }

  fun toEntity(domain: StudySubject): StudySubjectEntity {
    val topicsArr = JSONArray()
    domain.topics.forEach { topicsArr.put(it) }

    val qArr = JSONArray()
    domain.questions.forEach { q ->
      val obj = JSONObject()
      obj.put("id", q.id)
      obj.put("question", q.question)
      val optArr = JSONArray()
      q.options.forEach { optArr.put(it) }
      obj.put("options", optArr)
      obj.put("correctOptionIndex", q.correctOptionIndex)
      obj.put("explanation", q.explanation)
      qArr.put(obj)
    }

    return StudySubjectEntity(
      id = domain.id,
      name = domain.name,
      educationLevel = domain.educationLevel.name,
      category = domain.category,
      colorHex = domain.colorHex,
      iconKey = domain.iconKey,
      shortTip = domain.shortTip,
      topicsJson = topicsArr.toString(),
      questionsJson = qArr.toString(),
      isEnabled = domain.isEnabled,
      priorityWeight = domain.priorityWeight,
      sessionsCompleted = domain.sessionsCompleted,
      minutesStudied = domain.minutesStudied
    )
  }

  fun toDomain(entity: StudySessionEntity): StudySessionRecord {
    return StudySessionRecord(
      id = entity.id,
      subjectId = entity.subjectId,
      subjectName = entity.subjectName,
      educationLevel = try {
        EducationLevel.valueOf(entity.educationLevel)
      } catch (_: Exception) {
        EducationLevel.FUNDAMENTAL
      },
      durationMinutes = entity.durationMinutes,
      timestampEpochMillis = entity.timestampEpochMillis,
      completedAtFormatted = entity.completedAtFormatted,
      notes = entity.notes
    )
  }

  fun toEntity(domain: StudySessionRecord): StudySessionEntity {
    return StudySessionEntity(
      id = domain.id,
      subjectId = domain.subjectId,
      subjectName = domain.subjectName,
      educationLevel = domain.educationLevel.name,
      durationMinutes = domain.durationMinutes,
      timestampEpochMillis = domain.timestampEpochMillis,
      completedAtFormatted = domain.completedAtFormatted,
      notes = domain.notes
    )
  }

  fun toDomain(entity: ReminderConfigEntity): StudyReminderConfig {
    val days = entity.daysOfWeekCsv.split(",")
      .mapNotNull { it.trim().toIntOrNull() }
      .toSet()

    val strategy = try {
      RotationStrategy.valueOf(entity.rotationStrategy)
    } catch (_: Exception) {
      RotationStrategy.ALTERNATING_LEVELS
    }

    return StudyReminderConfig(
      id = entity.id,
      isReminderEnabled = entity.isReminderEnabled,
      reminderHour = entity.reminderHour,
      reminderMinute = entity.reminderMinute,
      daysOfWeek = if (days.isEmpty()) setOf(1, 2, 3, 4, 5) else days,
      focusDurationMinutes = entity.focusDurationMinutes,
      shortBreakMinutes = entity.shortBreakMinutes,
      rotationStrategy = strategy,
      soundEnabled = entity.soundEnabled,
      vibrateEnabled = entity.vibrateEnabled
    )
  }

  fun toEntity(domain: StudyReminderConfig): ReminderConfigEntity {
    return ReminderConfigEntity(
      id = domain.id,
      isReminderEnabled = domain.isReminderEnabled,
      reminderHour = domain.reminderHour,
      reminderMinute = domain.reminderMinute,
      daysOfWeekCsv = domain.daysOfWeek.joinToString(","),
      focusDurationMinutes = domain.focusDurationMinutes,
      shortBreakMinutes = domain.shortBreakMinutes,
      rotationStrategy = domain.rotationStrategy.name,
      soundEnabled = domain.soundEnabled,
      vibrateEnabled = domain.vibrateEnabled
    )
  }
}
