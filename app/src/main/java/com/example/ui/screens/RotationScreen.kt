package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EducationLevel
import com.example.model.StudySubject
import com.example.ui.components.SubjectIconHelper
import com.example.ui.theme.FundamentalGreen
import com.example.ui.theme.MedioAmber
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun RotationScreen(
  viewModel: StudyViewModel,
  onStartSubjectAndGoFocus: () -> Unit
) {
  val subjects by viewModel.allSubjects.collectAsState()
  var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Todas, 1: Fundamental, 2: Médio

  val filteredSubjects = remember(subjects, selectedTabIndex) {
    when (selectedTabIndex) {
      1 -> subjects.filter { it.educationLevel == EducationLevel.FUNDAMENTAL }
      2 -> subjects.filter { it.educationLevel == EducationLevel.MEDIO }
      else -> subjects
    }
  }

  val fundamentalCount = subjects.count { it.educationLevel == EducationLevel.FUNDAMENTAL && it.isEnabled }
  val medioCount = subjects.count { it.educationLevel == EducationLevel.MEDIO && it.isEnabled }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { viewModel.setAddSubjectDialogVisible(true) },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White,
        modifier = Modifier.testTag("fab_add_subject")
      ) {
        Icon(Icons.Default.Add, contentDescription = "Adicionar Matéria")
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      item {
        Spacer(modifier = Modifier.height(4.dp))
        // Explanation Card about Interleaved Rotation
        Card(
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
          ),
          border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              imageVector = Icons.Default.SwapVert,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Como Funciona a Rota Intercalada:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "O cérebro aprende melhor variando o tipo de estímulo. A cada ciclo o app alterna entre matérias de base (Fundamental) e aprofundadas (Médio), mantendo você com foco e sem esgotamento.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                lineHeight = 18.sp
              )
              Spacer(modifier = Modifier.height(8.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = FundamentalGreen.copy(alpha = 0.2f)
                ) {
                  Text(
                    text = "🌱 $fundamentalCount Fundamental",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = FundamentalGreen,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = MedioAmber.copy(alpha = 0.2f)
                ) {
                  Text(
                    text = "⚡ $medioCount Médio",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MedioAmber,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                  )
                }
              }
            }
          }
        }
      }

      item {
        // Tab row filter
        TabRow(
          selectedTabIndex = selectedTabIndex,
          containerColor = Color.Transparent,
          contentColor = MaterialTheme.colorScheme.primary
        ) {
          Tab(
            selected = selectedTabIndex == 0,
            onClick = { selectedTabIndex = 0 },
            text = { Text("Todas (${subjects.size})") }
          )
          Tab(
            selected = selectedTabIndex == 1,
            onClick = { selectedTabIndex = 1 },
            text = { Text("Fundamental") }
          )
          Tab(
            selected = selectedTabIndex == 2,
            onClick = { selectedTabIndex = 2 },
            text = { Text("Médio") }
          )
        }
      }

      items(filteredSubjects, key = { it.id }) { subject ->
        SubjectCardItem(
          subject = subject,
          onToggle = { enabled -> viewModel.toggleSubjectEnabled(subject.id, enabled) },
          onOpenDetails = { viewModel.setSubjectForDetail(subject) },
          onStartFocus = {
            viewModel.selectSubjectDirectly(subject)
            onStartSubjectAndGoFocus()
          }
        )
      }

      item {
        Spacer(modifier = Modifier.height(72.dp)) // padding for FAB and bottom bar
      }
    }
  }
}

@Composable
fun SubjectCardItem(
  subject: StudySubject,
  onToggle: (Boolean) -> Unit,
  onOpenDetails: () -> Unit,
  onStartFocus: () -> Unit
) {
  val levelColor = if (subject.educationLevel == EducationLevel.FUNDAMENTAL) FundamentalGreen else MedioAmber

  ElevatedCard(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .clickable { onOpenDetails() }
      .testTag("subject_card_${subject.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(44.dp)
          .clip(CircleShape)
          .background(levelColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = SubjectIconHelper.getIcon(subject.iconKey),
          contentDescription = null,
          tint = levelColor,
          modifier = Modifier.size(24.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = levelColor.copy(alpha = 0.15f)
          ) {
            Text(
              text = subject.educationLevel.shortBadge,
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              color = levelColor,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Spacer(modifier = Modifier.width(6.dp))

          Text(
            text = subject.category,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = subject.name,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold
        )

        Text(
          text = "${subject.topics.size} tópicos • ${subject.minutesStudied} min estudados",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      // Action: Study now icon button
      IconButton(
        onClick = onStartFocus,
        modifier = Modifier.testTag("btn_quick_study_${subject.id}")
      ) {
        Icon(
          imageVector = Icons.Default.PlayArrow,
          contentDescription = "Estudar esta matéria agora",
          tint = MaterialTheme.colorScheme.primary
        )
      }

      // Toggle switch to activate/deactivate in alternating rotation
      Switch(
        checked = subject.isEnabled,
        onCheckedChange = onToggle,
        modifier = Modifier.testTag("switch_subject_${subject.id}"),
        colors = SwitchDefaults.colors(
          checkedThumbColor = levelColor,
          checkedTrackColor = levelColor.copy(alpha = 0.3f)
        )
      )
    }
  }
}
