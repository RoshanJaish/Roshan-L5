package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Settings
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BadgeItem
import com.example.data.model.BoardType
import com.example.data.model.GradeLevel
import com.example.ui.QuizViewModel
import com.example.ui.components.HexagonBadgeCard
import com.example.ui.components.LevelProgressBar
import com.example.ui.components.StudentAvatar
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val allBadges = remember(userProfile.unlockedBadges) {
        viewModel.repository.getBadges(userProfile.unlockedBadges)
    }

    val unlockedBadges = allBadges.filter { it.isUnlocked }
    val lockedBadges = allBadges.filter { !it.isUnlocked }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var selectedBadgeDetail by remember { mutableStateOf<BadgeItem?>(null) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSoft)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Avatar & Handle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StudentAvatar(
                        name = userProfile.name,
                        avatarColor = BrandYellow,
                        size = 46.dp
                    )
                    Column {
                        Text(
                            text = userProfile.name,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "@${userProfile.username} • ${userProfile.board.shortName} ${userProfile.grade.displayName}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }

                // Close / Settings Button on top right matching mockup
                Surface(
                    onClick = { showEditProfileDialog = true },
                    shape = CircleShape,
                    color = CardWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = TextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
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
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // 1. Streak Card: "You did 6 streaks" (Yellow banner with cartoon boy reading illustration)
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = BrandYellowCard),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("streak_banner_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "You did",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextPrimary
                            )
                            Text(
                                text = "${userProfile.streakDays} streaks 🔥",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimary
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.streak_banner_art),
                                contentDescription = "Streak celebration illustration",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(76.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = TextPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }

            // 2. Stats Row: [Level: Gold] & [Points: 323]
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Level Card
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = CardWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFEDE9FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "👑", fontSize = 20.sp)
                            }
                            Column {
                                Text(
                                    text = "Level",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = userProfile.levelTitle,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }

                    // Points Card
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = CardWhite,
                        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BrandYellowLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "⭐", fontSize = 20.sp)
                            }
                            Column {
                                Text(
                                    text = "Points",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = "${userProfile.xp}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }

            // 3. "My Level Progress" Section with percentage pill matching mockup
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = CardWhite,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "My Level Progress",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            val progressPercent = ((userProfile.xp % 500) * 100) / 500
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = BrandPurple.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "${progressPercent}%",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrandPurple,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }

                        val progressVal = ((userProfile.xp % 500) / 500f).coerceIn(0f, 1f)
                        LevelProgressBar(
                            progress = progressVal,
                            height = 10.dp,
                            fillColor = BrandPurple
                        )

                        Text(
                            text = "${500 - (userProfile.xp % 500)} XP to next academic tier",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }

            // 4. "My Badges" with "See All"
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "My Badges",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "See All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(unlockedBadges, key = { it.id }) { badge ->
                        HexagonBadgeCard(
                            badge = badge,
                            onClick = { selectedBadgeDetail = badge }
                        )
                    }
                }
            }

            // 5. "Locked Badges" with "See All"
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Locked Badges",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "See All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(lockedBadges, key = { it.id }) { badge ->
                        HexagonBadgeCard(
                            badge = badge,
                            onClick = { selectedBadgeDetail = badge }
                        )
                    }
                }
            }
        }
    }

    // Badge Details Dialog
    selectedBadgeDetail?.let { badge ->
        AlertDialog(
            onDismissRequest = { selectedBadgeDetail = null },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(text = badge.iconEmoji, fontSize = 28.sp)
                    Text(text = badge.name)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = badge.description, fontSize = 14.sp, color = TextPrimary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Criteria: ${badge.unlockCriteria}",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                    if (badge.isUnlocked) {
                        Text(
                            text = "Status: Unlocked & Active ✨",
                            fontSize = 12.sp,
                            color = BrandGreen,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            text = "Status: Locked 🔒",
                            fontSize = 12.sp,
                            color = BrandCoral,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { selectedBadgeDetail = null }) {
                    Text("OK")
                }
            }
        )
    }

    // Edit Profile Modal
    if (showEditProfileDialog) {
        var editedName by remember { mutableStateOf(userProfile.name) }
        var editedBoard by remember { mutableStateOf(userProfile.board) }
        var editedGrade by remember { mutableStateOf(userProfile.grade) }

        ModalBottomSheet(
            onDismissRequest = { showEditProfileDialog = false },
            containerColor = CardWhite,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Edit Student Profile",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                OutlinedTextField(
                    value = editedName,
                    onValueChange = { editedName = it },
                    label = { Text("Student Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Board Syllabus",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(BoardType.values()) { board ->
                        FilterChip(
                            selected = board == editedBoard,
                            onClick = { editedBoard = board },
                            label = { Text(board.displayName) }
                        )
                    }
                }

                Text(
                    text = "Grade",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(GradeLevel.values()) { grade ->
                        FilterChip(
                            selected = grade == editedGrade,
                            onClick = { editedGrade = grade },
                            label = { Text(grade.displayName) }
                        )
                    }
                }

                Button(
                    onClick = {
                        viewModel.updateProfile(editedName, editedBoard, editedGrade)
                        showEditProfileDialog = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
