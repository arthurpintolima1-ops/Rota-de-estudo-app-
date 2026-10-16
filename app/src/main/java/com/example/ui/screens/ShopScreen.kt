package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.School
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.Duo3DButton
import com.example.ui.components.MascotSpeechBubble
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoBlueDark
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark
import com.example.ui.theme.DuoPurple
import com.example.ui.theme.DuoRed
import com.example.ui.viewmodel.StudyViewModel

@Composable
fun ShopScreen(viewModel: StudyViewModel) {
  val gems by viewModel.gems.collectAsState()
  val xp by viewModel.xp.collectAsState()
  val league by viewModel.league.collectAsState()

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("shop_screen_list"),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(6.dp))
      MascotSpeechBubble(
        message = "Gaste suas gemas aqui para proteger sua ofensiva ou turbinar seus pontos de estudo!"
      )
    }

    // League Card (Duolingo League Leaderboard)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE5E5E5))
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(40.dp)
                  .clip(CircleShape)
                  .background(DuoAmber),
                contentAlignment = Alignment.Center
              ) {
                Icon(Icons.Default.MilitaryTech, contentDescription = null, tint = Color.White)
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "Liga Bronze",
                  fontWeight = FontWeight.Black,
                  fontSize = 17.sp
                )
                Text(
                  text = "Top 3 avançam para a Liga Prata!",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = DuoAmber.copy(alpha = 0.2f)
            ) {
              Text(
                text = "🏆 2º LUGAR",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                color = DuoAmber,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Mini leaderboard row
          listOf(
            Triple(1, "Lucas M.", "390 XP"),
            Triple(2, "Você (Estudante)", "${xp} XP"),
            Triple(3, "Beatriz S.", "260 XP"),
            Triple(4, "Carlos E.", "180 XP")
          ).forEach { (pos, name, score) ->
            val isYou = pos == 2
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (isYou) DuoGreen.copy(alpha = 0.15f) else Color.Transparent,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 3.dp)
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = "#$pos",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = if (pos <= 3) DuoAmber else MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Text(
                    text = name,
                    fontWeight = if (isYou) FontWeight.Black else FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (isYou) DuoGreenDark else MaterialTheme.colorScheme.onSurface
                  )
                }

                Text(
                  text = score,
                  fontWeight = FontWeight.Black,
                  fontSize = 13.sp,
                  color = DuoPurple
                )
              }
            }
          }
        }
      }
    }

    // Shop Items
    item {
      Text(
        text = "Loja de Recompensas",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Black,
        modifier = Modifier.padding(vertical = 4.dp)
      )
    }

    item {
      ShopItemCard(
        icon = "❄️",
        title = "Bloqueio de Ofensiva",
        description = "Protege sua sequência mesmo se você esquecer de estudar por um dia.",
        priceGems = 50,
        canAfford = gems >= 50,
        onBuy = {
          // Visual purchase
        }
      )
    }

    item {
      ShopItemCard(
        icon = "⚡",
        title = "Poção de XP Duplo (15 min)",
        description = "Receba o dobro de pontos em todas as sessões e desafios concluídos.",
        priceGems = 80,
        canAfford = gems >= 80,
        onBuy = {}
      )
    }

    item {
      ShopItemCard(
        icon = "💖",
        title = "Recarga Total de Vidas",
        description = "Restaure instantaneamente todos os seus corações para 5/5.",
        priceGems = 40,
        canAfford = gems >= 40,
        onBuy = {}
      )
    }

    item {
      ShopItemCard(
        icon = "🎓",
        title = "Gorro Dourado do Foquito",
        description = "Deixa o mascote de estudos com visual comemorativo dourado.",
        priceGems = 100,
        canAfford = gems >= 100,
        onBuy = {}
      )
    }

    item {
      Spacer(modifier = Modifier.height(80.dp))
    }
  }
}

@Composable
fun ShopItemCard(
  icon: String,
  title: String,
  description: String,
  priceGems: Int,
  canAfford: Boolean,
  onBuy: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(18.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE5E5E5))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(50.dp)
          .clip(CircleShape)
          .background(Color(0xFFF7F7F7))
          .border(2.dp, Color(0xFFE5E5E5), CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(text = icon, fontSize = 24.sp)
      }

      Spacer(modifier = Modifier.width(12.dp))

      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = title,
          fontWeight = FontWeight.Black,
          fontSize = 15.sp
        )
        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 16.sp
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      Duo3DButton(
        onClick = onBuy,
        faceColor = if (canAfford) DuoBlue else Color(0xFFCCCCCC),
        shadowColor = if (canAfford) DuoBlueDark else Color(0xFFAAAAAA),
        enabled = canAfford,
        height = 42.dp,
        modifier = Modifier.width(88.dp)
      ) {
        Text(
          text = "💎 $priceGems",
          fontWeight = FontWeight.Black,
          fontSize = 12.sp,
          color = Color.White
        )
      }
    }
  }
}
