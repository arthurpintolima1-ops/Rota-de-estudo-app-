package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.DuoBlue
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoPurple
import com.example.ui.theme.DuoRed

@Composable
fun DuoTopBar(
  streakDays: Int,
  gems: Int,
  hearts: Int,
  xp: Int,
  league: String,
  onOpenProfile: () -> Unit = {}
) {
  Surface(
    modifier = Modifier
      .fillMaxWidth()
      .testTag("duo_top_bar"),
    color = MaterialTheme.colorScheme.surface,
    shadowElevation = 2.dp
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Streak (Flame)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .padding(horizontal = 4.dp, vertical = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Default.LocalFireDepartment,
          contentDescription = "Ofensiva",
          tint = DuoAmber,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = "$streakDays",
          fontWeight = FontWeight.Black,
          fontSize = 15.sp,
          color = DuoAmber
        )
      }

      // Gemas (Diamonds)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .padding(horizontal = 4.dp, vertical = 2.dp)
      ) {
        Text(
          text = "💎",
          fontSize = 16.sp
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = "$gems",
          fontWeight = FontWeight.Black,
          fontSize = 15.sp,
          color = DuoBlue
        )
      }

      // Hearts (Vidas)
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .padding(horizontal = 4.dp, vertical = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Favorite,
          contentDescription = "Vidas",
          tint = DuoRed,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
          text = "$hearts",
          fontWeight = FontWeight.Black,
          fontSize = 15.sp,
          color = DuoRed
        )
      }

      // League / XP
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .clickable { onOpenProfile() }
          .padding(horizontal = 4.dp, vertical = 2.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Bolt,
          contentDescription = "XP",
          tint = DuoPurple,
          modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
          text = "${xp}XP",
          fontWeight = FontWeight.Black,
          fontSize = 14.sp,
          color = DuoPurple
        )
      }
    }
  }
}
