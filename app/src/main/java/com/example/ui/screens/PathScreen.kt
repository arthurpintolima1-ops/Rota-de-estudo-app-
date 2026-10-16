package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EducationLevel
import com.example.model.PathNode
import com.example.model.PathNodeType
import com.example.ui.components.MascotSpeechBubble
import com.example.ui.components.SubjectIconHelper
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.DuoAmberDark
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoBlueDark
import com.example.ui.theme.DuoBorderLight
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoPurple
import com.example.ui.theme.DuoPurpleDark
import com.example.ui.theme.DuoYellow
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun PathScreen(
  viewModel: StudyViewModel,
  onNodeClick: (PathNode) -> Unit
) {
  val pathNodes by viewModel.pathNodes.collectAsState()
  val currentSubject by viewModel.currentSubject.collectAsState()
  val streakDays by viewModel.streakDays.collectAsState()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .testTag("path_screen_list"),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Unit Header Banner (Duolingo Header)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 10.dp)
          .testTag("unit_banner_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DuoGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "SEÇÃO 1 • ROTA DE FOCO",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.85f),
                letterSpacing = 1.sp
              )
              Text(
                text = "Alternância Fundamental & Médio",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color.White.copy(alpha = 0.25f)
            ) {
              Text(
                text = "🌱 ↔ ⚡",
                fontSize = 18.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Complete cada missão para avançar na trilha, coletar gemas e manter sua ofensiva acesa!",
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = 0.9f)
          )
        }
      }
    }

    // Mascot Encouragement Bubble
    item {
      val petCustomization by viewModel.petCustomization.collectAsState()
      MascotSpeechBubble(
        message = if (streakDays > 0) {
          "Incrível! Sua ofensiva é de $streakDays dias! Continue alternando entre as matérias para ganhar +50 XP!"
        } else {
          "Olá! Eu sou ${petCustomization.petName}, sua raposinha de estudos! Vamos alternar entre Fundamental e Médio para turbinar sua memória?"
        },
        mascotName = petCustomization.petName,
        modifier = Modifier.padding(horizontal = 14.dp)
      )
      Spacer(modifier = Modifier.height(10.dp))
    }

    // Winding stepping-stone path nodes
    itemsIndexed(pathNodes, key = { _, node -> node.id }) { index, node ->
      PathNodeItem(
        node = node,
        onClick = { onNodeClick(node) }
      )
      // Visual connector dots
      if (index < pathNodes.size - 1) {
        PathConnectorDots(
          fromOffset = node.horizontalOffsetPercent,
          toOffset = pathNodes[index + 1].horizontalOffsetPercent
        )
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
fun PathNodeItem(
  node: PathNode,
  onClick: () -> Unit
) {
  val isFundamental = node.subject.educationLevel == EducationLevel.FUNDAMENTAL

  val faceColor = when {
    node.isLocked -> Color(0xFFE5E5E5)
    node.nodeType == PathNodeType.CHEST -> DuoYellow
    isFundamental -> DuoGreen
    else -> DuoAmber
  }

  val shadowColor = when {
    node.isLocked -> Color(0xFFCCCCCC)
    node.nodeType == PathNodeType.CHEST -> DuoAmberDark
    isFundamental -> DuoGreenDark
    else -> DuoAmberDark
  }

  // Pulsing animation for current node
  val infiniteTransition = rememberInfiniteTransition(label = "pulse_node")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = if (node.isCurrent) 1.08f else 1f,
    animationSpec = infiniteRepeatable(
      animation = tween(800),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  // Horizontal offset to create the winding serpentine path
  val xOffset = (node.horizontalOffsetPercent * 110).dp

  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.offset(x = xOffset)
    ) {
      // Crown or Start badge above active node
      if (node.isCurrent) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = DuoPurple,
          modifier = Modifier.padding(bottom = 6.dp)
        ) {
          Text(
            text = "COMEÇAR",
            color = Color.White,
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
          )
        }
      }

      // Chunky 3D Circle Node
      Box(
        modifier = Modifier
          .size(80.dp)
          .scale(if (node.isCurrent) pulseScale else 1f)
          .clickable(enabled = !node.isLocked, onClick = onClick)
          .testTag("node_${node.id}"),
        contentAlignment = Alignment.Center
      ) {
        // 3D Shadow layer
        Box(
          modifier = Modifier
            .size(76.dp)
            .offset(y = 6.dp)
            .clip(CircleShape)
            .background(shadowColor)
        )

        // Top face
        Box(
          modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(faceColor)
            .border(
              width = if (node.isCurrent) 4.dp else 2.dp,
              color = if (node.isCurrent) Color.White else Color.Transparent,
              shape = CircleShape
            ),
          contentAlignment = Alignment.Center
        ) {
          if (node.isLocked) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = "Bloqueado",
              tint = Color(0xFFAAAAAA),
              modifier = Modifier.size(30.dp)
            )
          } else if (node.nodeType == PathNodeType.CHEST) {
            Text("🎁", fontSize = 32.sp)
          } else {
            Icon(
              imageVector = SubjectIconHelper.getIcon(node.subject.iconKey),
              contentDescription = node.subject.name,
              tint = Color.White,
              modifier = Modifier.size(36.dp)
            )
          }
        }

        // Checkmark badge if completed
        if (node.isCompleted) {
          Box(
            modifier = Modifier
              .align(Alignment.BottomEnd)
              .size(26.dp)
              .clip(CircleShape)
              .background(DuoYellow)
              .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = "Concluído",
              tint = Color.White,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      // Node Label (Subject + Level badge)
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = node.subject.name,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = if (node.isLocked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
        maxLines = 2,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        modifier = Modifier.widthIn(max = 130.dp)
      )

      // Level pill (Fund. vs Médio)
      Surface(
        shape = RoundedCornerShape(6.dp),
        color = if (isFundamental) DuoGreen.copy(alpha = 0.2f) else DuoAmber.copy(alpha = 0.2f)
      ) {
        Text(
          text = if (isFundamental) "🌱 Fund." else "⚡ Médio",
          fontSize = 10.sp,
          fontWeight = FontWeight.Black,
          color = if (isFundamental) DuoGreenDark else DuoAmberDark,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}

@Composable
fun PathConnectorDots(
  fromOffset: Float,
  toOffset: Float
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .height(28.dp),
    contentAlignment = Alignment.Center
  ) {
    Canvas(modifier = Modifier.fillMaxSize()) {
      val startX = size.width / 2 + (fromOffset * 110.dp.toPx())
      val endX = size.width / 2 + (toOffset * 110.dp.toPx())

      for (i in 1..3) {
        val fraction = i / 4f
        val x = startX + (endX - startX) * fraction
        val y = size.height * fraction
        drawCircle(
          color = Color(0xFFE5E5E5),
          radius = 5.dp.toPx(),
          center = androidx.compose.ui.geometry.Offset(x, y)
        )
      }
    }
  }
}
