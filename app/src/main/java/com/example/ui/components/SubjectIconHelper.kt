package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.ui.graphics.vector.ImageVector

object SubjectIconHelper {
  fun getIcon(iconKey: String): ImageVector {
    return when (iconKey) {
      "calculate" -> Icons.Default.Calculate
      "menu_book" -> Icons.Default.MenuBook
      "science" -> Icons.Default.Science
      "history_edu" -> Icons.Default.HistoryEdu
      "public" -> Icons.Default.Public
      "bolt" -> Icons.Default.Bolt
      "biotech" -> Icons.Default.Biotech
      "psychology" -> Icons.Default.Psychology
      "functions" -> Icons.Default.Functions
      "edit_note" -> Icons.Default.EditNote
      "auto_stories" -> Icons.Default.AutoStories
      else -> Icons.Default.School
    }
  }
}
