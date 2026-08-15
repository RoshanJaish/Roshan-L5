package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BadgeItem
import com.example.data.model.BoardType
import com.example.data.model.GradeLevel
import com.example.ui.theme.*

@Composable
fun StudentAvatar(
    name: String,
    modifier: Modifier = Modifier,
    avatarColor: Color = BrandYellow,
    size: Dp = 44.dp,
    showOnlineDot: Boolean = false
) {
    val initial = name.firstOrNull()?.uppercase() ?: "S"
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        listOf(avatarColor, avatarColor.copy(alpha = 0.8f))
                    )
                )
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initial,
                fontWeight = FontWeight.Bold,
                fontSize = (size.value * 0.42).sp,
                color = TextPrimary
            )
        }

        if (showOnlineDot) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(BrandGreen)
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}

@Composable
fun LevelProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFFEADDFF),
    fillColor: Color = BrandPurple,
    height: Dp = 8.dp
) {
    val animatedProgress by animateFloatAsState(targetValue = progress.coerceIn(0f, 1f), label = "progress")
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .fillMaxHeight()
                .clip(RoundedCornerShape(height / 2))
                .background(fillColor)
        )
    }
}

@Composable
fun HexagonBadgeCard(
    badge: BadgeItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = if (badge.isUnlocked) Color(badge.colorHex).copy(alpha = 0.12f) else CardSubtle,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (badge.isUnlocked) Color(badge.colorHex).copy(alpha = 0.4f) else DividerColor
        ),
        modifier = modifier
            .size(80.dp)
            .testTag("badge_${badge.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (badge.isUnlocked) {
                Text(
                    text = badge.iconEmoji,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = badge.name,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 11.sp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked badge",
                    tint = TextMuted,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = badge.name,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BoardGradeSelectorChip(
    board: BoardType,
    grade: GradeLevel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = BrandYellowLight,
        border = androidx.compose.foundation.BorderStroke(1.dp, BrandYellow),
        modifier = modifier.testTag("board_grade_selector")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "${board.shortName} • ${grade.displayName}",
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                color = TextPrimary
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Change board and grade",
                tint = TextPrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
