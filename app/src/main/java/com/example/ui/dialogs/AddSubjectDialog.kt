package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.EducationLevel
import com.example.ui.theme.FundamentalGreen
import com.example.ui.theme.MedioAmber

@Composable
fun AddSubjectDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, level: EducationLevel, category: String, tip: String, topicsText: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var selectedLevel by remember { mutableStateOf(EducationLevel.FUNDAMENTAL) }
  var category by remember { mutableStateOf("Geral") }
  var tip by remember { mutableStateOf("") }
  var topicsText by remember { mutableStateOf("") }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.92f)
        .padding(vertical = 16.dp)
        .testTag("add_subject_dialog_card"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(
        modifier = Modifier
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        Text(
          text = "Adicionar Matéria à Rota",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "Personalize sua rota de estudos intercalando entre os níveis.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Level selection chips
        Text(
          text = "Nível Escolar:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          FilterChip(
            selected = selectedLevel == EducationLevel.FUNDAMENTAL,
            onClick = { selectedLevel = EducationLevel.FUNDAMENTAL },
            label = { Text("Ensino Fundamental") },
            modifier = Modifier.testTag("chip_level_fundamental"),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = FundamentalGreen.copy(alpha = 0.2f),
              selectedLabelColor = FundamentalGreen
            )
          )

          FilterChip(
            selected = selectedLevel == EducationLevel.MEDIO,
            onClick = { selectedLevel = EducationLevel.MEDIO },
            label = { Text("Ensino Médio") },
            modifier = Modifier.testTag("chip_level_medio"),
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = MedioAmber.copy(alpha = 0.2f),
              selectedLabelColor = MedioAmber
            )
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Nome da Matéria *") },
          placeholder = { Text("Ex: Geometria Analítica, Inglês Instrumental...") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_subject_name"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = category,
          onValueChange = { category = it },
          label = { Text("Área do Conhecimento") },
          placeholder = { Text("Ex: Exatas, Humanas, Biológicas, Linguagens") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_subject_category"),
          singleLine = true,
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = tip,
          onValueChange = { tip = it },
          label = { Text("Dica Rápida / Estratégia de Foco") },
          placeholder = { Text("Ex: Fazer resumo ativo e 5 exercícios.") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_subject_tip"),
          maxLines = 2,
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
          value = topicsText,
          onValueChange = { topicsText = it },
          label = { Text("Tópicos Chave (um por linha)") },
          placeholder = { Text("Conceito A\nConceito B\nExercícios de Fixação") },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("input_subject_topics"),
          minLines = 3,
          maxLines = 5,
          shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End
        ) {
          OutlinedButton(
            onClick = onDismiss,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("add_subject_cancel")
          ) {
            Text("Cancelar")
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = {
              if (name.isNotBlank()) {
                onConfirm(name, selectedLevel, category, tip, topicsText)
              }
            },
            enabled = name.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.testTag("add_subject_save")
          ) {
            Text("Adicionar à Rota", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}
