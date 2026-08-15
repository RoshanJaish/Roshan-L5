package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class QuizRepository(
    private val quizDao: QuizDao
) {
    val userProfileFlow: Flow<UserProfile> = quizDao.getUserProfileFlow().map { entity ->
        if (entity != null) {
            val board = try { BoardType.valueOf(entity.board) } catch (e: Exception) { BoardType.CBSE }
            val grade = try { GradeLevel.valueOf(entity.grade) } catch (e: Exception) { GradeLevel.GRADE_8 }
            val unlockedBadges = entity.unlockedBadgesCsv.split(",").filter { it.isNotBlank() }
            UserProfile(
                id = entity.id,
                name = entity.name,
                username = entity.username,
                avatarIndex = entity.avatarIndex,
                board = board,
                grade = grade,
                levelTitle = entity.levelTitle,
                xp = entity.xp,
                xpForNextLevel = calculateXpForNextLevel(entity.xp),
                streakDays = entity.streakDays,
                bestStreak = entity.bestStreak,
                totalQuizzesCompleted = entity.totalQuizzesCompleted,
                championshipsWon = entity.championshipsWon,
                unlockedBadges = unlockedBadges
            )
        } else {
            // Default profile
            UserProfile()
        }
    }

    val quizAttemptsFlow: Flow<List<QuizAttemptEntity>> = quizDao.getAllAttempts()

    val categoriesFlow: Flow<List<QuizCategoryEntity>> = quizDao.getAllCategories()

    /**
     * Seeds the Room Database with initial categories, quizzes, subject questions,
     * and default student profile if not already seeded.
     */
    suspend fun seedDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val existingProfile = quizDao.getUserProfile()
        if (existingProfile == null) {
            quizDao.insertOrUpdateProfile(
                UserProfileEntity(
                    id = "user_main",
                    name = "Catlyne Sarah",
                    username = "cat123_",
                    avatarIndex = 0,
                    board = BoardType.CBSE.name,
                    grade = GradeLevel.GRADE_8.name,
                    levelTitle = "Gold",
                    xp = 323,
                    streakDays = 6,
                    bestStreak = 12,
                    totalQuizzesCompleted = 24,
                    championshipsWon = 3,
                    unlockedBadgesCsv = "super_star,quiz_champion,math_whiz,science_prodigy"
                )
            )
        }

        // Seed Categories
        quizDao.insertCategories(QuizPreloadData.categoryEntities)

        // Seed Quizzes & Questions if empty
        val questionCount = quizDao.getQuestionsCount()
        if (questionCount == 0) {
            val allQuizzes = QuizPreloadData.defaultQuizzes
            val quizEntities = allQuizzes.map { QuizPreloadData.quizToEntity(it) }
            quizDao.insertQuizzes(quizEntities)

            val questionEntities = mutableListOf<QuizQuestionEntity>()
            allQuizzes.forEach { quiz ->
                quiz.questions.forEach { q ->
                    questionEntities.add(
                        QuizPreloadData.questionToEntity(
                            q = q,
                            quizId = quiz.id,
                            category = quiz.category.name,
                            board = quiz.board.name,
                            grade = quiz.grade.name
                        )
                    )
                }
            }

            // Also seed championship quiz questions
            QuizPreloadData.championshipEvents.forEach { event ->
                quizDao.insertQuiz(QuizPreloadData.quizToEntity(event.quiz))
                event.quiz.questions.forEach { q ->
                    questionEntities.add(
                        QuizPreloadData.questionToEntity(
                            q = q,
                            quizId = event.quiz.id,
                            category = event.quiz.category.name,
                            board = event.quiz.board.name,
                            grade = event.quiz.grade.name
                        )
                    )
                }
            }

            quizDao.insertQuestions(questionEntities)
        }
    }

    // ==================== QUESTION RETRIEVAL ====================

    /**
     * Reactive Question Retrieval Flow for a specific Quiz ID.
     */
    fun getQuestionsForQuizFlow(quizId: String): Flow<List<QuizQuestion>> {
        return quizDao.getQuestionsForQuizFlow(quizId).map { entities ->
            entities.map { QuizPreloadData.entityToQuestion(it) }
        }
    }

    /**
     * Synchronous / Suspend Question Retrieval for a specific Quiz ID.
     */
    suspend fun getQuestionsForQuiz(quizId: String): List<QuizQuestion> = withContext(Dispatchers.IO) {
        val entities = quizDao.getQuestionsForQuiz(quizId)
        if (entities.isNotEmpty()) {
            entities.map { QuizPreloadData.entityToQuestion(it) }
        } else {
            // Fallback to preload in-memory list if not found
            QuizPreloadData.defaultQuizzes.firstOrNull { it.id == quizId }?.questions ?: emptyList()
        }
    }

    /**
     * Retrieve all questions for a subject category.
     */
    fun getQuestionsByCategory(category: SubjectCategory): Flow<List<QuizQuestion>> {
        return quizDao.getQuestionsByCategory(category.name).map { entities ->
            entities.map { QuizPreloadData.entityToQuestion(it) }
        }
    }

    /**
     * Retrieve questions filtered by board and grade level.
     */
    fun getQuestionsByBoardAndGrade(board: BoardType, grade: GradeLevel): Flow<List<QuizQuestion>> {
        return quizDao.getQuestionsByBoardAndGrade(board.name, grade.name).map { entities ->
            entities.map { QuizPreloadData.entityToQuestion(it) }
        }
    }

    /**
     * Retrieve a full Quiz with its persisted questions from Room.
     */
    suspend fun getFullQuizWithQuestions(quizId: String): Quiz? = withContext(Dispatchers.IO) {
        val quizEntity = quizDao.getQuizByIdSync(quizId)
        if (quizEntity != null) {
            val questions = getQuestionsForQuiz(quizId)
            QuizPreloadData.entityToQuiz(quizEntity, questions)
        } else {
            QuizPreloadData.defaultQuizzes.firstOrNull { it.id == quizId }
        }
    }

    // ==================== QUIZZES FLOW & RETRIEVAL ====================

    fun getQuizzesForFiltersFlow(
        category: SubjectCategory,
        board: BoardType,
        grade: GradeLevel
    ): Flow<List<Quiz>> {
        val catName = if (category == SubjectCategory.ALL) "ALL" else category.name
        val boardName = board.name
        val gradeName = grade.name

        return quizDao.getQuizzesByFilters(catName, boardName, gradeName).map { entities ->
            if (entities.isNotEmpty()) {
                entities.map { entity ->
                    val questions = quizDao.getQuestionsForQuiz(entity.id).map { QuizPreloadData.entityToQuestion(it) }
                    QuizPreloadData.entityToQuiz(entity, questions)
                }
            } else {
                // If filter specific quizzes aren't pre-seeded, fallback to matching default quizzes
                QuizPreloadData.defaultQuizzes.filter { quiz ->
                    (category == SubjectCategory.ALL || quiz.category == category)
                }
            }
        }
    }

    fun getQuizzesForFilters(category: SubjectCategory, board: BoardType, grade: GradeLevel): List<Quiz> {
        val allQuizzes = QuizPreloadData.defaultQuizzes
        return allQuizzes.filter { quiz ->
            (category == SubjectCategory.ALL || quiz.category == category)
        }
    }

    fun getChampionshipEvents(): List<ChampionshipEvent> {
        return QuizPreloadData.championshipEvents
    }

    fun getCommunityChallenges(): List<CommunityChallenge> {
        return QuizPreloadData.communityChallenges
    }

    fun getFriends(): List<FriendItem> {
        return QuizPreloadData.friendsList
    }

    fun getBadges(unlockedIds: List<String>): List<BadgeItem> {
        return QuizPreloadData.badgesList.map { badge ->
            badge.copy(isUnlocked = unlockedIds.contains(badge.id))
        }
    }

    suspend fun updatePreferences(board: BoardType, grade: GradeLevel, name: String? = null) = withContext(Dispatchers.IO) {
        val current = quizDao.getUserProfile() ?: UserProfileEntity()
        quizDao.insertOrUpdateProfile(
            current.copy(
                board = board.name,
                grade = grade.name,
                name = name ?: current.name
            )
        )
    }

    suspend fun recordQuizResult(
        quiz: Quiz,
        score: Int,
        totalQuestions: Int,
        timeTakenSeconds: Int
    ): QuizResultOutcome = withContext(Dispatchers.IO) {
        val current = quizDao.getUserProfile() ?: UserProfileEntity()
        val percentage = (score * 100) / maxOf(1, totalQuestions)
        val isPassed = percentage >= 60

        val baseXP = quiz.rewardXP
        val earnedXP = if (isPassed) {
            val ratio = score.toFloat() / totalQuestions.toFloat()
            (baseXP * ratio).toInt() + (if (score == totalQuestions) 25 else 10)
        } else {
            10 // participation XP
        }

        val newStreak = if (isPassed) current.streakDays + 1 else current.streakDays
        val bestStreak = maxOf(newStreak, current.bestStreak)
        val newXp = current.xp + earnedXP
        val newLevelTitle = getLevelTitle(newXp)
        val newQuizzesCount = current.totalQuizzesCompleted + 1
        val newChampionships = if (quiz.isChampionship && percentage >= 80) current.championshipsWon + 1 else current.championshipsWon

        val unlockedBadges = current.unlockedBadgesCsv.split(",").toMutableSet()
        val newlyEarnedBadges = mutableListOf<String>()

        if (score == totalQuestions && !unlockedBadges.contains("super_star")) {
            unlockedBadges.add("super_star")
            newlyEarnedBadges.add("super_star")
        }
        if (quiz.isChampionship && percentage >= 75 && !unlockedBadges.contains("quiz_champion")) {
            unlockedBadges.add("quiz_champion")
            newlyEarnedBadges.add("quiz_champion")
        }
        if (quiz.category == SubjectCategory.MATH && !unlockedBadges.contains("math_whiz")) {
            unlockedBadges.add("math_whiz")
            newlyEarnedBadges.add("math_whiz")
        }
        if (quiz.category == SubjectCategory.SCIENCE && !unlockedBadges.contains("science_prodigy")) {
            unlockedBadges.add("science_prodigy")
            newlyEarnedBadges.add("science_prodigy")
        }
        if (newStreak >= 7 && !unlockedBadges.contains("streak_legend")) {
            unlockedBadges.add("streak_legend")
            newlyEarnedBadges.add("streak_legend")
        }
        if (timeTakenSeconds < 60 && percentage >= 80 && !unlockedBadges.contains("speed_demon")) {
            unlockedBadges.add("speed_demon")
            newlyEarnedBadges.add("speed_demon")
        }
        if (newXp >= 1000 && !unlockedBadges.contains("olympiad_grandmaster")) {
            unlockedBadges.add("olympiad_grandmaster")
            newlyEarnedBadges.add("olympiad_grandmaster")
        }

        val updatedProfile = current.copy(
            xp = newXp,
            levelTitle = newLevelTitle,
            streakDays = newStreak,
            bestStreak = bestStreak,
            totalQuizzesCompleted = newQuizzesCount,
            championshipsWon = newChampionships,
            unlockedBadgesCsv = unlockedBadges.filter { it.isNotBlank() }.joinToString(",")
        )
        quizDao.insertOrUpdateProfile(updatedProfile)

        // Record attempt in Room
        quizDao.insertAttempt(
            QuizAttemptEntity(
                quizId = quiz.id,
                quizTitle = quiz.title,
                category = quiz.category.name,
                board = quiz.board.name,
                grade = quiz.grade.name,
                score = score,
                totalQuestions = totalQuestions,
                xpEarned = earnedXP,
                timeTakenSeconds = timeTakenSeconds
            )
        )

        QuizResultOutcome(
            score = score,
            totalQuestions = totalQuestions,
            percentage = percentage,
            xpEarned = earnedXP,
            streak = newStreak,
            newLevel = newLevelTitle,
            newlyEarnedBadges = newlyEarnedBadges,
            isPerfect = score == totalQuestions
        )
    }

    private fun calculateXpForNextLevel(xp: Int): Int {
        return when {
            xp < 200 -> 200
            xp < 500 -> 500
            xp < 1000 -> 1000
            xp < 2000 -> 2000
            else -> xp + 500
        }
    }

    private fun getLevelTitle(xp: Int): String {
        return when {
            xp >= 2000 -> "Diamond"
            xp >= 1000 -> "Platinum"
            xp >= 300 -> "Gold"
            xp >= 100 -> "Silver"
            else -> "Bronze"
        }
    }
}

data class QuizResultOutcome(
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val xpEarned: Int,
    val streak: Int,
    val newLevel: String,
    val newlyEarnedBadges: List<String>,
    val isPerfect: Boolean
)

