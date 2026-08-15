package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BoardType
import com.example.data.model.GradeLevel
import com.example.ui.QuizViewModel
import com.example.ui.components.StudentAvatar
import com.example.ui.theme.*

data class LeaderboardUser(
    val rank: Int,
    val name: String,
    val username: String,
    val avatarColorHex: Long,
    val streak: Int,
    val xp: Int,
    val board: String,
    val grade: String,
    val isCurrentUser: Boolean = false
)

@Composable
fun RankingsScreen(
    viewModel: QuizViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val userProfile by viewModel.userProfile.collectAsState()
    var selectedTab by remember { mutableStateOf("Weekly") }

    val leaderboardList = remember(userProfile.xp, userProfile.streakDays, selectedTab) {
        val list = mutableListOf(
            LeaderboardUser(1, "Jane", "jane_genius", 0xFF6BCB77, 12, 1080, "ICSE", "Grade 8"),
            LeaderboardUser(2, "Michael", "mike_champ", 0xFFFF7B54, 16, 890, "CBSE", "Grade 8"),
            LeaderboardUser(3, userProfile.name, userProfile.username, 0xFFFFD043, userProfile.streakDays, userProfile.xp, userProfile.board.shortName, userProfile.grade.displayName, isCurrentUser = true),
            LeaderboardUser(4, "Amoora", "amoora_99", 0xFF4D96FF, 7, 280, "CBSE", "Grade 8"),
            LeaderboardUser(5, "Zara Khan", "zara_k", 0xFFFF6B6B, 9, 245, "State Board", "Grade 8"),
            LeaderboardUser(6, "Alex Chen", "alex_c", 0xFFFFD93D, 5, 195, "Cambridge", "Grade 7"),
            LeaderboardUser(7, "Rohan Verma", "rohan_v", 0xFF845EC2, 4, 160, "CBSE", "Grade 8"),
            LeaderboardUser(8, "Sophia Lee", "sophia_l", 0xFFFF9671, 3, 140, "IB", "Grade 8")
        )
        // Sort based on XP
        list.sortedByDescending { it.xp }.mapIndexed { index, item -> item.copy(rank = index + 1) }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BgSoft)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Header: [< My Ranking]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            onClick = onBack,
                            shape = CircleShape,
                            color = CardWhite,
                            border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back",
                                    tint = TextPrimary
                                )
                            }
                        }

                        Text(
                            text = "My Ranking",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BrandYellowLight
                    ) {
                        Text(
                            text = "${userProfile.board.shortName} Rank",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: [World] [Weekly] [Friends] matching mockup
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(CardSubtle)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("World", "Weekly", "Friends").forEach { tab ->
                        val isSelected = tab == selectedTab
                        Surface(
                            onClick = { selectedTab = tab },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) BrandPurple else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("ranking_tab_$tab")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = tab,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else TextSecondary
                                )
                            }
                        }
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
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Top 3 Podium
            item {
                PodiumSection(leaderboardList.take(3))
            }

            // All Rankings Header
            item {
                Text(
                    text = "Leaderboard Standings",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                )
            }

            // List items
            itemsIndexed(leaderboardList, key = { _, item -> item.name + item.rank }) { _, student ->
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (student.isCurrentUser) BrandPurple.copy(alpha = 0.08f) else CardWhite,
                    border = androidx.compose.foundation.BorderStroke(
                        if (student.isCurrentUser) 1.5.dp else 1.dp,
                        if (student.isCurrentUser) BrandPurple else DividerColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Rank number circle or crown
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (student.rank) {
                                            1 -> BrandYellow
                                            2 -> Color(0xFFE2E8F0)
                                            3 -> Color(0xFFFFD1A9)
                                            else -> CardSubtle
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${student.rank}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }

                            StudentAvatar(
                                name = student.name,
                                avatarColor = Color(student.avatarColorHex),
                                size = 42.dp
                            )

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = student.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    if (student.isCurrentUser) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = BrandPurple,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = "You",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${student.streak} Streak • ${student.board}",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        // Points / XP Badge matching mockup
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = BrandYellowLight,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BrandYellow)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "⭐", fontSize = 12.sp)
                                Text(
                                    text = "${student.xp} point",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PodiumSection(topThree: List<LeaderboardUser>) {
    if (topThree.size < 3) return

    val first = topThree[0]
    val second = topThree[1]
    val third = topThree[2]

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        // 2nd Place (Silver)
        PodiumCol(student = second, rank = 2, height = 110.dp, crown = "🥈")

        // 1st Place (Gold)
        PodiumCol(student = first, rank = 1, height = 140.dp, crown = "👑")

        // 3rd Place (Bronze)
        PodiumCol(student = third, rank = 3, height = 90.dp, crown = "🥉")
    }
}

@Composable
fun PodiumCol(
    student: LeaderboardUser,
    rank: Int,
    height: androidx.compose.ui.unit.Dp,
    crown: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(text = crown, fontSize = 20.sp)
        StudentAvatar(
            name = student.name,
            avatarColor = Color(student.avatarColorHex),
            size = if (rank == 1) 56.dp else 48.dp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = student.name.split(" ").firstOrNull() ?: student.name,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "${student.xp} XP",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = BrandPurple
        )
        Spacer(modifier = Modifier.height(6.dp))

        // Pedestal
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            if (rank == 1) BrandYellow else if (rank == 2) Color(0xFFCBD5E1) else Color(0xFFFFD1A9),
                            if (rank == 1) BrandYellowDark else if (rank == 2) Color(0xFF94A3B8) else Color(0xFFFDBA74)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$rank",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}
