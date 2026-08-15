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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
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
import com.example.ui.components.StudentAvatar
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityChallengesScreen(
    viewModel: QuizViewModel,
    onStartChallengeQuiz: (Quiz) -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val challenges = remember { viewModel.repository.getCommunityChallenges() }

    var showCreateChallengeDialog by remember { mutableStateOf(false) }
    var roomCodeInput by remember { mutableStateOf("") }
    var showJoinDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSoft)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Community Challenges",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(text = "⚔️", fontSize = 20.sp)
                        }
                        Text(
                            text = "Duel classmates on board & grade topics",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    // Create Challenge Button
                    Button(
                        onClick = { showCreateChallengeDialog = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("create_challenge_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Create", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Join with Code Card
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CardWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                    modifier = Modifier.fillMaxWidth()
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
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(BrandYellowLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🔑", fontSize = 20.sp)
                            }
                            Column {
                                Text(
                                    text = "Have a Room Code?",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Enter code to join friend's duel",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = { showJoinDialog = true },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandYellow,
                                contentColor = TextPrimary
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Join Room", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = "Active Topic Showdowns",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            // Challenge Cards
            items(challenges, key = { it.id }) { challenge ->
                Surface(
                    shape = RoundedCornerShape(22.dp),
                    color = CardWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                StudentAvatar(
                                    name = challenge.creatorName,
                                    avatarColor = Color(challenge.creatorAvatarColor),
                                    size = 38.dp
                                )
                                Column {
                                    Text(
                                        text = challenge.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Created by ${challenge.creatorName}",
                                        fontSize = 12.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BrandYellowLight,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BrandYellow)
                            ) {
                                Text(
                                    text = "+${challenge.prizeXP} XP",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Topic details
                        Text(
                            text = challenge.topic,
                            fontSize = 13.sp,
                            color = TextPrimary.copy(alpha = 0.85f)
                        )

                        // Tags & Status Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFEDE9FE)
                                ) {
                                    Text(
                                        text = "${challenge.board.shortName} • ${challenge.grade.displayName}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = BrandPurple,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Text(
                                    text = "${challenge.participantsCount}/${challenge.maxParticipants} joined",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Button(
                                onClick = { onStartChallengeQuiz(challenge.quiz) },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                                modifier = Modifier.height(36.dp)
                            ) {
                                Text("Accept Duel", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }

    // Join Code Dialog
    if (showJoinDialog) {
        AlertDialog(
            onDismissRequest = { showJoinDialog = false },
            title = { Text("Enter Challenge Room Code") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Ask your friend or teacher for the 6-digit challenge code.")
                    OutlinedTextField(
                        value = roomCodeInput,
                        onValueChange = { if (it.length <= 6) roomCodeInput = it.uppercase() },
                        placeholder = { Text("e.g. QZ8492") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showJoinDialog = false
                        val quiz = challenges.first().quiz
                        onStartChallengeQuiz(quiz.copy(title = "Room Duel $roomCodeInput", isCommunityChallenge = true))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple)
                ) {
                    Text("Join Duel")
                }
            },
            dismissButton = {
                TextButton(onClick = { showJoinDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Create Challenge Dialog Modal
    if (showCreateChallengeDialog) {
        var challengeTitle by remember { mutableStateOf("") }
        var challengeTopic by remember { mutableStateOf("") }
        var selectedSubject by remember { mutableStateOf(SubjectCategory.MATH) }
        var selectedBoard by remember { mutableStateOf(userProfile.board) }
        var selectedGrade by remember { mutableStateOf(userProfile.grade) }

        ModalBottomSheet(
            onDismissRequest = { showCreateChallengeDialog = false },
            containerColor = CardWhite,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Create Community Challenge",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = challengeTitle,
                    onValueChange = { challengeTitle = it },
                    label = { Text("Challenge Name (e.g., Geometry Battle)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = challengeTopic,
                    onValueChange = { challengeTopic = it },
                    label = { Text("Syllabus Topic (e.g., Circles & Triangles)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(text = "Subject", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(SubjectCategory.values().filter { it != SubjectCategory.ALL }) { subject ->
                        FilterChip(
                            selected = subject == selectedSubject,
                            onClick = { selectedSubject = subject },
                            label = { Text(subject.displayName) }
                        )
                    }
                }

                Text(text = "Target Board & Grade", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(BoardType.values()) { board ->
                        FilterChip(
                            selected = board == selectedBoard,
                            onClick = { selectedBoard = board },
                            label = { Text(board.shortName) }
                        )
                    }
                }

                Button(
                    onClick = {
                        val title = if (challengeTitle.isNotBlank()) challengeTitle else "${userProfile.name}'s Quiz Duel"
                        showCreateChallengeDialog = false
                        val quiz = challenges.first().quiz.copy(
                            title = title,
                            subtitle = if (challengeTopic.isNotBlank()) challengeTopic else "Community Challenge",
                            category = selectedSubject,
                            board = selectedBoard,
                            grade = selectedGrade,
                            isCommunityChallenge = true,
                            rewardXP = 120
                        )
                        onStartChallengeQuiz(quiz)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPurple),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Launch Challenge Room 🚀", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
