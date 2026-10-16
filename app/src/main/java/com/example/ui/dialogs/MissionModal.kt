package com.example.ui.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.model.EducationLevel
import com.example.model.PathNode
import com.example.ui.components.SubjectIconHelper
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGreen

@Composable
fun MissionModal(
  node: PathNode?,
  onDismiss: () -> Unit,
  onStartMission: (PathNode) -> Unit,
  onOpenQuiz: (PathNode) -> Unit
) {
  if (node == null) return

  val isFundamental = node.subject.educationLevel == EducationLevel.FUNDAMENTAL
  val themeColor = if (isFundamental) DuoGreen else DuoAmber

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .padding(vertical = 12.dp)
        .testTag("mission_modal_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = themeColor.copy(alpha = 0.15f)
          ) {
            Text(
              text = if (isFundamental) "🌱 ENSINO FUNDAMENTAL" else "⚡ ENSINO MÉDIO",
              fontWeight = FontWeight.Black,
              fontSize = 11.sp,
              color = themeColor,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(36.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Fechar")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Center Mascot + Subject Icon Combo
        Box(contentAlignment = Alignment.BottomEnd) {
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(CircleShape)
              .background(themeColor)
              .border(3.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = SubjectIconHelper.getIcon(node.subject.iconKey),
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(40.dp)
            )
          }

          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.study_fox_mascot_1792140274353),
              contentDescription = "Mascote Raposa",
              modifier = Modifier.fillMaxWidth(),
              contentScale = ContentScale.Crop
            )
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Complete Subject Name
        Text(
          text = node.subject.name,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Black,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Full Study Strategy Tip without cuts
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = themeColor.copy(alpha = 0.1f),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "💡 ${node.subject.shortTip}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(10.dp),
            lineHeight = 19.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Topics List Preview
        Column(modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Conteúdo Desta Missão:",
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = themeColor
          )
          Spacer(modifier = Modifier.height(4.dp))
          node.subject.topics.take(4).forEach { topic ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(vertical = 2.dp)
            ) {
              Icon(Icons.Default.CheckCircle, contentDescription = null, tint = themeColor, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(text = topic, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Rewards banner
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MaterialTheme.colorScheme.surfaceVariant,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("⚡", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "+50 XP", fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("💎", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "+15 Gemas", fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("⭐", fontSize = 16.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "3 Estrelas", fontWeight = FontWeight.Black, fontSize = 13.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Responsive, Highly Clickable Start Button (48dp min height)
        Button(
          onClick = {
            onStartMission(node)
            onDismiss()
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("btn_start_duo_mission"),
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = themeColor)
        ) {
          Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "COMEÇAR MISSÃO DE FOCO",
            fontWeight = FontWeight.Black,
            fontSize = 15.sp,
            color = Color.White
          )
        }

        if (node.subject.questions.isNotEmpty()) {
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedButton(
            onClick = {
              onOpenQuiz(node)
              onDismiss()
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("btn_start_duo_quiz"),
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.5.dp, DuoBlue)
          ) {
            Icon(Icons.Default.Quiz, contentDescription = null, tint = DuoBlue, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "TESTAR COM MINI-QUIZ (+15 XP)",
              fontWeight = FontWeight.Black,
              fontSize = 14.sp,
              color = DuoBlue
            )
          }
        }
      }
    }
  }
}
