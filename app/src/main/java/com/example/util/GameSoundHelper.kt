package com.example.util

import android.media.AudioManager
import android.media.ToneGenerator

object GameSoundHelper {
  private var toneGen: ToneGenerator? = null

  init {
    try {
      toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
    } catch (_: Exception) {}
  }

  fun playSuccessChime() {
    try {
      toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 180)
    } catch (_: Exception) {}
  }

  fun playLevelUpFanfare() {
    try {
      toneGen?.startTone(ToneGenerator.TONE_CDMA_ALERT_NETWORK_LITE, 350)
    } catch (_: Exception) {}
  }

  fun playWrongBeep() {
    try {
      toneGen?.startTone(ToneGenerator.TONE_PROP_NACK, 200)
    } catch (_: Exception) {}
  }

  fun playClick() {
    try {
      toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 40)
    } catch (_: Exception) {}
  }
}
