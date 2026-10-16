package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.db.entities.StudySubjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySubjectDao {
  @Query("SELECT * FROM study_subjects ORDER BY educationLevel ASC, priorityWeight DESC, name ASC")
  fun getAllSubjectsFlow(): Flow<List<StudySubjectEntity>>

  @Query("SELECT * FROM study_subjects WHERE id = :id LIMIT 1")
  suspend fun getSubjectById(id: String): StudySubjectEntity?

  @Query("SELECT COUNT(*) FROM study_subjects")
  suspend fun getCount(): Int

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertAll(subjects: List<StudySubjectEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(subject: StudySubjectEntity)

  @Update
  suspend fun update(subject: StudySubjectEntity)

  @Query("UPDATE study_subjects SET isEnabled = :isEnabled WHERE id = :id")
  suspend fun toggleEnabled(id: String, isEnabled: Boolean)

  @Query("UPDATE study_subjects SET sessionsCompleted = sessionsCompleted + 1, minutesStudied = minutesStudied + :minutes WHERE id = :id")
  suspend fun incrementStats(id: String, minutes: Int)

  @Query("DELETE FROM study_subjects WHERE id = :id")
  suspend fun delete(id: String)
}
