package com.example.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.db.entities.ReminderConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderConfigDao {
  @Query("SELECT * FROM reminder_configs WHERE id = 1 LIMIT 1")
  fun getConfigFlow(): Flow<ReminderConfigEntity?>

  @Query("SELECT * FROM reminder_configs WHERE id = 1 LIMIT 1")
  suspend fun getConfig(): ReminderConfigEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertOrUpdate(config: ReminderConfigEntity)
}
