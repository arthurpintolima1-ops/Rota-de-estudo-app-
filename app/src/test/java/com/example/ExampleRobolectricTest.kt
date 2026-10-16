package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.DefaultSubjectsData
import com.example.data.db.EntityMappers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("RotaEstudo", appName)
  }

  @Test
  fun `test entity mapper conversion with JSON`() {
    val subjects = DefaultSubjectsData.getDefaultSubjects()
    val first = subjects.first()

    val entity = EntityMappers.toEntity(first)
    val convertedBack = EntityMappers.toDomain(entity)

    assertEquals(first.id, convertedBack.id)
    assertEquals(first.name, convertedBack.name)
    assertEquals(first.educationLevel, convertedBack.educationLevel)
    assertEquals(first.topics.size, convertedBack.topics.size)
  }
}
