package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.DuoAmber

@Composable
fun MascotSpeechBubble(
  message: String,
  modifier: Modifier = Modifier,
  mascotName: String = "Foxy",
  equippedHatEmoji: String = ""
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 4.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Fox Avatar with warm amber frame
    Box(
      modifier = Modifier
        .size(68.dp)
        .clip(CircleShape)
        .background(DuoAmber)
        .border(3.dp, Color.White, CircleShape),
      contentAlignment = Alignment.Center
    ) {
      Image(
        painter = painterResource(id = R.drawable.study_fox_mascot_1792140274353),
        contentDescription = mascotName,
        modifier = Modifier.fillMaxWidth(),
        contentScale = ContentScale.Crop
      )
      if (equippedHatEmoji.isNotEmpty()) {
        Text(
          text = equippedHatEmoji,
          fontSize = 18.sp,
          modifier = Modifier.align(Alignment.TopEnd)
        )
      }
    }

    Spacer(modifier = Modifier.width(10.dp))

    // Speech bubble with pet name header
    Box(
      modifier = Modifier
        .weight(1f)
        .clip(RoundedCornerShape(18.dp))
        .background(MaterialTheme.colorScheme.surface)
        .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
        .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "🦊 $mascotName diz:",
            fontWeight = FontWeight.Black,
            fontSize = 11.sp,
            color = DuoAmber
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = message,
          style = MaterialTheme.typography.bodyMedium,
          fontWeight = FontWeight.SemiBold,
          lineHeight = 19.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }
  }
}
