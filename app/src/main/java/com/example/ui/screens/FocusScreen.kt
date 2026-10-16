package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EducationLevel
import com.example.ui.components.Duo3DButton
import com.example.ui.components.MascotSpeechBubble
import com.example.ui.components.SubjectIconHelper
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.DuoAmberDark
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoBlueDark
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoPurple
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun FocusScreen(
  viewModel: StudyViewModel,
  onNavigateToRotation: () -> Unit
) {
  val currentSubject by viewModel.currentSubject.collectAsState()
  val nextSubject by viewModel.nextSubject.collectAsState()
  val isRunning by viewModel.isTimerRunning.collectAsState()
  val isBreak by viewModel.isBreakMode.collectAsState()
  val secondsRemaining by viewModel.timerSecondsRemaining.collectAsState()
  val targetMinutes by viewModel.currentFocusTargetMinutes.collectAsState()
  val ambientPlaying by viewModel.ambientSoundPlaying.collectAsState()
  val petCustomization by viewModel.petCustomization.collectAsState()

  // Track completed study topic checklist items
  val checkedTopics = remember { mutableStateMapOf<String, Boolean>() }

  val isFundamental = currentSubject?.educationLevel == EducationLevel.FUNDAMENTAL
  val activeColor = when {
    isBreak -> DuoBlue
    isFundamental -> DuoGreen
    else -> DuoAmber
  }
  val shadowColor = when {
    isBreak -> DuoBlueDark
    isFundamental -> DuoGreenDark
    else -> DuoAmberDark
  }

  val totalSeconds = if (isBreak) 5 * 60 else targetMinutes * 60
  val progress = if (totalSeconds > 0) {
    (secondsRemaining.toFloat() / totalSeconds.toFloat()).coerceIn(0f, 1f)
  } else 0f

  val animatedProgress by animateFloatAsState(
    targetValue = progress,
    animationSpec = tween(durationMillis = 500),
    label = "timer_progress"
  )

  val minutes = secondsRemaining / 60
  val seconds = secondsRemaining % 60
  val timeFormatted = String.format("%02d:%02d", minutes, seconds)

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Mascot Speech Bubble encouraging focus
    MascotSpeechBubble(
      message = when {
        isBreak -> "Muito bem! Descanse a mente por 5 minutos, beba água e se prepare para a próxima matéria!"
        isRunning -> "Foco total na missão de ${currentSubject?.name ?: "estudos"}! Siga o roteiro abaixo para absorver o conteúdo."
        else -> "Preparado para estudar ${currentSubject?.name ?: "sua matéria"}? Veja o que estudar no roteiro abaixo e inicie o cronômetro!"
      },
      mascotName = petCustomization.petName
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Active Mission Card (Full title, no cuts!)
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("active_mission_banner"),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      border = BorderStroke(2.dp, Color(0xFFE2E8F0))
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = activeColor.copy(alpha = 0.15f)
          ) {
            Text(
              text = if (isBreak) "☕ INTERVALO ATIVO" else (if (isFundamental) "🌱 ENSINO FUNDAMENTAL" else "⚡ ENSINO MÉDIO"),
              fontWeight = FontWeight.Black,
              fontSize = 11.sp,
              color = activeColor,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = DuoPurple.copy(alpha = 0.15f)
            ) {
              Text(
                text = "⚡ +50 XP",
                fontWeight = FontWeight.Black,
                fontSize = 12.sp,
                color = DuoPurple,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Spacer(modifier = Modifier.width(6.dp))

            IconButton(
              onClick = { viewModel.toggleAmbientSound() },
              modifier = Modifier.size(36.dp)
            ) {
              Icon(
                imageVector = if (ambientPlaying) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                contentDescription = "Som",
                tint = if (ambientPlaying) DuoBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(46.dp)
              .clip(CircleShape)
              .background(activeColor)
              .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (isBreak) Icons.Default.GraphicEq else SubjectIconHelper.getIcon(currentSubject?.iconKey ?: "school"),
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(26.dp)
            )
          }

          Spacer(modifier = Modifier.width(12.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (isBreak) "Momento de Descanso" else (currentSubject?.name ?: "Matemática Básica"),
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black,
              lineHeight = 22.sp
            )
            Text(
              text = if (isBreak) "Alongue-se e respire fundo." else (currentSubject?.category ?: "Geral"),
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Circular Countdown Timer (Ergonomic size that fits all phones)
    Box(
      modifier = Modifier
        .size(200.dp)
        .testTag("duo_circular_timer"),
      contentAlignment = Alignment.Center
    ) {
      Canvas(modifier = Modifier.size(185.dp)) {
        val strokeWidth = 14.dp.toPx()
        drawCircle(
          color = Color(0xFFE5E5E5),
          style = Stroke(width = strokeWidth)
        )
        drawArc(
          color = activeColor,
          startAngle = -90f,
          sweepAngle = 360f * animatedProgress,
          useCenter = false,
          style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
      }

      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
          text = timeFormatted,
          fontSize = 42.sp,
          fontWeight = FontWeight.Black,
          letterSpacing = 1.sp
        )

        Surface(
          shape = RoundedCornerShape(10.dp),
          color = if (isRunning) activeColor.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
        ) {
          Text(
            text = when {
              isBreak -> "PAUSA"
              isRunning -> "EM FOCO"
              else -> "PRONTO"
            },
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            color = if (isRunning) activeColor else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Ergonomic, Fully Accessible Controls (Fits comfortably without cutting)
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Reset button
      OutlinedButton(
        onClick = { viewModel.resetTimer() },
        modifier = Modifier
          .height(48.dp)
          .weight(1f)
          .testTag("btn_reset_timer"),
        shape = RoundedCornerShape(14.dp)
      ) {
        Icon(Icons.Default.Refresh, contentDescription = "Reiniciar", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Reiniciar", fontSize = 13.sp, fontWeight = FontWeight.Bold)
      }

      // Main Start/Pause Button (Responsive, easy to tap!)
      Button(
        onClick = {
          if (isRunning) viewModel.pauseTimer() else viewModel.startTimer()
        },
        modifier = Modifier
          .height(48.dp)
          .weight(2f)
          .testTag("btn_toggle_timer"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isRunning) DuoAmber else activeColor
        )
      ) {
        Icon(
          imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
          contentDescription = null,
          tint = Color.White,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = if (isRunning) "PAUSAR" else "INICIAR FOCO",
          fontWeight = FontWeight.Black,
          fontSize = 14.sp,
          color = Color.White
        )
      }

      // Skip to next subject
      OutlinedButton(
        onClick = { viewModel.advanceToNextSubject() },
        modifier = Modifier
          .height(48.dp)
          .weight(1f)
          .testTag("btn_skip_subject"),
        shape = RoundedCornerShape(14.dp)
      ) {
        Icon(Icons.Default.SkipNext, contentDescription = "Pular", modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Pular", fontSize = 13.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Duration preset chips
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally)
    ) {
      listOf(15, 25, 45, 50).forEach { mins ->
        val selected = targetMinutes == mins && !isBreak
        FilterChip(
          selected = selected,
          onClick = { viewModel.setFocusDuration(mins) },
          label = { Text("${mins}m", fontWeight = FontWeight.Black) },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = activeColor,
            selectedLabelColor = Color.White
          ),
          shape = RoundedCornerShape(10.dp)
        )
      }

      FilterChip(
        selected = isBreak,
        onClick = { viewModel.setBreakDuration(5) },
        label = { Text("Pausa 5m", fontWeight = FontWeight.Black) },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = DuoBlue,
          selectedLabelColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
      )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ========================================================
    // DETAILED STUDY GUIDANCE SECTION ("O QUE ESTUDAR NESTE TEMPO")
    // ========================================================
    if (currentSubject != null && !isBreak) {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, activeColor.copy(alpha = 0.5f))
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.FormatListBulleted, contentDescription = null, tint = activeColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "O que estudar nesta sessão de ${targetMinutes} min:",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Full Study Tip without cuts
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = activeColor.copy(alpha = 0.1f),
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(
              modifier = Modifier.padding(10.dp),
              verticalAlignment = Alignment.Top
            ) {
              Icon(Icons.Default.Lightbulb, contentDescription = null, tint = activeColor, modifier = Modifier.size(20.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "Estratégia Recomendada:",
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = activeColor
                )
                Text(
                  text = currentSubject?.shortTip ?: "Estude os conceitos e faça anotações ativas.",
                  style = MaterialTheme.typography.bodyMedium,
                  lineHeight = 19.sp
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Step-by-step Time Breakdown Routine
          Text(
            text = "Roteiro de Estudos Sugerido:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.primary
          )

          Spacer(modifier = Modifier.height(6.dp))

          val step1 = if (targetMinutes <= 15) "1º ao 3º min: Leitura rápida e fixação dos conceitos centrais." else "1º ao 5º min: Leitura atenta da teoria e resumo das fórmulas ou ideias chave."
          val step2 = if (targetMinutes <= 15) "3º ao 12º min: Resolução de 2 a 3 exercícios práticos da matéria." else "5º ao 20º min: Resolução intensiva de exercícios práticos da matéria."
          val step3 = if (targetMinutes <= 15) "12º ao 15º min: Auto-explicação rápida do que você entendeu." else "20º ao 25º min: Revisão mental, conferência dos erros e anotação final."

          listOf(
            "📖 Teoria Inicial" to step1,
            "✍️ Prática & Exercícios" to step2,
            "🧠 Auto-Explicação" to step3
          ).forEach { (fase, desc) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp),
              verticalAlignment = Alignment.Top
            ) {
              Text(
                text = "•",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = activeColor,
                modifier = Modifier.padding(end = 6.dp)
              )
              Column {
                Text(text = fase, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Topics Checklist (Student can check off what they covered!)
          Text(
            text = "Tópicos da Matéria (Toque para marcar o que estudou):",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )

          Spacer(modifier = Modifier.height(6.dp))

          currentSubject?.topics?.forEach { topic ->
            val isChecked = checkedTopics[topic] == true
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .clickable { checkedTopics[topic] = !isChecked }
                .padding(vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (isChecked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = null,
                tint = if (isChecked) activeColor else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = topic,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isChecked) FontWeight.Bold else FontWeight.Normal,
                color = if (isChecked) activeColor else MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // Next upcoming subject in rotation preview
    if (nextSubject != null) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { viewModel.advanceToNextSubject() }
          .testTag("next_subject_preview_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, Color(0xFFE2E8F0))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                  if (nextSubject?.educationLevel == EducationLevel.FUNDAMENTAL) DuoGreen else DuoAmber
                ),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = SubjectIconHelper.getIcon(nextSubject?.iconKey ?: "school"),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
              )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
              Text(
                text = "PRÓXIMA NA ROTA (${if (nextSubject?.educationLevel == EducationLevel.FUNDAMENTAL) "FUNDAMENTAL" else "MÉDIO"}):",
                fontWeight = FontWeight.Black,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = nextSubject?.name ?: "",
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
              )
            }
          }

          Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xFFAAAAAA))
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Mini-Quiz challenge button (Compact, fully clickable, never overflowing)
    if (currentSubject?.questions?.isNotEmpty() == true) {
      Button(
        onClick = { viewModel.openQuizForCurrentSubject() },
        modifier = Modifier
          .fillMaxWidth()
          .height(50.dp)
          .testTag("btn_trigger_quiz_checkpoint"),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = DuoBlue)
      ) {
        Icon(Icons.Default.Quiz, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "TESTAR COM MINI-QUIZ (+15 XP)",
          fontWeight = FontWeight.Black,
          fontSize = 14.sp,
          color = Color.White
        )
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}
