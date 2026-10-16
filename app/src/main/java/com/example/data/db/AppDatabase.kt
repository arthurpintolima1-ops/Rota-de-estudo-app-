package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.db.dao.ReminderConfigDao
import com.example.data.db.dao.StudySessionDao
import com.example.data.db.dao.StudySubjectDao
import com.example.data.db.entities.ReminderConfigEntity
import com.example.data.db.entities.StudySessionEntity
import com.example.data.db.entities.StudySubjectEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    StudySubjectEntity::class,
    StudySessionEntity::class,
    ReminderConfigEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun studySubjectDao(): StudySubjectDao
  abstract fun studySessionDao(): StudySessionDao
  abstract fun reminderConfigDao(): ReminderConfigDao

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          AppDatabase::class.java,
          "rota_estudo.db"
        )
          .addCallback(object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
              CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                val defaultEntities = DefaultSubjectsData.getDefaultSubjects().map {
                  EntityMappers.toEntity(it)
                }
                database.studySubjectDao().insertAll(defaultEntities)
                database.reminderConfigDao().insertOrUpdate(ReminderConfigEntity())
              }
            }
          })
          .fallbackToDestructiveMigration()
          .build()
        INSTANCE = instance
        instance
      }
    }
  }
}
