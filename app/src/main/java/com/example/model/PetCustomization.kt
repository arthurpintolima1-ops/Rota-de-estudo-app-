package com.example.model

enum class BackgroundStyle(
  val label: String,
  val iconEmoji: String,
  val hexBackground: Long,
  val hexSurface: Long,
  val hexText: Long
) {
  LIGHT_PAPER("Papel Claro", "⚪", 0xFFFAFAFA, 0xFFFFFFFF, 0xFF1E293B),
  DARK_NIGHT("Escuro Noturno", "🌙", 0xFF0F172A, 0xFF1E293B, 0xFFF8FAFC),
  WARM_SEPIA("Sépia Aconchegante", "☕", 0xFFF7F2E7, 0xFFFFFDF9, 0xFF3E2723),
  FOREST_MINT("Floresta Menta", "🌲", 0xFFEAF5EF, 0xFFF5FAF7, 0xFF14452F),
  COSMIC_PURPLE("Roxo Cósmico", "🌌", 0xFF1A102F, 0xFF281A45, 0xFFF3E8FF)
}

enum class FontScaleOption(val label: String, val multiplier: Float) {
  NORMAL("Normal (Padrão)", 1.0f),
  MEDIUM("Médio (+15%)", 1.15f),
  LARGE("Grande (+30%)", 1.30f)
}

data class PetCustomization(
  val petName: String = "Foxy",
  val equippedHatId: String = "capelo", // "none", "capelo", "bone", "coroa", "fones", "mago"
  val equippedOutfitId: String = "uniforme", // "uniforme", "kimono", "jaleco", "gala"
  val equippedAccessoryId: String = "oculos", // "none", "oculos", "oculos_sol", "folha"
  val unlockedItemIds: Set<String> = setOf("uniforme", "capelo", "oculos")
)

data class PetItem(
  val id: String,
  val name: String,
  val category: String, // "hat", "outfit", "accessory"
  val emojiIcon: String,
  val description: String,
  val unlockRequirement: String
)

data class AlternativeQuest(
  val id: String,
  val title: String,
  val description: String,
  val targetCount: Int,
  val currentCount: Int,
  val rewardItem: PetItem,
  val isCompleted: Boolean = false,
  val isClaimed: Boolean = false
)
