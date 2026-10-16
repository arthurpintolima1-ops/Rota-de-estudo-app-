package com.example.ui.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.QuizQuestion
import com.example.model.StudySubject
import com.example.ui.components.MascotSpeechBubble
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoBlueDark
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenContainer
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoRed
import com.example.ui.theme.DuoRedContainer
import com.example.ui.theme.DuoRedDark
import com.example.util.GameSoundHelper

@Composable
fun QuizDialog(
  subject: StudySubject,
  question: QuizQuestion?,
  onDismiss: () -> Unit,
  onAnswerCorrect: () -> Unit = {}
) {
  if (question == null) return

  var selectedIndex by remember { mutableStateOf<Int?>(null) }
  val isAnswered = selectedIndex != null
  val isCorrect = selectedIndex == question.correctOptionIndex

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .padding(vertical = 12.dp)
        .testTag("quiz_dialog_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(16.dp)
      ) {
        // Header (Close + Progress bar)
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(40.dp)
          ) {
            Icon(Icons.Default.Close, contentDescription = "Fechar Desafio")
          }

          // Progress Bar
          Box(
            modifier = Modifier
              .weight(1f)
              .height(10.dp)
              .padding(horizontal = 8.dp)
              .clip(RoundedCornerShape(5.dp))
              .background(Color(0xFFE2E8F0))
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth(if (isAnswered) 1f else 0.5f)
                .height(10.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(DuoGreen)
            )
          }

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Favorite, contentDescription = null, tint = DuoRed, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(3.dp))
            Text("5", fontWeight = FontWeight.Black, color = DuoRed, fontSize = 14.sp)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Mascot speech bubble
        MascotSpeechBubble(
          message = "Desafio de ${subject.name}! Leia com calma e toque na opção correta:"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Full Question Box - complete text, no truncation!
        Surface(
          shape = RoundedCornerShape(14.dp),
          color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
          border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = question.question,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(14.dp),
            lineHeight = 22.sp
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Options List - Flexible heights, wrap text cleanly, guaranteed touch target of at least 48dp
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          question.options.forEachIndexed { index, option ->
            val isOptionSelected = selectedIndex == index
            val isThisCorrect = index == question.correctOptionIndex

            val backgroundColor = when {
              !isAnswered -> if (isOptionSelected) DuoBlue.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
              isThisCorrect -> DuoGreenContainer
              isOptionSelected && !isThisCorrect -> DuoRedContainer
              else -> MaterialTheme.colorScheme.surface
            }

            val borderColor = when {
              !isAnswered -> if (isOptionSelected) DuoBlue else Color(0xFFCBD5E1)
              isThisCorrect -> DuoGreen
              isOptionSelected && !isThisCorrect -> DuoRed
              else -> Color(0xFFE2E8F0)
            }

            val letters = listOf("A", "B", "C", "D", "E")
            val letter = letters.getOrElse(index) { "${index + 1}" }

            Surface(
              modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 52.dp)
                .clip(RoundedCornerShape(14.dp))
                .clickable(enabled = !isAnswered) {
                  selectedIndex = index
                  if (index == question.correctOptionIndex) {
                    GameSoundHelper.playSuccessChime()
                    onAnswerCorrect()
                  } else {
                    GameSoundHelper.playWrongBeep()
                  }
                }
                .testTag("quiz_option_$index"),
              shape = RoundedCornerShape(14.dp),
              color = backgroundColor,
              border = BorderStroke(2.dp, borderColor)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Option Letter Badge
                Box(
                  modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                      when {
                        isAnswered && isThisCorrect -> DuoGreen
                        isAnswered && isOptionSelected -> DuoRed
                        isOptionSelected -> DuoBlue
                        else -> Color(0xFFE2E8F0)
                      }
                    ),
                  contentAlignment = Alignment.Center
                ) {
                  if (isAnswered && isThisCorrect) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                  } else if (isAnswered && isOptionSelected && !isThisCorrect) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                  } else {
                    Text(
                      text = letter,
                      fontWeight = FontWeight.Black,
                      fontSize = 14.sp,
                      color = if (isOptionSelected) Color.White else Color(0xFF334155)
                    )
                  }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Full option text with multiline wrapping
                Text(
                  text = option,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = if (isOptionSelected || (isAnswered && isThisCorrect)) FontWeight.Bold else FontWeight.Medium,
                  modifier = Modifier.weight(1f),
                  lineHeight = 20.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Evaluation & Result Box
        AnimatedVisibility(
          visible = isAnswered,
          enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn()
        ) {
          Column(modifier = Modifier.fillMaxWidth()) {
            Surface(
              shape = RoundedCornerShape(14.dp),
              color = if (isCorrect) DuoGreenContainer else DuoRedContainer,
              border = BorderStroke(1.5.dp, if (isCorrect) DuoGreen else DuoRed),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (isCorrect) DuoGreenDark else DuoRedDark,
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = if (isCorrect) "Resposta Correta! 🎉 +15 XP" else "Resposta Incorreta!",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = if (isCorrect) DuoGreenDark else DuoRedDark
                  )
                }

                if (question.explanation.isNotEmpty()) {
                  Spacer(modifier = Modifier.height(6.dp))
                  Text(
                    text = question.explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (isCorrect) DuoGreenDark else DuoRedDark,
                    lineHeight = 19.sp
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Large, easy to tap Continue Button
            Button(
              onClick = onDismiss,
              modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("quiz_finish_button"),
              shape = RoundedCornerShape(14.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (isCorrect) DuoGreen else DuoBlue
              )
            ) {
              Text(
                text = "CONTINUAR",
                fontWeight = FontWeight.Black,
                fontSize = 16.sp,
                color = Color.White
              )
            }
          }
        }

        // If not answered yet, show Skip option
        if (!isAnswered) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("quiz_skip_button"),
            shape = RoundedCornerShape(12.dp)
          ) {
            Text("Pular Desafio por Enquanto", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
