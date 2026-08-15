package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.QuizQuestion
import com.example.data.repository.QuizResultOutcome
import com.example.ui.ActiveQuizState
import com.example.ui.QuizViewModel
import com.example.ui.components.HexagonBadgeCard
import com.example.ui.theme.*
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizPlayScreen(
    viewModel: QuizViewModel,
    activeQuizState: ActiveQuizState,
    onExitQuiz: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quiz = activeQuizState.quiz
    val questions = quiz.questions
    val currentIndex = activeQuizState.currentQuestionIndex
    val currentQuestion = questions.getOrNull(currentIndex) ?: questions[0]
    val selectedOptionIndex = activeQuizState.selectedAnswers[currentIndex]
    val secondsRemaining = activeQuizState.secondsRemaining

    var showQuestionGridModal by remember { mutableStateOf(false) }

    // Format mm:ss
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

    // Full screen lavender/purple canvas matching the center mockup
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(QuizCanvasPurple)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. Top Header Bar: [<] [Math Quiz] [∷]
            Column {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Back button in rounded semi-transparent container
                    Surface(
                        onClick = onExitQuiz,
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(42.dp)
                            .testTag("quiz_back_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Centered Quiz Title
                    Text(
                        text = quiz.title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    // Grid question navigator icon button
                    Surface(
                        onClick = { showQuestionGridModal = true },
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.2f),
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(42.dp)
                            .testTag("quiz_grid_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "Question grid overview",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Question Counter & Progress row ("03 Question" and "3 of 7")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val questionNumStr = String.format(Locale.getDefault(), "%02d Question", currentIndex + 1)
                    Text(
                        text = questionNumStr,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${currentIndex + 1} of ${questions.size}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal segmented capsule progress indicators matching the design
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    questions.forEachIndexed { idx, _ ->
                        val isAnswered = activeQuizState.selectedAnswers.containsKey(idx)
                        val isCurrent = idx == currentIndex
                        val capsuleColor = when {
                            isCurrent -> Color.White
                            isAnswered -> BrandYellow
                            else -> Color.White.copy(alpha = 0.25f)
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(capsuleColor)
                                .clickable { viewModel.goToQuestion(idx) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Yellow countdown timer pill on the right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = BrandYellow,
                        modifier = Modifier.testTag("quiz_timer_badge")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Timer,
                                contentDescription = "Timer",
                                tint = TextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = timeFormatted,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            // 2. Middle Section: Question Illustration + Question Statement + Options
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                // Illustration Card
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.quiz_question_art),
                            contentDescription = "Question Graphic",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(20.dp))
                        )
                    }
                }

                // Question Statement in large bold white typography
                item {
                    Text(
                        text = currentQuestion.text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 28.sp,
                        modifier = Modifier.testTag("question_text")
                    )
                }

                // "Choose your answer" label
                item {
                    Text(
                        text = "Choose your answer",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White.copy(alpha = 0.75f)
                    )
                }

                // Answer Options (A, B, C, D)
                val optionPrefixes = listOf("A", "B", "C", "D", "E", "F")
                itemsIndexed(currentQuestion.options) { optIndex, optionText ->
                    val prefix = optionPrefixes.getOrElse(optIndex) { "${optIndex + 1}" }
                    val isSelected = selectedOptionIndex == optIndex

                    Surface(
                        onClick = { viewModel.selectOption(currentIndex, optIndex) },
                        shape = RoundedCornerShape(24.dp),
                        color = if (isSelected) QuizOptionSelected else QuizOptionDefault,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("option_${prefix.lowercase()}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = "$prefix. $optionText",
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            // 3. Bottom Controls Bar: [< Previous] (Circle Question #) [Next >]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Previous Button
                Surface(
                    onClick = { viewModel.previousQuestion() },
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White.copy(alpha = if (currentIndex > 0) 0.25f else 0.10f),
                    contentColor = Color.White,
                    enabled = currentIndex > 0,
                    modifier = Modifier
                        .height(46.dp)
                        .testTag("quiz_prev_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Previous",
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Previous",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Question jump circle with current index pill
                Surface(
                    onClick = { showQuestionGridModal = true },
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f),
                    contentColor = Color.White,
                    modifier = Modifier
                        .size(46.dp)
                        .testTag("question_jump_circle")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            shape = CircleShape,
                            color = BrandCyan,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${currentIndex + 1}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                // Next or Submit Button
                val isLastQuestion = currentIndex == questions.size - 1
                Surface(
                    onClick = { viewModel.nextQuestion() },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isLastQuestion) BrandYellow else Color.White,
                    contentColor = TextPrimary,
                    modifier = Modifier
                        .height(46.dp)
                        .testTag("quiz_next_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (isLastQuestion) "Submit" else "Next",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Next",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }

    // Question Grid Selector Modal
    if (showQuestionGridModal) {
        ModalBottomSheet(
            onDismissRequest = { showQuestionGridModal = false },
            containerColor = CardWhite,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Question Overview",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    questions.forEachIndexed { index, _ ->
                        val isAnswered = activeQuizState.selectedAnswers.containsKey(index)
                        val isCurrent = index == currentIndex

                        Surface(
                            onClick = {
                                viewModel.goToQuestion(index)
                                showQuestionGridModal = false
                            },
                            shape = CircleShape,
                            color = when {
                                isCurrent -> BrandPurple
                                isAnswered -> BrandYellow
                                else -> CardSubtle
                            },
                            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isCurrent) Color.White else TextPrimary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        showQuestionGridModal = false
                        viewModel.submitActiveQuiz()
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandYellow, contentColor = TextPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finish & Submit Quiz", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Quiz Completion Result Dialog / Sheet
    if (activeQuizState.isFinished && activeQuizState.resultOutcome != null) {
        val outcome = activeQuizState.resultOutcome
        AlertDialog(
            onDismissRequest = { /* Must click action */ },
            containerColor = CardWhite,
            shape = RoundedCornerShape(28.dp),
            title = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (outcome.isPerfect) "🎉 Perfect Score!" else if (outcome.percentage >= 60) "🌟 Great Job!" else "💪 Keep Practicing!",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = quiz.title,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Score display circle
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(BrandPurple.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${outcome.score}/${outcome.totalQuestions}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = BrandPurple
                            )
                            Text(
                                text = "${outcome.percentage}%",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                    }

                    // Stat Pills Row: XP Earned & Streaks
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BrandYellowLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandYellow)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "⭐", fontSize = 16.sp)
                                Text(
                                    text = "+${outcome.xpEarned} XP",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFFFECEC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandCoral)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "🔥", fontSize = 16.sp)
                                Text(
                                    text = "${outcome.streak} Streak",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    // New badges alert if any
                    if (outcome.newlyEarnedBadges.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFF3E8FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC084FC)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(text = "🏆", fontSize = 22.sp)
                                Column {
                                    Text(
                                        text = "New Badge Unlocked!",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandPurple
                                    )
                                    Text(
                                        text = outcome.newlyEarnedBadges.joinToString(", ").replace("_", " ").capitalize(Locale.getDefault()),
                                        fontSize = 11.sp,
                                        color = TextPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = onExitQuiz,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quiz_finish_return_button")
                ) {
                    Text("Continue Learning", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }
            }
        )
    }
}
