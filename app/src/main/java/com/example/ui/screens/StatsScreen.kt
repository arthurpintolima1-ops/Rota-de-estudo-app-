package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.model.StudySessionRecord
import com.example.ui.theme.FundamentalGreen
import com.example.ui.theme.MedioAmber
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun StatsScreen(viewModel: StudyViewModel) {
  val sessions by viewModel.completedSessions.collectAsState()
  var showClearConfirm by remember { mutableStateOf(false) }

  val totalMinutes = sessions.sumOf { it.durationMinutes }
  val totalHours = totalMinutes / 60
  val remainingMinutes = totalMinutes % 60

  val fundamentalMinutes = sessions.filter { it.educationLevel == EducationLevel.FUNDAMENTAL }.sumOf { it.durationMinutes }
  val medioMinutes = sessions.filter { it.educationLevel == EducationLevel.MEDIO }.sumOf { it.durationMinutes }

  val totalCount = sessions.size

  // Calculate active study days streak
  val uniqueDays = sessions.map {
    it.completedAtFormatted.split(" ").firstOrNull() ?: ""
  }.filter { it.isNotEmpty() }.toSet().size

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(6.dp))
      // Streak and Total Hours Header Cards
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Streak Card
        ElevatedCard(
          modifier = Modifier
            .weight(1f)
            .testTag("stats_streak_card"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(MedioAmber.copy(alpha = 0.2f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = MedioAmber)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = "$uniqueDays dias",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Dias de Estudo",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        // Total Focus Time Card
        ElevatedCard(
          modifier = Modifier
            .weight(1f)
            .testTag("stats_total_time_card"),
          shape = RoundedCornerShape(18.dp),
          colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Box(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Timer, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (totalHours > 0) "${totalHours}h ${remainingMinutes}m" else "${totalMinutes}m",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "$totalCount sessões feitas",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    item {
      // Balance comparison: Fundamental vs Médio
      Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.PieChart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Equilíbrio da Rota de Estudos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          val totalMinsFloat = (fundamentalMinutes + medioMinutes).toFloat().coerceAtLeast(1f)
          val fundRatio = (fundamentalMinutes / totalMinsFloat).coerceIn(0f, 1f)
          val medRatio = (medioMinutes / totalMinsFloat).coerceIn(0f, 1f)

          // Stacked bar visual comparison
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(16.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant)
          ) {
            if (fundamentalMinutes > 0) {
              Box(
                modifier = Modifier
                  .weight(fundRatio)
                  .height(16.dp)
                  .background(FundamentalGreen)
              )
            }
            if (medioMinutes > 0) {
              Box(
                modifier = Modifier
                  .weight(medRatio)
                  .height(16.dp)
                  .background(MedioAmber)
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(FundamentalGreen)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Fundamental: $fundamentalMinutes min (${(fundRatio * 100).toInt()}%)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(10.dp)
                  .clip(CircleShape)
                  .background(MedioAmber)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Médio: $medioMinutes min (${(medRatio * 100).toInt()}%)",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Histórico de Ciclos de Foco",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }

        if (sessions.isNotEmpty()) {
          IconButton(onClick = { showClearConfirm = true }) {
            Icon(Icons.Default.DeleteOutline, contentDescription = "Limpar Histórico")
          }
        }
      }
    }

    if (sessions.isEmpty()) {
      item {
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
              modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
              text = "Nenhuma sessão concluída ainda",
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Inicie seu primeiro ciclo de foco na aba 'Foco' para registrar seus estudos!",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(sessions, key = { it.id }) { session ->
        SessionLogItem(session)
      }
    }

    item {
      Spacer(modifier = Modifier.height(32.dp))
    }
  }

  if (showClearConfirm) {
    AlertDialog(
      onDismissRequest = { showClearConfirm = false },
      title = { Text("Limpar Histórico de Estudos?") },
      text = { Text("Essa ação removerá todas as sessões registradas no histórico local.") },
      confirmButton = {
        TextButton(
          onClick = {
            viewModel.clearHistory()
            showClearConfirm = false
          }
        ) {
          Text("Limpar", color = MaterialTheme.colorScheme.error)
        }
      },
      dismissButton = {
        TextButton(onClick = { showClearConfirm = false }) {
          Text("Cancelar")
        }
      }
    )
  }
}

@Composable
fun SessionLogItem(session: StudySessionRecord) {
  val levelColor = if (session.educationLevel == EducationLevel.FUNDAMENTAL) FundamentalGreen else MedioAmber

  Surface(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(14.dp),
    color = MaterialTheme.colorScheme.surface,
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(levelColor)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
          Text(
            text = session.subjectName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
          )
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = session.educationLevel.label,
              style = MaterialTheme.typography.labelSmall,
              color = levelColor,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "• ${session.completedAtFormatted}",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer
      ) {
        Text(
          text = "+${session.durationMinutes} min",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
      }
    }
  }
}
