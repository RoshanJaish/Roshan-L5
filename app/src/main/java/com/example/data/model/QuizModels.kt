package com.example.data.model

data class QuizQuestion(
    val id: String,
    val text: String,
    val options: List<String>,
    val correctOptionIndex: Int,
    val explanation: String = "",
    val points: Int = 10
)

enum class BoardType(val displayName: String, val shortName: String) {
    CBSE("CBSE Board", "CBSE"),
    ICSE("ICSE / CISCE", "ICSE"),
    STATE_BOARD("State Board", "State"),
    CAMBRIDGE("Cambridge / IGCSE", "Cambridge"),
    IB("IB World", "IB"),
    GENERAL("General Academic", "General")
}

enum class GradeLevel(val displayName: String, val groupName: String) {
    GRADE_1("Grade 1", "Primary"),
    GRADE_2("Grade 2", "Primary"),
    GRADE_3("Grade 3", "Primary"),
    GRADE_4("Grade 4", "Primary"),
    GRADE_5("Grade 5", "Primary"),
    GRADE_6("Grade 6", "Middle School"),
    GRADE_7("Grade 7", "Middle School"),
    GRADE_8("Grade 8", "Middle School"),
    GRADE_9("Grade 9", "High School"),
    GRADE_10("Grade 10", "High School"),
    GRADE_11("Grade 11", "Senior Secondary"),
    GRADE_12("Grade 12", "Senior Secondary")
}

enum class SubjectCategory(val displayName: String, val iconResName: String) {
    ALL("All", "all"),
    MATH("Math", "math"),
    SCIENCE("Science", "science"),
    ENGLISH("English", "english"),
    SOCIAL_STUDIES("Social", "history"),
    TECH("Computer", "tech"),
    GENERAL_KNOWLEDGE("GK & Animals", "gk")
}

data class Quiz(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: SubjectCategory,
    val board: BoardType,
    val grade: GradeLevel,
    val questions: List<QuizQuestion>,
    val durationMinutes: Int = 5,
    val isChampionship: Boolean = false,
    val isCommunityChallenge: Boolean = false,
    val rewardXP: Int = 50,
    val difficulty: String = "Medium"
)

data class UserProfile(
    val id: String = "user_main",
    val name: String = "Catlyne Sarah",
    val username: String = "cat123_",
    val avatarIndex: Int = 0,
    val board: BoardType = BoardType.CBSE,
    val grade: GradeLevel = GradeLevel.GRADE_8,
    val levelTitle: String = "Gold",
    val xp: Int = 323,
    val xpForNextLevel: Int = 500,
    val streakDays: Int = 6,
    val bestStreak: Int = 12,
    val totalQuizzesCompleted: Int = 24,
    val championshipsWon: Int = 3,
    val unlockedBadges: List<String> = listOf("super_star", "quiz_champion", "math_whiz", "science_prodigy")
)

data class BadgeItem(
    val id: String,
    val name: String,
    val description: String,
    val iconEmoji: String,
    val colorHex: Long,
    val isUnlocked: Boolean,
    val unlockCriteria: String
)

data class FriendItem(
    val id: String,
    val name: String,
    val username: String,
    val avatarColorHex: Long,
    val streak: Int,
    val points: Int,
    val isOnline: Boolean,
    val board: String,
    val grade: String
)

data class ChampionshipEvent(
    val id: String,
    val title: String,
    val subtitle: String,
    val board: BoardType,
    val gradeGroup: String,
    val subject: SubjectCategory,
    val prizeXP: Int,
    val participantsCount: Int,
    val status: String, // "Live Now", "Starting Soon", "Ended"
    val endsInMinutes: Int,
    val quiz: Quiz
)

data class CommunityChallenge(
    val id: String,
    val creatorName: String,
    val creatorAvatarColor: Long,
    val title: String,
    val topic: String,
    val board: BoardType,
    val grade: GradeLevel,
    val subject: SubjectCategory,
    val prizeXP: Int,
    val participantsCount: Int,
    val maxParticipants: Int = 50,
    val quiz: Quiz
)
