package com.example.data.repository

import android.content.Context
import com.example.data.db.AppDatabase
import com.example.data.db.DefaultSubjectsData
import com.example.data.db.EntityMappers
import com.example.data.db.entities.ReminderConfigEntity
import com.example.model.EducationLevel
import com.example.model.StudyReminderConfig
import com.example.model.StudySessionRecord
import com.example.model.StudySubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StudyRepository(context: Context) {

  private val database = AppDatabase.getInstance(context)
  private val subjectDao = database.studySubjectDao()
  private val sessionDao = database.studySessionDao()
  private val reminderDao = database.reminderConfigDao()

  suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
    if (subjectDao.getCount() == 0) {
      val defaultEntities = DefaultSubjectsData.getDefaultSubjects().map {
        EntityMappers.toEntity(it)
      }
      subjectDao.insertAll(defaultEntities)
    }
    if (reminderDao.getConfig() == null) {
      reminderDao.insertOrUpdate(ReminderConfigEntity())
    }
  }

  val subjectsFlow: Flow<List<StudySubject>> = subjectDao.getAllSubjectsFlow().map { list ->
    list.map { EntityMappers.toDomain(it) }
  }

  val sessionsFlow: Flow<List<StudySessionRecord>> = sessionDao.getAllSessionsFlow().map { list ->
    list.map { EntityMappers.toDomain(it) }
  }

  val reminderConfigFlow: Flow<StudyReminderConfig> = reminderDao.getConfigFlow().map { entity ->
    if (entity != null) EntityMappers.toDomain(entity) else StudyReminderConfig()
  }

  suspend fun toggleSubject(id: String, isEnabled: Boolean) = withContext(Dispatchers.IO) {
    subjectDao.toggleEnabled(id, isEnabled)
  }

  suspend fun saveSubject(subject: StudySubject) = withContext(Dispatchers.IO) {
    subjectDao.insert(EntityMappers.toEntity(subject))
  }

  suspend fun deleteSubject(id: String) = withContext(Dispatchers.IO) {
    subjectDao.delete(id)
  }

  suspend fun recordCompletedSession(
    subject: StudySubject,
    durationMinutes: Int,
    notes: String = ""
  ): Long = withContext(Dispatchers.IO) {
    val now = System.currentTimeMillis()
    val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
    val record = StudySessionRecord(
      subjectId = subject.id,
      subjectName = subject.name,
      educationLevel = subject.educationLevel,
      durationMinutes = durationMinutes,
      timestampEpochMillis = now,
      completedAtFormatted = formatter.format(Date(now)),
      notes = notes
    )
    val insertedId = sessionDao.insert(EntityMappers.toEntity(record))
    subjectDao.incrementStats(subject.id, durationMinutes)
    insertedId
  }

  suspend fun saveReminderConfig(config: StudyReminderConfig) = withContext(Dispatchers.IO) {
    reminderDao.insertOrUpdate(EntityMappers.toEntity(config))
  }

  suspend fun clearHistory() = withContext(Dispatchers.IO) {
    sessionDao.clearAll()
  }
}
