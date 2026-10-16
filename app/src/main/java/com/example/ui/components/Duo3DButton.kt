package com.example.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DuoGreen
import com.example.ui.theme.DuoGreenDark

@Composable
fun Duo3DButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  faceColor: Color = DuoGreen,
  shadowColor: Color = DuoGreenDark,
  contentColor: Color = Color.White,
  cornerRadius: Dp = 16.dp,
  height: Dp = 54.dp,
  testTag: String = "duo_button",
  content: @Composable RowScope.() -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val shadowDepth = 4.dp
  val topOffset by animateDpAsState(
    targetValue = if (isPressed && enabled) shadowDepth else 0.dp,
    label = "btn_press"
  )

  Box(
    modifier = modifier
      .height(height + shadowDepth)
      .testTag(testTag)
  ) {
    // 3D Shadow layer (bottom face)
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .offset(y = shadowDepth)
        .clip(RoundedCornerShape(cornerRadius))
        .background(if (enabled) shadowColor else Color(0xFFCCCCCC))
    )

    // Interactive Top face layer
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(height)
        .offset(y = topOffset)
        .clip(RoundedCornerShape(cornerRadius))
        .background(if (enabled) faceColor else Color(0xFFE5E5E5))
        .clickable(
          enabled = enabled,
          interactionSource = interactionSource,
          indication = null,
          onClick = onClick
        ),
      contentAlignment = Alignment.Center
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        content = content
      )
    }
  }
}
