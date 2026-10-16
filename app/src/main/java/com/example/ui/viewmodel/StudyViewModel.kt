package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.PetCatalog
import com.example.data.repository.StudyRepository
import com.example.model.AlternativeQuest
import com.example.model.BackgroundStyle
import com.example.model.EducationLevel
import com.example.model.FontScaleOption
import com.example.model.PathNode
import com.example.model.PathNodeType
import com.example.model.PetCustomization
import com.example.model.QuizQuestion
import com.example.model.RotationStrategy
import com.example.model.StudyReminderConfig
import com.example.model.StudySessionRecord
import com.example.model.StudySubject
import com.example.util.AlarmScheduler
import com.example.util.GameSoundHelper
import com.example.util.NotificationHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudyViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = StudyRepository(application)

  val allSubjects: StateFlow<List<StudySubject>> = repository.subjectsFlow.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val completedSessions: StateFlow<List<StudySessionRecord>> = repository.sessionsFlow.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    emptyList()
  )

  val reminderConfig: StateFlow<StudyReminderConfig> = repository.reminderConfigFlow.stateIn(
    viewModelScope,
    SharingStarted.WhileSubscribed(5000),
    StudyReminderConfig()
  )

  // Onboarding & Level focus state
  private val _hasCompletedOnboarding = MutableStateFlow(false)
  val hasCompletedOnboarding: StateFlow<Boolean> = _hasCompletedOnboarding.asStateFlow()

  private val _userLevelFocus = MutableStateFlow("HYBRID")
  val userLevelFocus: StateFlow<String> = _userLevelFocus.asStateFlow()

  // Appearance & Accessibility customisation
  private val _backgroundStyle = MutableStateFlow(BackgroundStyle.LIGHT_PAPER)
  val backgroundStyle: StateFlow<BackgroundStyle> = _backgroundStyle.asStateFlow()

  private val _fontScale = MutableStateFlow(FontScaleOption.NORMAL)
  val fontScale: StateFlow<FontScaleOption> = _fontScale.asStateFlow()

  // Pet Fox Customization & Alternative Quests
  private val _petCustomization = MutableStateFlow(PetCustomization())
  val petCustomization: StateFlow<PetCustomization> = _petCustomization.asStateFlow()

  private val _alternativeQuests = MutableStateFlow<List<AlternativeQuest>>(PetCatalog.getInitialQuests())
  val alternativeQuests: StateFlow<List<AlternativeQuest>> = _alternativeQuests.asStateFlow()

  // Gamification Stats
  private val _xp = MutableStateFlow(120)
  val xp: StateFlow<Int> = _xp.asStateFlow()

  private val _gems = MutableStateFlow(150)
  val gems: StateFlow<Int> = _gems.asStateFlow()

  private val _hearts = MutableStateFlow(5)
  val hearts: StateFlow<Int> = _hearts.asStateFlow()

  private val _league = MutableStateFlow("Bronze")
  val league: StateFlow<String> = _league.asStateFlow()

  private val _streakDays = MutableStateFlow(1)
  val streakDays: StateFlow<Int> = _streakDays.asStateFlow()

  // Path Nodes
  private val _pathNodes = MutableStateFlow<List<PathNode>>(emptyList())
  val pathNodes: StateFlow<List<PathNode>> = _pathNodes.asStateFlow()

  private val _activeMissionNode = MutableStateFlow<PathNode?>(null)
  val activeMissionNode: StateFlow<PathNode?> = _activeMissionNode.asStateFlow()

  // Subjects in focus
  private val _currentSubject = MutableStateFlow<StudySubject?>(null)
  val currentSubject: StateFlow<StudySubject?> = _currentSubject.asStateFlow()

  private val _nextSubject = MutableStateFlow<StudySubject?>(null)
  val nextSubject: StateFlow<StudySubject?> = _nextSubject.asStateFlow()

  // Timer states
  private val _isTimerRunning = MutableStateFlow(false)
  val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

  private val _isBreakMode = MutableStateFlow(false)
  val isBreakMode: StateFlow<Boolean> = _isBreakMode.asStateFlow()

  private val _currentFocusTargetMinutes = MutableStateFlow(25)
  val currentFocusTargetMinutes: StateFlow<Int> = _currentFocusTargetMinutes.asStateFlow()

  private val _timerSecondsRemaining = MutableStateFlow(25 * 60)
  val timerSecondsRemaining: StateFlow<Int> = _timerSecondsRemaining.asStateFlow()

  private val _ambientSoundPlaying = MutableStateFlow(false)
  val ambientSoundPlaying: StateFlow<Boolean> = _ambientSoundPlaying.asStateFlow()

  // Dialogs and sheets
  private val _quizSubject = MutableStateFlow<StudySubject?>(null)
  val quizSubject: StateFlow<StudySubject?> = _quizSubject.asStateFlow()

  private val _selectedQuizQuestion = MutableStateFlow<QuizQuestion?>(null)
  val selectedQuizQuestion: StateFlow<QuizQuestion?> = _selectedQuizQuestion.asStateFlow()

  private val _sessionCompletedJustNow = MutableStateFlow<StudySessionRecord?>(null)
  val sessionCompletedJustNow: StateFlow<StudySessionRecord?> = _sessionCompletedJustNow.asStateFlow()

  private val _showAddSubjectDialog = MutableStateFlow(false)
  val showAddSubjectDialog: StateFlow<Boolean> = _showAddSubjectDialog.asStateFlow()

  private val _selectedSubjectForDetail = MutableStateFlow<StudySubject?>(null)
  val selectedSubjectForDetail: StateFlow<StudySubject?> = _selectedSubjectForDetail.asStateFlow()

  private var timerJob: Job? = null
  private var fundamentalIndex = 0
  private var medioIndex = 0

  init {
    viewModelScope.launch {
      repository.ensureSeeded()
    }

    viewModelScope.launch {
      allSubjects.collect { subjects ->
        if (subjects.isNotEmpty()) {
          if (_currentSubject.value == null) {
            computeInitialSubjects(subjects)
          }
          generatePathNodes(subjects)
        }
      }
    }

    viewModelScope.launch {
      completedSessions.collect { sessions ->
        if (sessions.isNotEmpty()) {
          val uniqueDays = sessions.map {
            it.completedAtFormatted.split(" ").firstOrNull() ?: ""
          }.filter { it.isNotEmpty() }.toSet().size
          _streakDays.value = uniqueDays.coerceAtLeast(1)
        }
      }
    }
  }

  fun completeOnboarding(
    levelChoice: String,
    petName: String,
    bgStyle: BackgroundStyle,
    fontScale: FontScaleOption
  ) {
    _userLevelFocus.value = levelChoice
    _petCustomization.value = _petCustomization.value.copy(petName = petName)
    _backgroundStyle.value = bgStyle
    _fontScale.value = fontScale
    _hasCompletedOnboarding.value = true

    // Recompute path based on user choice
    allSubjects.value.let { subjects ->
      if (subjects.isNotEmpty()) {
        computeInitialSubjects(subjects)
        generatePathNodes(subjects)
      }
    }
  }

  fun setBackgroundStyle(style: BackgroundStyle) {
    _backgroundStyle.value = style
  }

  fun setFontScale(scale: FontScaleOption) {
    _fontScale.value = scale
  }

  fun equipPetItem(category: String, itemId: String) {
    val current = _petCustomization.value
    _petCustomization.value = when (category) {
      "hat" -> current.copy(equippedHatId = itemId)
      "outfit" -> current.copy(equippedOutfitId = itemId)
      else -> current.copy(equippedAccessoryId = itemId)
    }
    GameSoundHelper.playClick()
  }

  fun claimQuestReward(questId: String) {
    val quests = _alternativeQuests.value.toMutableList()
    val index = quests.indexOfFirst { it.id == questId }
    if (index != -1) {
      val quest = quests[index]
      if (quest.isCompleted && !quest.isClaimed) {
        quests[index] = quest.copy(isClaimed = true)
        _alternativeQuests.value = quests

        // Unlock item in pet closet!
        val currentUnlocked = _petCustomization.value.unlockedItemIds.toMutableSet()
        currentUnlocked.add(quest.rewardItem.id)
        _petCustomization.value = _petCustomization.value.copy(unlockedItemIds = currentUnlocked)

        _xp.value += 30
        _gems.value += 10
        GameSoundHelper.playLevelUpFanfare()
      }
    }
  }

  private fun updateQuestProgress(actionType: String, subject: StudySubject? = null, minutes: Int = 0) {
    val quests = _alternativeQuests.value.map { quest ->
      var newCount = quest.currentCount
      when (quest.id) {
        "quest_bone" -> if (actionType == "FOCUS_DONE") newCount++
        "quest_fones" -> if (actionType == "FOCUS_DONE" && minutes >= 45) newCount++
        "quest_jaleco" -> if (actionType == "FOCUS_DONE" && (subject?.category == "Exatas" || subject?.category == "Biológicas")) newCount++
        "quest_kimono" -> if (actionType == "BREAK_DONE") newCount++
        "quest_oculos_sol" -> if (actionType == "FOCUS_DONE") newCount++
        "quest_mago" -> if (actionType == "QUIZ_CORRECT") newCount++
        "quest_coroa" -> newCount = _streakDays.value
      }
      val isNowCompleted = newCount >= quest.targetCount
      quest.copy(
        currentCount = newCount.coerceAtMost(quest.targetCount),
        isCompleted = isNowCompleted
      )
    }
    _alternativeQuests.value = quests
  }

  private fun computeInitialSubjects(subjects: List<StudySubject>) {
    val activeFundamental = subjects.filter { it.isEnabled && it.educationLevel == EducationLevel.FUNDAMENTAL }
    val activeMedio = subjects.filter { it.isEnabled && it.educationLevel == EducationLevel.MEDIO }

    val initialCurr = when (_userLevelFocus.value) {
      "MEDIO" -> activeMedio.firstOrNull() ?: subjects.firstOrNull()
      else -> activeFundamental.firstOrNull() ?: subjects.firstOrNull()
    }
    val initialNext = when (_userLevelFocus.value) {
      "MEDIO" -> activeFundamental.firstOrNull() ?: subjects.getOrNull(1)
      else -> activeMedio.firstOrNull() ?: subjects.getOrNull(1)
    }

    _currentSubject.value = initialCurr
    _nextSubject.value = initialNext
  }

  private fun generatePathNodes(subjects: List<StudySubject>) {
    val fundSubjects = subjects.filter { it.educationLevel == EducationLevel.FUNDAMENTAL }
    val medSubjects = subjects.filter { it.educationLevel == EducationLevel.MEDIO }

    if (fundSubjects.isEmpty() || medSubjects.isEmpty()) return

    val offsetPattern = listOf(0f, 0.45f, 0.7f, 0.35f, 0f, -0.35f, -0.7f, -0.45f)
    val nodes = mutableListOf<PathNode>()
    val count = maxOf(fundSubjects.size, medSubjects.size) * 2

    for (i in 0 until count) {
      val isFund = if (_userLevelFocus.value == "MEDIO") (i % 2 == 1) else (i % 2 == 0)
      val subject = if (isFund) {
        fundSubjects[(i / 2) % fundSubjects.size]
      } else {
        medSubjects[(i / 2) % medSubjects.size]
      }

      val isChest = (i + 1) % 5 == 0
      val nodeType = if (isChest) PathNodeType.CHEST else PathNodeType.LESSON
      val offset = offsetPattern[i % offsetPattern.size]

      val isCompleted = i == 0
      val isCurrent = i == 1
      val isLocked = i > 1

      nodes.add(
        PathNode(
          id = "path_node_${i}_${subject.id}",
          stepNumber = i + 1,
          subject = subject,
          nodeType = nodeType,
          horizontalOffsetPercent = offset,
          isCompleted = isCompleted,
          isCurrent = isCurrent,
          isLocked = isLocked,
          stars = if (isCompleted) 3 else 0,
          xpReward = if (nodeType == PathNodeType.CHEST) 100 else 50
        )
      )
    }

    _pathNodes.value = nodes
  }

  fun setFocusDuration(minutes: Int) {
    if (!_isTimerRunning.value) {
      _currentFocusTargetMinutes.value = minutes
      _isBreakMode.value = false
      _timerSecondsRemaining.value = minutes * 60
    }
  }

  fun setBreakDuration(minutes: Int) {
    if (!_isTimerRunning.value) {
      _isBreakMode.value = true
      _timerSecondsRemaining.value = minutes * 60
    }
  }

  fun startTimer() {
    if (_isTimerRunning.value) return
    _isTimerRunning.value = true
    GameSoundHelper.playClick()

    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (_isTimerRunning.value && _timerSecondsRemaining.value > 0) {
        delay(1000L)
        _timerSecondsRemaining.value -= 1
      }

      if (_timerSecondsRemaining.value <= 0) {
        onTimerCompleted()
      }
    }
  }

  fun pauseTimer() {
    _isTimerRunning.value = false
    timerJob?.cancel()
    GameSoundHelper.playClick()
  }

  fun resetTimer() {
    _isTimerRunning.value = false
    timerJob?.cancel()
    val duration = if (_isBreakMode.value) 5 else _currentFocusTargetMinutes.value
    _timerSecondsRemaining.value = duration * 60
    GameSoundHelper.playClick()
  }

  private fun onTimerCompleted() {
    _isTimerRunning.value = false
    timerJob?.cancel()

    if (!_isBreakMode.value) {
      val subject = _currentSubject.value
      val duration = _currentFocusTargetMinutes.value
      if (subject != null) {
        viewModelScope.launch {
          val recordId = repository.recordCompletedSession(
            subject = subject,
            durationMinutes = duration,
            notes = "Missão de foco concluída!"
          )
          val session = StudySessionRecord(
            id = recordId,
            subjectId = subject.id,
            subjectName = subject.name,
            educationLevel = subject.educationLevel,
            durationMinutes = duration,
            timestampEpochMillis = System.currentTimeMillis(),
            completedAtFormatted = "Agora",
            notes = "Missão de foco"
          )
          _sessionCompletedJustNow.value = session

          _xp.value += 50
          _gems.value += 15
          GameSoundHelper.playLevelUpFanfare()

          updateQuestProgress("FOCUS_DONE", subject = subject, minutes = duration)
          advancePathProgress()

          NotificationHelper.showSessionFinished(
            context = getApplication(),
            subjectName = subject.name,
            minutes = duration
          )
        }
      }
      _isBreakMode.value = true
      _timerSecondsRemaining.value = 5 * 60
    } else {
      updateQuestProgress("BREAK_DONE")
      _isBreakMode.value = false
      advanceToNextSubject()
      _timerSecondsRemaining.value = _currentFocusTargetMinutes.value * 60
    }
  }

  private fun advancePathProgress() {
    val currentNodes = _pathNodes.value.toMutableList()
    val currentIndex = currentNodes.indexOfFirst { it.isCurrent }
    if (currentIndex != -1) {
      currentNodes[currentIndex] = currentNodes[currentIndex].copy(
        isCompleted = true,
        isCurrent = false,
        stars = 3
      )
      val nextIndex = currentIndex + 1
      if (nextIndex < currentNodes.size) {
        currentNodes[nextIndex] = currentNodes[nextIndex].copy(
          isLocked = false,
          isCurrent = true
        )
      }
      _pathNodes.value = currentNodes
    }
  }

  fun advanceToNextSubject() {
    val subjects = allSubjects.value
    if (subjects.isEmpty()) return

    val current = _currentSubject.value
    val next = _nextSubject.value
    val activeFundamental = subjects.filter { it.isEnabled && it.educationLevel == EducationLevel.FUNDAMENTAL }
    val activeMedio = subjects.filter { it.isEnabled && it.educationLevel == EducationLevel.MEDIO }

    val newCurrent = next ?: run {
      if (current?.educationLevel == EducationLevel.FUNDAMENTAL) {
        activeMedio.firstOrNull() ?: subjects.first()
      } else {
        activeFundamental.firstOrNull() ?: subjects.first()
      }
    }
    _currentSubject.value = newCurrent

    val newNext = if (newCurrent.educationLevel == EducationLevel.FUNDAMENTAL) {
      if (activeMedio.isNotEmpty()) {
        medioIndex = (medioIndex + 1) % activeMedio.size
        activeMedio[medioIndex]
      } else {
        subjects.firstOrNull { it.id != newCurrent.id }
      }
    } else {
      if (activeFundamental.isNotEmpty()) {
        fundamentalIndex = (fundamentalIndex + 1) % activeFundamental.size
        activeFundamental[fundamentalIndex]
      } else {
        subjects.firstOrNull { it.id != newCurrent.id }
      }
    }

    _nextSubject.value = newNext
  }

  fun selectSubjectDirectly(subject: StudySubject) {
    _currentSubject.value = subject
    val subjects = allSubjects.value
    val activeFundamental = subjects.filter { it.isEnabled && it.educationLevel == EducationLevel.FUNDAMENTAL }
    val activeMedio = subjects.filter { it.isEnabled && it.educationLevel == EducationLevel.MEDIO }

    _nextSubject.value = if (subject.educationLevel == EducationLevel.FUNDAMENTAL) {
      activeMedio.firstOrNull()
    } else {
      activeFundamental.firstOrNull()
    }
  }

  fun toggleSubjectEnabled(id: String, enabled: Boolean) {
    viewModelScope.launch {
      repository.toggleSubject(id, enabled)
    }
  }

  fun toggleAmbientSound() {
    _ambientSoundPlaying.value = !_ambientSoundPlaying.value
  }

  fun openMissionNode(node: PathNode) {
    _activeMissionNode.value = node
    GameSoundHelper.playClick()
  }

  fun dismissMissionNode() {
    _activeMissionNode.value = null
  }

  fun startMissionFromNode(node: PathNode) {
    selectSubjectDirectly(node.subject)
    dismissMissionNode()
  }

  fun openQuizForCurrentSubject() {
    val subject = _currentSubject.value ?: return
    _quizSubject.value = subject
    _selectedQuizQuestion.value = subject.questions.randomOrNull()
  }

  fun openQuizForSubject(subject: StudySubject) {
    _quizSubject.value = subject
    _selectedQuizQuestion.value = subject.questions.randomOrNull()
  }

  fun answerQuizCorrect() {
    _xp.value += 15
    _gems.value += 5
    updateQuestProgress("QUIZ_CORRECT")
  }

  fun dismissQuizDialog() {
    _quizSubject.value = null
    _selectedQuizQuestion.value = null
  }

  fun dismissSessionCompletedDialog() {
    _sessionCompletedJustNow.value = null
  }

  fun setSubjectForDetail(subject: StudySubject?) {
    _selectedSubjectForDetail.value = subject
  }

  fun setAddSubjectDialogVisible(visible: Boolean) {
    _showAddSubjectDialog.value = visible
  }

  fun addCustomSubject(
    name: String,
    level: EducationLevel,
    category: String,
    tip: String,
    topicsText: String
  ) {
    viewModelScope.launch {
      val topics = topicsText.split("\n")
        .map { it.trim().removePrefix("-").removePrefix("•").trim() }
        .filter { it.isNotEmpty() }

      val colorHex = if (level == EducationLevel.FUNDAMENTAL) "#58CC02" else "#FF9600"
      val newSubject = StudySubject(
        id = "custom_${System.currentTimeMillis()}",
        name = name.trim(),
        educationLevel = level,
        category = category.trim().ifEmpty { "Geral" },
        colorHex = colorHex,
        iconKey = "school",
        shortTip = tip.trim().ifEmpty { "Revise os pontos centrais e pratique exercícios." },
        topics = if (topics.isEmpty()) listOf("Conceitos Iniciais", "Prática e Exercícios") else topics,
        questions = emptyList(),
        isEnabled = true,
        priorityWeight = 2
      )
      repository.saveSubject(newSubject)
      _showAddSubjectDialog.value = false
    }
  }

  fun deleteSubject(id: String) {
    viewModelScope.launch {
      repository.deleteSubject(id)
    }
  }

  fun updateReminderConfig(config: StudyReminderConfig) {
    viewModelScope.launch {
      repository.saveReminderConfig(config)
      AlarmScheduler.scheduleDailyReminder(getApplication(), config)
    }
  }

  fun triggerTestNotification() {
    val curr = _currentSubject.value
    NotificationHelper.showStudyReminder(
      context = getApplication(),
      subjectName = curr?.name ?: "Matemática Básica",
      levelName = curr?.educationLevel?.label ?: "Ensino Fundamental"
    )
  }

  fun clearHistory() {
    viewModelScope.launch {
      repository.clearHistory()
    }
  }
}
