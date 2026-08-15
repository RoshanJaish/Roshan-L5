package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Quiz
import com.example.data.model.SubjectCategory
import com.example.ui.theme.*

@Composable
fun QuizListItemCard(
    quiz: Quiz,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = CardWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, DividerColor),
        modifier = modifier
            .fillMaxWidth()
            .testTag("quiz_card_${quiz.id}")
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
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Category icon box matching the mockup
                val (icon, bgColor, tint) = getCategoryIconAndColors(quiz.category)
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = quiz.category.displayName,
                        tint = tint,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Column {
                    Text(
                        text = quiz.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = quiz.subtitle,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${quiz.questions.size} Qs",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPurple
                        )
                        Text(
                            text = "•",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = "+${quiz.rewardXP} XP",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandOrange
                        )
                        Text(
                            text = "•",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                        Text(
                            text = quiz.board.shortName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Start Quiz",
                tint = TextMuted,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun getCategoryIconAndColors(category: SubjectCategory): Triple<ImageVector, Color, Color> {
    return when (category) {
        SubjectCategory.MATH -> Triple(Icons.Default.Calculate, Color(0xFFFFECEC), Color(0xFFFF6B6B))
        SubjectCategory.SCIENCE -> Triple(Icons.Default.Science, Color(0xFFE8F4FD), Color(0xFF4EA8DE))
        SubjectCategory.ENGLISH -> Triple(Icons.Default.MenuBook, Color(0xFFFFF7DB), Color(0xFFE09F00))
        SubjectCategory.SOCIAL_STUDIES -> Triple(Icons.Default.Public, Color(0xFFF3E8FF), Color(0xFF9333EA))
        SubjectCategory.TECH -> Triple(Icons.Default.Computer, Color(0xFFE0F2FE), Color(0xFF0284C7))
        SubjectCategory.GENERAL_KNOWLEDGE -> Triple(Icons.Default.Pets, Color(0xFFE6F4EA), Color(0xFF34A853))
        SubjectCategory.ALL -> Triple(Icons.Default.AutoAwesome, Color(0xFFEDE9FE), BrandPurple)
    }
}
