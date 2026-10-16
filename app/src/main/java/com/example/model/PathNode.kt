package com.example.model

enum class PathNodeType {
  LESSON,
  CHEST,
  BOSS_CHALLENGE
}

data class PathNode(
  val id: String,
  val stepNumber: Int,
  val subject: StudySubject,
  val nodeType: PathNodeType = PathNodeType.LESSON,
  val horizontalOffsetPercent: Float = 0f, // -0.5f to 0.5f for zigzag curve
  val isCompleted: Boolean = false,
  val isCurrent: Boolean = false,
  val isLocked: Boolean = false,
  val stars: Int = 0, // 0 to 3
  val xpReward: Int = 50
)
