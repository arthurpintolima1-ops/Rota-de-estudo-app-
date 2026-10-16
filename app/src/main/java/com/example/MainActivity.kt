package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.example.model.BackgroundStyle
import com.example.ui.components.DuoTopBar
import com.example.ui.dialogs.AddSubjectDialog
import com.example.ui.dialogs.MissionModal
import com.example.ui.dialogs.QuizDialog
import com.example.ui.dialogs.SessionCompletedDialog
import com.example.ui.dialogs.SubjectDetailSheet
import com.example.ui.screens.FocusScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PathScreen
import com.example.ui.screens.PetClosetScreen
import com.example.ui.screens.RemindersScreen
import com.example.ui.screens.RotationScreen
import com.example.ui.theme.DuoAmber
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.StudyViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: StudyViewModel by viewModels()

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val hasCompletedOnboarding by viewModel.hasCompletedOnboarding.collectAsState()
      val backgroundStyle by viewModel.backgroundStyle.collectAsState()
      val fontScale by viewModel.fontScale.collectAsState()

      val currentDensity = LocalDensity.current
      val customDensity = Density(
        density = currentDensity.density,
        fontScale = currentDensity.fontScale * fontScale.multiplier
      )

      CompositionLocalProvider(LocalDensity provides customDensity) {
        MyApplicationTheme {
          if (!hasCompletedOnboarding) {
            OnboardingScreen(
              onComplete = { levelChoice, petName, bgStyle, fontOpt ->
                viewModel.completeOnboarding(levelChoice, petName, bgStyle, fontOpt)
              }
            )
          } else {
            var currentTab by rememberSaveable { mutableIntStateOf(0) } // 0: Trilha, 1: Foco, 2: Pet Closet, 3: Matérias, 4: Lembretes

            BackHandler(enabled = currentTab != 0) {
              currentTab = 0
            }

            val streakDays by viewModel.streakDays.collectAsState()
            val gems by viewModel.gems.collectAsState()
            val hearts by viewModel.hearts.collectAsState()
            val xp by viewModel.xp.collectAsState()
            val league by viewModel.league.collectAsState()

            val activeMissionNode by viewModel.activeMissionNode.collectAsState()
            val nextSubject by viewModel.nextSubject.collectAsState()
            val quizSubject by viewModel.quizSubject.collectAsState()
            val selectedQuizQuestion by viewModel.selectedQuizQuestion.collectAsState()
            val showAddSubject by viewModel.showAddSubjectDialog.collectAsState()
            val detailSubject by viewModel.selectedSubjectForDetail.collectAsState()
            val sessionCompletedJustNow by viewModel.sessionCompletedJustNow.collectAsState()

            Scaffold(
              modifier = Modifier
                .fillMaxSize()
                .background(Color(backgroundStyle.hexBackground)),
              topBar = {
                DuoTopBar(
                  streakDays = streakDays,
                  gems = gems,
                  hearts = hearts,
                  xp = xp,
                  league = league,
                  onOpenProfile = { currentTab = 2 }
                )
              },
              bottomBar = {
                NavigationBar(
                  containerColor = Color(backgroundStyle.hexSurface),
                  tonalElevation = 8.dp
                ) {
                  NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    icon = {
                      Icon(
                        imageVector = if (currentTab == 0) Icons.Filled.Map else Icons.Outlined.Map,
                        contentDescription = "Trilha"
                      )
                    },
                    label = { Text("Trilha", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = DuoAmber,
                      selectedTextColor = DuoAmber,
                      indicatorColor = DuoAmber.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_trilha")
                  )

                  NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    icon = {
                      Icon(
                        imageVector = if (currentTab == 1) Icons.Filled.Timer else Icons.Outlined.Timer,
                        contentDescription = "Foco"
                      )
                    },
                    label = { Text("Foco", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = DuoAmber,
                      selectedTextColor = DuoAmber,
                      indicatorColor = DuoAmber.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_foco")
                  )

                  NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    icon = {
                      Icon(
                        imageVector = if (currentTab == 2) Icons.Filled.Pets else Icons.Outlined.Pets,
                        contentDescription = "Raposinha"
                      )
                    },
                    label = { Text("Raposa", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = DuoAmber,
                      selectedTextColor = DuoAmber,
                      indicatorColor = DuoAmber.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_pet")
                  )

                  NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 },
                    icon = {
                      Icon(
                        imageVector = if (currentTab == 3) Icons.Filled.School else Icons.Outlined.School,
                        contentDescription = "Matérias"
                      )
                    },
                    label = { Text("Matérias", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = DuoAmber,
                      selectedTextColor = DuoAmber,
                      indicatorColor = DuoAmber.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_materias")
                  )

                  NavigationBarItem(
                    selected = currentTab == 4,
                    onClick = { currentTab = 4 },
                    icon = {
                      Icon(
                        imageVector = if (currentTab == 4) Icons.Filled.Alarm else Icons.Outlined.Alarm,
                        contentDescription = "Lembretes"
                      )
                    },
                    label = { Text("Lembretes", fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                      selectedIconColor = DuoAmber,
                      selectedTextColor = DuoAmber,
                      indicatorColor = DuoAmber.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.testTag("nav_tab_lembretes")
                  )
                }
              }
            ) { innerPadding ->
              Box(
                modifier = Modifier
                  .fillMaxSize()
                  .background(Color(backgroundStyle.hexBackground))
                  .padding(innerPadding)
              ) {
                when (currentTab) {
                  0 -> PathScreen(
                    viewModel = viewModel,
                    onNodeClick = { node -> viewModel.openMissionNode(node) }
                  )
                  1 -> FocusScreen(
                    viewModel = viewModel,
                    onNavigateToRotation = { currentTab = 3 }
                  )
                  2 -> PetClosetScreen(viewModel = viewModel)
                  3 -> RotationScreen(
                    viewModel = viewModel,
                    onStartSubjectAndGoFocus = { currentTab = 1 }
                  )
                  4 -> RemindersScreen(viewModel = viewModel)
                }
              }
            }

            // Mission Node modal
            if (activeMissionNode != null) {
              MissionModal(
                node = activeMissionNode,
                onDismiss = { viewModel.dismissMissionNode() },
                onStartMission = { node ->
                  viewModel.startMissionFromNode(node)
                  currentTab = 1
                },
                onOpenQuiz = { node ->
                  viewModel.openQuizForSubject(node.subject)
                }
              )
            }

            // Mini Quiz Dialog
            if (quizSubject != null && selectedQuizQuestion != null) {
              QuizDialog(
                subject = quizSubject!!,
                question = selectedQuizQuestion,
                onDismiss = { viewModel.dismissQuizDialog() },
                onAnswerCorrect = { viewModel.answerQuizCorrect() }
              )
            }

            // Add Custom Subject Dialog
            if (showAddSubject) {
              AddSubjectDialog(
                onDismiss = { viewModel.setAddSubjectDialogVisible(false) },
                onConfirm = { name, level, category, tip, topicsText ->
                  viewModel.addCustomSubject(name, level, category, tip, topicsText)
                }
              )
            }

            // Subject Detail Sheet
            if (detailSubject != null) {
              SubjectDetailSheet(
                subject = detailSubject,
                onDismiss = { viewModel.setSubjectForDetail(null) },
                onStartFocus = { subject ->
                  viewModel.selectSubjectDirectly(subject)
                  currentTab = 1
                },
                onOpenQuiz = { subject ->
                  viewModel.openQuizForCurrentSubject()
                }
              )
            }

            // Session Completed Dialog
            if (sessionCompletedJustNow != null) {
              SessionCompletedDialog(
                session = sessionCompletedJustNow!!,
                nextSubject = nextSubject,
                onStartNextSubject = {
                  viewModel.advanceToNextSubject()
                  viewModel.startTimer()
                },
                onTakeBreak = {
                  viewModel.setBreakDuration(5)
                  viewModel.startTimer()
                },
                onDismiss = { viewModel.dismissSessionCompletedDialog() }
              )
            }
          }
        }
      }
    }
  }
}
