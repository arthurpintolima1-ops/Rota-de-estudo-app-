package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = DuoGreen,
  onPrimary = Color.White,
  primaryContainer = DuoGreenDark,
  onPrimaryContainer = Color.White,
  secondary = DuoBlue,
  onSecondary = Color.White,
  secondaryContainer = DuoBlueDark,
  onSecondaryContainer = Color.White,
  tertiary = DuoAmber,
  onTertiary = Color.White,
  background = DuoBackgroundDark,
  surface = DuoSurfaceDark,
  onBackground = DuoTextLight,
  onSurface = DuoTextLight,
  surfaceVariant = DuoBorderDark,
  onSurfaceVariant = DuoTextDarkMuted,
  outline = DuoBorderDark
)

private val LightColorScheme = lightColorScheme(
  primary = DuoGreen,
  onPrimary = Color.White,
  primaryContainer = DuoGreenContainer,
  onPrimaryContainer = DuoGreenDark,
  secondary = DuoBlue,
  onSecondary = Color.White,
  secondaryContainer = DuoBlueContainer,
  onSecondaryContainer = DuoBlueDark,
  tertiary = DuoAmber,
  onTertiary = Color.White,
  background = DuoBackgroundLight,
  surface = DuoSurfaceLight,
  onBackground = DuoTextDark,
  onSurface = DuoTextDark,
  surfaceVariant = DuoBorderLight,
  onSurfaceVariant = DuoTextMuted,
  outline = DuoBorderLight
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep intentional game theme
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
