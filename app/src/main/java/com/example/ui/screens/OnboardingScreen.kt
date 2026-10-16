package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.BackgroundStyle
import com.example.model.EducationLevel
import com.example.model.FontScaleOption
import com.example.ui.components.Duo3DButton
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.DuoAmberDark
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark

@Composable
fun OnboardingScreen(
  onComplete: (levelChoice: String, petName: String, bgStyle: BackgroundStyle, fontScale: FontScaleOption) -> Unit
) {
  var selectedLevel by remember { mutableStateOf("HYBRID") } // "FUNDAMENTAL", "MEDIO", "HYBRID"
  var petName by remember { mutableStateOf("Foxy") }
  var selectedBg by remember { mutableStateOf(BackgroundStyle.LIGHT_PAPER) }
  var selectedFont by remember { mutableStateOf(FontScaleOption.NORMAL) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color(selectedBg.hexBackground))
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 24.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Spacer(modifier = Modifier.height(12.dp))

    // Mascot Hero Presentation
    Box(
      modifier = Modifier
        .size(130.dp)
        .clip(CircleShape)
        .background(DuoAmber.copy(alpha = 0.2f))
        .border(4.dp, DuoAmber, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.study_fox_mascot_1792140274353),
        contentDescription = "Mascote Raposa",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "Boas-vindas ao RotaEstudo!",
      fontSize = 24.sp,
      fontWeight = FontWeight.Black,
      textAlign = TextAlign.Center,
      color = Color(selectedBg.hexText)
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Sua raposinha de estudos está pronta para te guiar. Vamos personalizar sua experiência!",
      style = MaterialTheme.typography.bodyMedium,
      color = Color(selectedBg.hexText).copy(alpha = 0.75f),
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(20.dp))

    // STEP 1: Escolha do Nível
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color(selectedBg.hexSurface)),
      border = BorderStroke(2.dp, Color(0xFFE2E8F0))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "1. Qual é o seu foco escolar principal?",
          fontWeight = FontWeight.Black,
          fontSize = 16.sp,
          color = Color(selectedBg.hexText)
        )
        Text(
          text = "Você pode alterar isso quando quiser nas configurações.",
          style = MaterialTheme.typography.bodySmall,
          color = Color(selectedBg.hexText).copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Level Option: Fundamental
        LevelChoiceCard(
          title = "Ensino Fundamental",
          subtitle = "Foco em fixar a base: Matemática Básica, Português, Ciências, História e Geografia.",
          badge = "🌱 Base",
          badgeColor = DuoGreen,
          isSelected = selectedLevel == "FUNDAMENTAL",
          onClick = { selectedLevel = "FUNDAMENTAL" },
          testTag = "choice_fundamental"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Level Option: Médio
        LevelChoiceCard(
          title = "Ensino Médio",
          subtitle = "Aprofundamento: Física, Química, Biologia, Matemática Avançada e Redação ENEM.",
          badge = "⚡ Avançado",
          badgeColor = DuoAmber,
          isSelected = selectedLevel == "MEDIO",
          onClick = { selectedLevel = "MEDIO" },
          testTag = "choice_medio"
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Level Option: Ambos Intercalados
        LevelChoiceCard(
          title = "Ambos Intercalados (Recomendado)",
          subtitle = "Alterna continuamente entre Fundamental e Médio para fortalecer a memória ativa.",
          badge = "⭐ Híbrido",
          badgeColor = DuoBlue,
          isSelected = selectedLevel == "HYBRID",
          onClick = { selectedLevel = "HYBRID" },
          testTag = "choice_hybrid"
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // STEP 2: Nome do Mascote
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color(selectedBg.hexSurface)),
      border = BorderStroke(2.dp, Color(0xFFE2E8F0))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "2. Dê um nome para sua Raposinha:",
          fontWeight = FontWeight.Black,
          fontSize = 16.sp,
          color = Color(selectedBg.hexText)
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
          value = petName,
          onValueChange = { petName = it },
          placeholder = { Text("Ex: Foxy, Kitsune, Sabichão...") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_onboarding_pet_name"),
          shape = RoundedCornerShape(14.dp),
          singleLine = true
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // STEP 3: Cor de Fundo & Tamanho das Letras
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color(selectedBg.hexSurface)),
      border = BorderStroke(2.dp, Color(0xFFE2E8F0))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "3. Personalize a aparência:",
          fontWeight = FontWeight.Black,
          fontSize = 16.sp,
          color = Color(selectedBg.hexText)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Cor de fundo preferida:",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = Color(selectedBg.hexText)
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          BackgroundStyle.values().forEach { style ->
            val isChosen = selectedBg == style
            Surface(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { selectedBg = style }
                .testTag("bg_choice_${style.name}"),
              color = Color(style.hexBackground),
              border = BorderStroke(2.dp, if (isChosen) DuoAmber else Color(0xFFCCCCCC)),
              shape = RoundedCornerShape(12.dp)
            ) {
              Column(
                modifier = Modifier.padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text(text = style.iconEmoji, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = style.label.split(" ").first(),
                  fontSize = 11.sp,
                  fontWeight = if (isChosen) FontWeight.Black else FontWeight.Normal,
                  color = Color(style.hexText)
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Tamanho das letras:",
          style = MaterialTheme.typography.bodySmall,
          fontWeight = FontWeight.Bold,
          color = Color(selectedBg.hexText)
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FontScaleOption.values().forEach { fontOpt ->
            val isChosen = selectedFont == fontOpt
            Surface(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(12.dp))
                .clickable { selectedFont = fontOpt }
                .testTag("font_choice_${fontOpt.name}"),
              color = if (isChosen) DuoAmber.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
              border = BorderStroke(2.dp, if (isChosen) DuoAmber else Color(0xFFDDDDDD)),
              shape = RoundedCornerShape(12.dp)
            ) {
              Box(
                modifier = Modifier.padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = fontOpt.label.split(" ").first(),
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  color = if (isChosen) DuoAmberDark else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Finish Button
    Duo3DButton(
      onClick = {
        val finalName = petName.trim().ifEmpty { "Foxy" }
        onComplete(selectedLevel, finalName, selectedBg, selectedFont)
      },
      faceColor = DuoAmber,
      shadowColor = DuoAmberDark,
      modifier = Modifier.fillMaxWidth(),
      testTag = "btn_start_journey"
    ) {
      Text(
        text = "COMEÇAR MINHA JORNADA 🦊",
        fontWeight = FontWeight.Black,
        fontSize = 16.sp,
        color = Color.White
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun LevelChoiceCard(
  title: String,
  subtitle: String,
  badge: String,
  badgeColor: Color,
  isSelected: Boolean,
  onClick: () -> Unit,
  testTag: String
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(14.dp))
      .clickable(onClick = onClick)
      .testTag(testTag),
    shape = RoundedCornerShape(14.dp),
    color = if (isSelected) badgeColor.copy(alpha = 0.12f) else Color.Transparent,
    border = BorderStroke(2.dp, if (isSelected) badgeColor else Color(0xFFE2E8F0))
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(24.dp)
          .clip(CircleShape)
          .background(if (isSelected) badgeColor else Color(0xFFE2E8F0)),
        contentAlignment = Alignment.Center
      ) {
        if (isSelected) {
          Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = title,
            fontWeight = FontWeight.Black,
            fontSize = 15.sp
          )
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = badgeColor.copy(alpha = 0.2f)
          ) {
            Text(
              text = badge,
              fontSize = 10.sp,
              fontWeight = FontWeight.Black,
              color = badgeColor,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
