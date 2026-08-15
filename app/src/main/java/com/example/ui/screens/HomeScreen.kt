package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.QuizViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: QuizViewModel,
    onStartQuiz: (Quiz) -> Unit,
    onNavigateToChampionship: () -> Unit,
    onNavigateToRankings: () -> Unit,
    onNavigateToProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedBoard by viewModel.selectedBoard.collectAsState()
    val selectedGrade by viewModel.selectedGrade.collectAsState()

    var showBoardGradeDialog by remember { mutableStateOf(false) }
    var selectedFriendForChallenge by remember { mutableStateOf<FriendItem?>(null) }

    val quizzes by viewModel.filteredQuizzes.collectAsState()

    val friends = remember { viewModel.repository.getFriends() }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSoft)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Top status bar matching mockup
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Avatar + Level Progress
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        StudentAvatar(
                            name = userProfile.name,
                            avatarColor = BrandYellow,
                            size = 38.dp,
                            modifier = Modifier.clickable { onNavigateToProfile() }
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "My Level Progress",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            val progress = (userProfile.xp % 500) / 500f
                            LevelProgressBar(
                                progress = progress,
                                height = 6.dp,
                                fillColor = BrandPurple
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Right: XP Star & Notification Icon
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // XP Star Pill
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = BrandYellowLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandYellow),
                            modifier = Modifier.clickable { onNavigateToRankings() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "⭐", fontSize = 13.sp)
                                Text(
                                    text = "${userProfile.xp} XP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        // Bell / Notifications Button
                        Surface(
                            shape = CircleShape,
                            color = CardWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                            modifier = Modifier
                                .size(36.dp)
                                .clickable { onNavigateToProfile() }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = TextPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // "Hi, Catlyne - Let's continue a quiz! ✨"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Hi, ${userProfile.name.split(" ").firstOrNull() ?: userProfile.name}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Let's continue a quiz!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(text = "✨", fontSize = 18.sp)
                        }
                    }

                    // Board & Grade selector pill
                    BoardGradeSelectorChip(
                        board = selectedBoard,
                        grade = selectedGrade,
                        onClick = { showBoardGradeDialog = true }
                    )
                }
            }
        },
        containerColor = BgSoft,
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Hero Quiz Banners Carousel
            item {
                val heroQuiz = quizzes.firstOrNull() ?: viewModel.repository.getQuizzesForFilters(
                    SubjectCategory.ALL,
                    selectedBoard,
                    selectedGrade
                ).first()

                QuizHeroBannerCard(
                    quiz = heroQuiz,
                    onStartClick = { onStartQuiz(heroQuiz) }
                )
            }

            // 2. Championship Spotlight Banner
            item {
                Surface(
                    onClick = { onNavigateToChampionship() },
                    shape = RoundedCornerShape(20.dp),
                    color = BrandYellowCard,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BrandYellowDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("championship_banner_button")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(text = "🏆", fontSize = 32.sp)
                            Column {
                                Text(
                                    text = "All-India Championship",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Live tournament • 500 XP prize pool",
                                    fontSize = 12.sp,
                                    color = TextPrimary.copy(alpha = 0.8f)
                                )
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BrandPurple,
                            contentColor = Color.White
                        ) {
                            Text(
                                text = "Enter",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // 3. "My Friends" Row
            item {
                FriendsRow(
                    friends = friends,
                    onFriendClick = { friend ->
                        selectedFriendForChallenge = friend
                    },
                    onSeeAllClick = { onNavigateToRankings() }
                )
            }

            // 4. Category Filter Chips
            item {
                CategoryFilterChips(
                    selectedCategory = selectedCategory,
                    onCategorySelect = { viewModel.selectCategory(it) }
                )
            }

            // 5. "Latest Quiz" Header & List
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Latest Quiz",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "See All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }

            items(quizzes, key = { it.id }) { quiz ->
                QuizListItemCard(
                    quiz = quiz,
                    onClick = { onStartQuiz(quiz) }
                )
            }
        }
    }

    // Board & Grade Selection Modal Bottom Sheet
    if (showBoardGradeDialog) {
        ModalBottomSheet(
            onDismissRequest = { showBoardGradeDialog = false },
            containerColor = CardWhite,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Select Board & Grade",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Personalize your quiz questions, challenges, and championships based on your school syllabus.",
                    fontSize = 13.sp,
                    color = TextSecondary
                )

                Text(
                    text = "Education Board",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(BoardType.values()) { board ->
                        val isSelected = board == selectedBoard
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectBoard(board) },
                            label = { Text(board.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandPurple,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Text(
                    text = "Grade Level",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(GradeLevel.values()) { grade ->
                        val isSelected = grade == selectedGrade
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectGrade(grade) },
                            label = { Text(grade.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BrandYellow,
                                selectedLabelColor = TextPrimary
                            )
                        )
                    }
                }

                Button(
                    onClick = { showBoardGradeDialog = false },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Apply Changes", fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Friend Challenge Dialog
    selectedFriendForChallenge?.let { friend ->
        AlertDialog(
            onDismissRequest = { selectedFriendForChallenge = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StudentAvatar(name = friend.name, avatarColor = Color(friend.avatarColorHex), size = 36.dp)
                    Text("Challenge ${friend.name}!")
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Send a 1v1 quiz duel challenge to @${friend.username}?")
                    Text("• Board: ${friend.board} (${friend.grade})", color = TextSecondary, fontSize = 13.sp)
                    Text("• Streak: ${friend.streak} days 🔥", color = TextSecondary, fontSize = 13.sp)
                    Text("• Reward: +100 XP for the winner!", color = BrandPurple, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val quiz = quizzes.firstOrNull() ?: viewModel.repository.getQuizzesForFilters(
                            SubjectCategory.ALL,
                            selectedBoard,
                            selectedGrade
                        ).first()
                        selectedFriendForChallenge = null
                        onStartQuiz(quiz.copy(title = "1v1 Duel vs ${friend.name}", isCommunityChallenge = true))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandYellow, contentColor = TextPrimary)
                ) {
                    Text("Start Duel", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedFriendForChallenge = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
