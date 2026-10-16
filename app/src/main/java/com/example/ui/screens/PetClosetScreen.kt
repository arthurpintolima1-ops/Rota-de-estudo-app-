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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PetCatalog
import com.example.model.AlternativeQuest
import com.example.model.BackgroundStyle
import com.example.model.FontScaleOption
import com.example.model.PetItem
import com.example.ui.components.Duo3DButton
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.DuoAmberDark
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoBlueDark
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoPurple
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun PetClosetScreen(viewModel: StudyViewModel) {
  val petCustomization by viewModel.petCustomization.collectAsState()
  val alternativeQuests by viewModel.alternativeQuests.collectAsState()
  val backgroundStyle by viewModel.backgroundStyle.collectAsState()
  val fontScale by viewModel.fontScale.collectAsState()

  var selectedCategoryTab by remember { mutableIntStateOf(0) } // 0: Chapéus, 1: Trajes, 2: Acessórios

  val currentItems = remember(selectedCategoryTab) {
    val categoryKey = when (selectedCategoryTab) {
      0 -> "hat"
      1 -> "outfit"
      else -> "accessory"
    }
    PetCatalog.allItems.filter { it.category == categoryKey }
  }

  val equippedHat = PetCatalog.allItems.find { it.id == petCustomization.equippedHatId }
  val equippedOutfit = PetCatalog.allItems.find { it.id == petCustomization.equippedOutfitId }
  val equippedAccessory = PetCatalog.allItems.find { it.id == petCustomization.equippedAccessoryId }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("pet_closet_screen"),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(6.dp))

      // Main Fox Mascot Preview Showcase
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, Color(0xFFE2E8F0))
      ) {
        Column(
          modifier = Modifier.padding(18.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
              .size(150.dp)
              .clip(CircleShape)
              .background(DuoAmber.copy(alpha = 0.15f))
              .border(4.dp, DuoAmber, CircleShape),
            contentAlignment = Alignment.Center
          ) {
            Image(
              painter = painterResource(id = R.drawable.study_fox_mascot_1792140274353),
              contentDescription = "Raposa",
              modifier = Modifier.fillMaxSize(),
              contentScale = ContentScale.Crop
            )

            // Overlays of equipped hat and accessory
            if (equippedHat != null) {
              Box(
                modifier = Modifier
                  .align(Alignment.TopEnd)
                  .size(42.dp)
                  .clip(CircleShape)
                  .background(Color.White)
                  .border(2.dp, DuoAmber, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(text = equippedHat.emojiIcon, fontSize = 22.sp)
              }
            }

            if (equippedAccessory != null) {
              Box(
                modifier = Modifier
                  .align(Alignment.BottomStart)
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(Color.White)
                  .border(2.dp, DuoBlue, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(text = equippedAccessory.emojiIcon, fontSize = 18.sp)
              }
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = petCustomization.petName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black
          )

          Spacer(modifier = Modifier.height(4.dp))

          Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = DuoAmber.copy(alpha = 0.2f)
            ) {
              Text(
                text = "${equippedHat?.emojiIcon ?: "🎓"} ${equippedHat?.name ?: "Sem Chapéu"}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DuoAmberDark,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = DuoGreen.copy(alpha = 0.2f)
            ) {
              Text(
                text = "${equippedOutfit?.emojiIcon ?: "👔"} ${equippedOutfit?.name ?: "Uniforme"}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = DuoGreenDark,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }
        }
      }
    }

    // Wardrobe Category Tabs
    item {
      TabRow(
        selectedTabIndex = selectedCategoryTab,
        containerColor = Color.Transparent,
        contentColor = DuoAmber
      ) {
        Tab(
          selected = selectedCategoryTab == 0,
          onClick = { selectedCategoryTab = 0 },
          text = { Text("🎩 Chapéus", fontWeight = FontWeight.Bold) }
        )
        Tab(
          selected = selectedCategoryTab == 1,
          onClick = { selectedCategoryTab = 1 },
          text = { Text("👕 Trajes", fontWeight = FontWeight.Bold) }
        )
        Tab(
          selected = selectedCategoryTab == 2,
          onClick = { selectedCategoryTab = 2 },
          text = { Text("👓 Acessórios", fontWeight = FontWeight.Bold) }
        )
      }
    }

    // Wardrobe Item Grid / Row of items
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        currentItems.forEach { item ->
          val isUnlocked = petCustomization.unlockedItemIds.contains(item.id)
          val isEquipped = when (item.category) {
            "hat" -> petCustomization.equippedHatId == item.id
            "outfit" -> petCustomization.equippedOutfitId == item.id
            else -> petCustomization.equippedAccessoryId == item.id
          }

          Surface(
            modifier = Modifier
              .weight(1f)
              .clip(RoundedCornerShape(16.dp))
              .clickable {
                if (isUnlocked) {
                  viewModel.equipPetItem(item.category, item.id)
                }
              }
              .testTag("pet_item_${item.id}"),
            shape = RoundedCornerShape(16.dp),
            color = when {
              isEquipped -> DuoAmber.copy(alpha = 0.2f)
              isUnlocked -> MaterialTheme.colorScheme.surface
              else -> Color(0xFFEBEBEB)
            },
            border = BorderStroke(
              2.dp,
              if (isEquipped) DuoAmber else Color(0xFFE2E8F0)
            )
          ) {
            Column(
              modifier = Modifier.padding(8.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Box(
                modifier = Modifier
                  .size(44.dp)
                  .clip(CircleShape)
                  .background(if (isUnlocked) Color.White else Color(0xFFDDDDDD)),
                contentAlignment = Alignment.Center
              ) {
                if (isUnlocked) {
                  Text(text = item.emojiIcon, fontSize = 24.sp)
                } else {
                  Icon(Icons.Default.Lock, contentDescription = "Bloqueado", tint = Color(0xFF888888), modifier = Modifier.size(20.dp))
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = item.name.split(" ").first(),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
              )

              if (isEquipped) {
                Text(
                  text = "Equipado",
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black,
                  color = DuoAmber
                )
              } else if (!isUnlocked) {
                Text(
                  text = "Bloqueado",
                  fontSize = 9.sp,
                  color = Color(0xFF888888)
                )
              }
            }
          }
        }
      }
    }

    // ALTERNATIVE MISSIONS HEADER
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(Icons.Default.Redeem, contentDescription = null, tint = DuoPurple)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
          Text(
            text = "Missões Alternativas para Roupas & Chapéus",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black
          )
          Text(
            text = "Cumpra os desafios para destravar itens exclusivos para sua Raposinha!",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Alternative Quests List
    items(alternativeQuests, key = { it.id }) { quest ->
      QuestItemCard(
        quest = quest,
        onClaim = { viewModel.claimQuestReward(quest.id) }
      )
    }

    // CUSTOMIZE BACKGROUND AND FONT SIZE SECTION
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(2.dp, Color(0xFFE2E8F0))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Palette, contentDescription = null, tint = DuoAmber)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Personalizar Cor do Fundo & Letras",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Black
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "Cor de Fundo da Interface:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            BackgroundStyle.values().forEach { style ->
              val isCurrent = backgroundStyle == style
              Surface(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { viewModel.setBackgroundStyle(style) }
                  .testTag("closet_bg_${style.name}"),
                color = Color(style.hexBackground),
                border = BorderStroke(2.dp, if (isCurrent) DuoAmber else Color(0xFFCCCCCC)),
                shape = RoundedCornerShape(12.dp)
              ) {
                Column(
                  modifier = Modifier.padding(vertical = 8.dp),
                  horizontalAlignment = Alignment.CenterHorizontally
                ) {
                  Text(text = style.iconEmoji, fontSize = 16.sp)
                  Text(
                    text = style.label.split(" ").first(),
                    fontSize = 10.sp,
                    fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Normal,
                    color = Color(style.hexText)
                  )
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.TextFields, contentDescription = null, tint = DuoBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Tamanho das Letras:",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold
            )
          }
          Spacer(modifier = Modifier.height(6.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FontScaleOption.values().forEach { fontOpt ->
              val isCurrent = fontScale == fontOpt
              Surface(
                modifier = Modifier
                  .weight(1f)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { viewModel.setFontScale(fontOpt) }
                  .testTag("closet_font_${fontOpt.name}"),
                color = if (isCurrent) DuoBlue.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(2.dp, if (isCurrent) DuoBlue else Color(0xFFDDDDDD)),
                shape = RoundedCornerShape(12.dp)
              ) {
                Box(
                  modifier = Modifier.padding(vertical = 10.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = fontOpt.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (isCurrent) DuoBlueDark else MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
fun QuestItemCard(
  quest: AlternativeQuest,
  onClaim: () -> Unit
) {
  val progress = (quest.currentCount.toFloat() / quest.targetCount.toFloat()).coerceIn(0f, 1f)
  val canClaim = quest.isCompleted && !quest.isClaimed

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = BorderStroke(2.dp, Color(0xFFE2E8F0))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(46.dp)
          .clip(CircleShape)
          .background(Color(0xFFF7F7F7))
          .border(2.dp, DuoPurple.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(text = quest.rewardItem.emojiIcon, fontSize = 24.sp)
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = quest.title,
          fontWeight = FontWeight.Black,
          fontSize = 15.sp
        )
        Text(
          text = quest.description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        LinearProgressIndicator(
          progress = { progress },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = DuoPurple,
          trackColor = Color(0xFFE5E5E5)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          text = "${quest.currentCount}/${quest.targetCount}",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = DuoPurple
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      if (quest.isClaimed) {
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = DuoGreen.copy(alpha = 0.2f)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Default.Check, contentDescription = null, tint = DuoGreenDark, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Resgatado", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DuoGreenDark)
          }
        }
      } else {
        Duo3DButton(
          onClick = onClaim,
          enabled = canClaim,
          faceColor = if (canClaim) DuoGreen else Color(0xFFE0E0E0),
          shadowColor = if (canClaim) DuoGreenDark else Color(0xFFCCCCCC),
          height = 38.dp,
          modifier = Modifier.width(96.dp)
        ) {
          Text(
            text = if (canClaim) "RESGATAR" else "EM CURSO",
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            color = if (canClaim) Color.White else Color(0xFF888888)
          )
        }
      }
    }
  }
}
