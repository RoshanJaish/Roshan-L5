package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Quiz
import com.example.ui.theme.*

@Composable
fun QuizHeroBannerCard(
    quiz: Quiz,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BrandPurpleBg),
        modifier = modifier
            .fillMaxWidth()
            .height(175.dp)
            .testTag("hero_quiz_card")
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background illustration / subtle graphic
            Image(
                painter = painterResource(id = R.drawable.quiz_hero_banner),
                contentDescription = "Quiz Banner Illustration",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
            )

            // Semi-transparent overlay on left to ensure crisp text contrast
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                BrandPurple.copy(alpha = 0.92f),
                                BrandPurple.copy(alpha = 0.70f),
                                Color.Transparent
                            ),
                            startX = 0f,
                            endX = 500f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = quiz.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${quiz.questions.size} questions • ${quiz.rewardXP} XP",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    // Progress capsule dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp))
                                .background(BrandYellow)
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.5f))
                        )
                    }
                }

                // Yellow "Let's go!" button matching the mockup
                Button(
                    onClick = onStartClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandYellow,
                        contentColor = TextPrimary
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(38.dp)
                        .testTag("hero_lets_go_button")
                ) {
                    Text(
                        text = "Let's go!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
