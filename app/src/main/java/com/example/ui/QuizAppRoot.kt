package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.NavTab
import com.example.ui.components.QuizBottomNavBar
import com.example.ui.screens.*
import com.example.ui.theme.BgSoft

@Composable
fun QuizAppRoot(
    viewModel: QuizViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(NavTab.HOME) }
    val activeQuizState by viewModel.activeQuizState.collectAsState()

    // If a quiz is actively being played, show the full screen immersive QuizPlayScreen
    if (activeQuizState != null) {
        QuizPlayScreen(
            viewModel = viewModel,
            activeQuizState = activeQuizState!!,
            onExitQuiz = { viewModel.exitQuiz() },
            modifier = modifier
        )
    } else {
        Scaffold(
            bottomBar = {
                QuizBottomNavBar(
                    currentTab = currentTab,
                    onTabSelected = { currentTab = it }
                )
            },
            containerColor = BgSoft,
            modifier = modifier
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        NavTab.HOME -> HomeScreen(
                            viewModel = viewModel,
                            onStartQuiz = { quiz -> viewModel.startQuiz(quiz) },
                            onNavigateToChampionship = { currentTab = NavTab.CHAMPIONSHIP },
                            onNavigateToRankings = { currentTab = NavTab.RANKINGS },
                            onNavigateToProfile = { currentTab = NavTab.PROFILE }
                        )
                        NavTab.CHAMPIONSHIP -> ChampionshipScreen(
                            viewModel = viewModel,
                            onStartChampionshipQuiz = { quiz -> viewModel.startQuiz(quiz) }
                        )
                        NavTab.COMMUNITY -> CommunityChallengesScreen(
                            viewModel = viewModel,
                            onStartChallengeQuiz = { quiz -> viewModel.startQuiz(quiz) }
                        )
                        NavTab.RANKINGS -> RankingsScreen(
                            viewModel = viewModel,
                            onBack = { currentTab = NavTab.HOME }
                        )
                        NavTab.PROFILE -> ProfileScreen(
                            viewModel = viewModel,
                            onBack = { currentTab = NavTab.HOME }
                        )
                    }
                }
            }
        }
    }
}
