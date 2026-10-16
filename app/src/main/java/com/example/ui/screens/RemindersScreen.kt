package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.model.RotationStrategy
import com.example.model.StudyReminderConfig
import com.example.ui.theme.FundamentalGreen
import com.example.ui.theme.MedioAmber
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun RemindersScreen(viewModel: StudyViewModel) {
  val context = LocalContext.current
  val config by viewModel.reminderConfig.collectAsState()

  var hasNotificationPermission by remember {
    mutableStateOf(
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
      } else true
    )
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    hasNotificationPermission = isGranted
  }

  var selectedHour by remember(config.reminderHour) { mutableIntStateOf(config.reminderHour) }
  var selectedMinute by remember(config.reminderMinute) { mutableIntStateOf(config.reminderMinute) }
  var isEnabled by remember(config.isReminderEnabled) { mutableStateOf(config.isReminderEnabled) }
  var selectedDays by remember(config.daysOfWeek) { mutableStateOf(config.daysOfWeek) }
  var selectedStrategy by remember(config.rotationStrategy) { mutableStateOf(config.rotationStrategy) }
  var testNotificationSentBanner by remember { mutableStateOf(false) }

  val dayLabels = listOf(
    1 to "Seg",
    2 to "Ter",
    3 to "Qua",
    4 to "Qui",
    5 to "Sex",
    6 to "Sáb",
    7 to "Dom"
  )

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Permission Card if not granted
    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MedioAmber.copy(alpha = 0.15f)),
        border = BorderStroke(1.dp, MedioAmber.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.Notifications, contentDescription = null, tint = MedioAmber)
          Spacer(modifier = Modifier.width(12.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Ativar Notificações",
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.bodyMedium,
              color = MedioAmber
            )
            Text(
              text = "Permita notificações para que o app envie lembretes no horário programado.",
              style = MaterialTheme.typography.bodySmall
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Button(
            onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("btn_grant_notification_permission")
          ) {
            Text("Permitir")
          }
        }
      }
    }

    // Main Reminder Toggle & Time Card
    ElevatedCard(
      modifier = Modifier
        .fillMaxWidth()
        .testTag("reminder_config_card"),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = if (isEnabled) Icons.Default.AlarmOn else Icons.Default.Alarm,
              contentDescription = null,
              tint = if (isEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
              Text(
                text = "Lembrete Diário de Estudo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = if (isEnabled) "Ativo: você receberá avisos para focar" else "Desativado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Switch(
            checked = isEnabled,
            onCheckedChange = { checked ->
              isEnabled = checked
              viewModel.updateReminderConfig(
                config.copy(isReminderEnabled = checked)
              )
            },
            modifier = Modifier.testTag("switch_reminder_enable")
          )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Time selector row
        Text(
          text = "Horário do Lembrete:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Hour Picker
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Button(
                onClick = {
                  val newH = if (selectedHour <= 0) 23 else selectedHour - 1
                  selectedHour = newH
                  viewModel.updateReminderConfig(config.copy(reminderHour = newH))
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(4.dp)
              ) {
                Text("-")
              }
              Text(
                text = String.format("%02d", selectedHour),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
              )
              Button(
                onClick = {
                  val newH = (selectedHour + 1) % 24
                  selectedHour = newH
                  viewModel.updateReminderConfig(config.copy(reminderHour = newH))
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(4.dp)
              ) {
                Text("+")
              }
            }
          }

          Text(
            text = ":",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp)
          )

          // Minute Picker
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.padding(horizontal = 4.dp)
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Button(
                onClick = {
                  val newM = if (selectedMinute <= 0) 55 else (selectedMinute - 5)
                  selectedMinute = newM
                  viewModel.updateReminderConfig(config.copy(reminderMinute = newM))
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(4.dp)
              ) {
                Text("-")
              }
              Text(
                text = String.format("%02d", selectedMinute),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp)
              )
              Button(
                onClick = {
                  val newM = (selectedMinute + 5) % 60
                  selectedMinute = newM
                  viewModel.updateReminderConfig(config.copy(reminderMinute = newM))
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(4.dp)
              ) {
                Text("+")
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Days of Week
        Text(
          text = "Dias de Notificação:",
          style = MaterialTheme.typography.labelMedium,
          fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          dayLabels.forEach { (dayId, label) ->
            val isDaySelected = selectedDays.contains(dayId)
            Surface(
              modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .clickable {
                  val newDays = if (isDaySelected) {
                    if (selectedDays.size > 1) selectedDays - dayId else selectedDays
                  } else {
                    selectedDays + dayId
                  }
                  selectedDays = newDays
                  viewModel.updateReminderConfig(config.copy(daysOfWeek = newDays))
                }
                .testTag("day_chip_$dayId"),
              shape = CircleShape,
              color = if (isDaySelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
              border = BorderStroke(
                1.dp,
                if (isDaySelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
              )
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = label,
                  fontSize = 12.sp,
                  fontWeight = if (isDaySelected) FontWeight.Bold else FontWeight.Normal,
                  color = if (isDaySelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }
    }

    // Instant Notification Test Button
    Card(
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
      ),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Send,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Testar Notificação Agora",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
          )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "Veja como o lembrete aparece na barra de notificações com a matéria da rota e atalho para começar.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !hasNotificationPermission) {
              permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
              viewModel.triggerTestNotification()
              testNotificationSentBanner = true
            }
          },
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("btn_trigger_test_notification")
        ) {
          Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text("Disparar Lembrete na Barra do Android", fontWeight = FontWeight.Bold)
        }

        if (testNotificationSentBanner) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "✓ Notificação enviada! Verifique a barra de status do seu aparelho.",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = FundamentalGreen
          )
        }
      }
    }

    // Alternating Rotation Strategy Card
    OutlinedCard(
      shape = RoundedCornerShape(18.dp),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Modo de Rota das Matérias:",
          style = MaterialTheme.typography.titleSmall,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))

        RotationStrategy.values().forEach { strategy ->
          val isCurrent = selectedStrategy == strategy
          Surface(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable {
                selectedStrategy = strategy
                viewModel.updateReminderConfig(config.copy(rotationStrategy = strategy))
              }
              .testTag("strategy_chip_${strategy.name}"),
            shape = RoundedCornerShape(12.dp),
            color = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surface,
            border = BorderStroke(
              1.dp,
              if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
          ) {
            Row(
              modifier = Modifier.padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Box(
                modifier = Modifier
                  .size(24.dp)
                  .clip(CircleShape)
                  .background(if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
              ) {
                if (isCurrent) {
                  Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = strategy.title,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Bold
                )
                Text(
                  text = strategy.description,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}
