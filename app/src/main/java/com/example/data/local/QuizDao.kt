package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {

    // ==================== CATEGORIES ====================
    @Query("SELECT * FROM quiz_categories")
    fun getAllCategories(): Flow<List<QuizCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<QuizCategoryEntity>)

    // ==================== QUIZZES ====================
    @Query("SELECT * FROM quizzes")
    fun getAllQuizzes(): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quizzes WHERE category = :category")
    fun getQuizzesByCategory(category: String): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quizzes WHERE (:category = 'ALL' OR category = :category) AND (:board = 'ALL' OR board = :board) AND (:grade = 'ALL' OR grade = :grade)")
    fun getQuizzesByFilters(category: String, board: String, grade: String): Flow<List<QuizEntity>>

    @Query("SELECT * FROM quizzes WHERE id = :quizId LIMIT 1")
    fun getQuizById(quizId: String): Flow<QuizEntity?>

    @Query("SELECT * FROM quizzes WHERE id = :quizId LIMIT 1")
    suspend fun getQuizByIdSync(quizId: String): QuizEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizzes(quizzes: List<QuizEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)

    // ==================== QUESTIONS & QUESTION RETRIEVAL ====================
    @Query("SELECT * FROM quiz_questions WHERE quizId = :quizId")
    fun getQuestionsForQuizFlow(quizId: String): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quiz_questions WHERE quizId = :quizId")
    suspend fun getQuestionsForQuiz(quizId: String): List<QuizQuestionEntity>

    @Query("SELECT * FROM quiz_questions WHERE category = :category")
    fun getQuestionsByCategory(category: String): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quiz_questions WHERE board = :board AND grade = :grade")
    fun getQuestionsByBoardAndGrade(board: String, grade: String): Flow<List<QuizQuestionEntity>>

    @Query("SELECT * FROM quiz_questions WHERE id = :questionId LIMIT 1")
    suspend fun getQuestionById(questionId: String): QuizQuestionEntity?

    @Query("SELECT COUNT(*) FROM quiz_questions")
    suspend fun getQuestionsCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuizQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuizQuestionEntity)

    // ==================== USER PROFILE & STUDENT PROGRESS ====================
    @Query("SELECT * FROM user_profile WHERE id = 'user_main' LIMIT 1")
    fun getUserProfileFlow(): Flow<UserProfileEntity?>

    @Query("SELECT * FROM user_profile WHERE id = 'user_main' LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: UserProfileEntity)

    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<QuizAttemptEntity>>

    @Query("SELECT * FROM quiz_attempts WHERE quizId = :quizId ORDER BY timestamp DESC")
    fun getAttemptsByQuizId(quizId: String): Flow<List<QuizAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttemptEntity)

    // ==================== CUSTOM CHALLENGES ====================
    @Query("SELECT * FROM custom_challenges ORDER BY timestamp DESC")
    fun getCustomChallenges(): Flow<List<CustomChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomChallenge(challenge: CustomChallengeEntity)
}

