package com.example.model

enum class RotationStrategy(val title: String, val description: String) {
  ALTERNATING_LEVELS(
    "Alternância Fundamental ↔ Médio",
    "Garante variação contínua: estuda uma matéria do Fundamental, depois uma do Médio e repete."
  ),
  SEQUENTIAL(
    "Ciclo Sequencial",
    "Percorre a fila de matérias ativas uma após a outra."
  ),
  RANDOM_BALANCED(
    "Aleatório Balanceado",
    "Sorteia a próxima matéria alternando entre níveis para evitar monotonia."
  )
}

data class StudyReminderConfig(
  val id: Int = 1,
  val isReminderEnabled: Boolean = true,
  val reminderHour: Int = 16,
  val reminderMinute: Int = 0,
  val daysOfWeek: Set<Int> = setOf(1, 2, 3, 4, 5), // Mon-Fri (1=Mon, 7=Sun or Calendar.MONDAY..)
  val focusDurationMinutes: Int = 25,
  val shortBreakMinutes: Int = 5,
  val rotationStrategy: RotationStrategy = RotationStrategy.ALTERNATING_LEVELS,
  val soundEnabled: Boolean = true,
  val vibrateEnabled: Boolean = true
)
