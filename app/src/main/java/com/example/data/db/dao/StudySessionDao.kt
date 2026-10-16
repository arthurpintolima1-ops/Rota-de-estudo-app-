package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.db.entities.StudySessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudySessionDao {
  @Query("SELECT * FROM study_sessions ORDER BY timestampEpochMillis DESC")
  fun getAllSessionsFlow(): Flow<List<StudySessionEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(session: StudySessionEntity): Long

  @Query("DELETE FROM study_sessions WHERE id = :id")
  suspend fun delete(id: Long)

  @Query("DELETE FROM study_sessions")
  suspend fun clearAll()
}
