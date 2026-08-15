package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Category entity storing subject metadata in Room.
 */
@Entity(tableName = "quiz_categories")
data class QuizCategoryEntity(
    @PrimaryKey val id: String, // e.g. "MATH", "SCIENCE", "ENGLISH", "SOCIAL_STUDIES", "TECH", "GENERAL_KNOWLEDGE"
    val displayName: String,
    val description: String,
    val iconResName: String,
    val colorHex: Long = 0xFF6750A4
)

/**
 * Quiz header entity storing quiz metadata in Room.
 */
@Entity(tableName = "quizzes")
data class QuizEntity(
    @PrimaryKey val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val board: String,
    val grade: String,
    val durationMinutes: Int = 5,
    val isChampionship: Boolean = false,
    val isCommunityChallenge: Boolean = false,
    val rewardXP: Int = 50,
    val difficulty: String = "Medium",
    val questionCount: Int = 5
)

/**
 * Quiz question entity storing individual questions and options in Room.
 */
@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey val id: String,
    val quizId: String,
    val text: String,
    val optionsJson: String, // Comma separated or serialized options
    val correctOptionIndex: Int,
    val explanation: String = "",
    val points: Int = 10,
    val category: String, // MATH, SCIENCE, etc.
    val board: String, // CBSE, ICSE, etc.
    val grade: String, // GRADE_8, etc.
    val difficulty: String = "Medium"
)

/**
 * Student Profile entity storing student academic progress, XP, streak, and tier.
 */
@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: String = "user_main",
    val name: String = "Catlyne Sarah",
    val username: String = "cat123_",
    val avatarIndex: Int = 0,
    val board: String = "CBSE",
    val grade: String = "GRADE_8",
    val levelTitle: String = "Gold",
    val xp: Int = 323,
    val streakDays: Int = 6,
    val bestStreak: Int = 12,
    val totalQuizzesCompleted: Int = 24,
    val championshipsWon: Int = 3,
    val unlockedBadgesCsv: String = "super_star,quiz_champion,math_whiz,science_prodigy"
)

/**
 * Quiz attempt entity tracking student progress and past quiz performances.
 */
@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val quizId: String,
    val quizTitle: String,
    val category: String,
    val board: String,
    val grade: String,
    val score: Int,
    val totalQuestions: Int,
    val xpEarned: Int,
    val timeTakenSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Custom student-created challenge duels.
 */
@Entity(tableName = "custom_challenges")
data class CustomChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val topic: String,
    val creatorName: String,
    val board: String,
    val grade: String,
    val subject: String,
    val prizeXP: Int,
    val questionsJson: String,
    val timestamp: Long = System.currentTimeMillis()
)

