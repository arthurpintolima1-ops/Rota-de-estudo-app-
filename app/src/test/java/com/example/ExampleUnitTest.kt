package com.example

import com.example.data.db.DefaultSubjectsData
import com.example.model.EducationLevel
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testDefaultSubjectsIncludeBothFundamentalAndMedio() {
    val subjects = DefaultSubjectsData.getDefaultSubjects()
    assertTrue(subjects.isNotEmpty())

    val fundamental = subjects.filter { it.educationLevel == EducationLevel.FUNDAMENTAL }
    val medio = subjects.filter { it.educationLevel == EducationLevel.MEDIO }

    assertTrue("Deve conter matérias do Ensino Fundamental", fundamental.isNotEmpty())
    assertTrue("Deve conter matérias do Ensino Médio", medio.isNotEmpty())

    // Check key fundamental subjects
    assertTrue(fundamental.any { it.name.contains("Matemática") })
    assertTrue(fundamental.any { it.name.contains("Portuguesa") })
    assertTrue(fundamental.any { it.name.contains("Ciências") })

    // Check key medio subjects
    assertTrue(medio.any { it.name.contains("Física") })
    assertTrue(medio.any { it.name.contains("Química") })
    assertTrue(medio.any { it.name.contains("Biologia") })
    assertTrue(medio.any { it.name.contains("Redação") })
  }
}
