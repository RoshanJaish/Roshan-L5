package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.QuizAttemptEntity
import com.example.data.local.QuizCategoryEntity
import com.example.data.local.QuizDatabase
import com.example.data.model.*
import com.example.data.repository.QuizRepository
import com.example.data.repository.QuizResultOutcome
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ActiveQuizState(
    val quiz: Quiz,
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, Int> = emptyMap(), // questionIndex -> optionIndex
    val secondsRemaining: Int = 180,
    val isFinished: Boolean = false,
    val resultOutcome: QuizResultOutcome? = null
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {
    private val database = QuizDatabase.getDatabase(application)
    val repository = QuizRepository(database.quizDao())

    val userProfile: StateFlow<UserProfile> = repository.userProfileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    val quizAttempts: StateFlow<List<QuizAttemptEntity>> = repository.quizAttemptsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val categories: StateFlow<List<QuizCategoryEntity>> = repository.categoriesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedCategory = MutableStateFlow(SubjectCategory.ALL)
    val selectedCategory: StateFlow<SubjectCategory> = _selectedCategory.asStateFlow()

    private val _selectedBoard = MutableStateFlow(BoardType.CBSE)
    val selectedBoard: StateFlow<BoardType> = _selectedBoard.asStateFlow()

    private val _selectedGrade = MutableStateFlow(GradeLevel.GRADE_8)
    val selectedGrade: StateFlow<GradeLevel> = _selectedGrade.asStateFlow()

    // Reactive Flow of filtered quizzes combining filter states
    val filteredQuizzes: StateFlow<List<Quiz>> = combine(
        _selectedCategory,
        _selectedBoard,
        _selectedGrade
    ) { category, board, grade ->
        repository.getQuizzesForFilters(category, board, grade)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.getQuizzesForFilters(SubjectCategory.ALL, BoardType.CBSE, GradeLevel.GRADE_8)
    )

    private val _activeQuizState = MutableStateFlow<ActiveQuizState?>(null)
    val activeQuizState: StateFlow<ActiveQuizState?> = _activeQuizState.asStateFlow()

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.seedDatabaseIfNeeded()
            // Sync initial board & grade with profile
            userProfile.collect { profile ->
                _selectedBoard.value = profile.board
                _selectedGrade.value = profile.grade
            }
        }
    }

    // ==================== QUESTION RETRIEVAL ====================

    /**
     * Retrieves questions for a specific quiz via reactive Flow from Room.
     */
    fun getQuestionsForQuizFlow(quizId: String): Flow<List<QuizQuestion>> {
        return repository.getQuestionsForQuizFlow(quizId)
    }

    /**
     * Retrieves questions for a subject category via reactive Flow from Room.
     */
    fun retrieveQuestionsForCategory(category: SubjectCategory): Flow<List<QuizQuestion>> {
        return repository.getQuestionsByCategory(category)
    }

    /**
     * Retrieves questions for a specific board and grade level from Room.
     */
    fun retrieveQuestionsForBoardAndGrade(board: BoardType, grade: GradeLevel): Flow<List<QuizQuestion>> {
        return repository.getQuestionsByBoardAndGrade(board, grade)
    }

    /**
     * Asynchronously loads a quiz with its questions from the Room database.
     */
    fun loadQuizWithQuestions(quizId: String, onLoaded: (Quiz?) -> Unit) {
        viewModelScope.launch {
            val quiz = repository.getFullQuizWithQuestions(quizId)
            onLoaded(quiz)
        }
    }

    // ==================== FILTERING & ACTIONS ====================

    fun selectCategory(category: SubjectCategory) {
        _selectedCategory.value = category
    }

    fun selectBoard(board: BoardType) {
        _selectedBoard.value = board
        viewModelScope.launch {
            repository.updatePreferences(board = board, grade = _selectedGrade.value)
        }
    }

    fun selectGrade(grade: GradeLevel) {
        _selectedGrade.value = grade
        viewModelScope.launch {
            repository.updatePreferences(board = _selectedBoard.value, grade = grade)
        }
    }

    fun updateProfile(name: String, board: BoardType, grade: GradeLevel) {
        viewModelScope.launch {
            repository.updatePreferences(board = board, grade = grade, name = name)
            _selectedBoard.value = board
            _selectedGrade.value = grade
        }
    }

    fun startQuiz(quiz: Quiz) {
        timerJob?.cancel()

        viewModelScope.launch {
            // Retrieve latest questions from Room if quiz questions list is empty
            val resolvedQuiz = if (quiz.questions.isEmpty()) {
                val questions = repository.getQuestionsForQuiz(quiz.id)
                quiz.copy(questions = questions)
            } else {
                quiz
            }

            val totalSeconds = resolvedQuiz.durationMinutes * 60
            _activeQuizState.value = ActiveQuizState(
                quiz = resolvedQuiz,
                currentQuestionIndex = 0,
                selectedAnswers = emptyMap(),
                secondsRemaining = totalSeconds,
                isFinished = false,
                resultOutcome = null
            )

            // Start countdown timer
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    val current = _activeQuizState.value ?: break
                    if (current.isFinished) break

                    val remaining = current.secondsRemaining - 1
                    if (remaining <= 0) {
                        _activeQuizState.value = current.copy(secondsRemaining = 0)
                        submitActiveQuiz()
                        break
                    } else {
                        _activeQuizState.value = current.copy(secondsRemaining = remaining)
                    }
                }
            }
        }
    }

    fun selectOption(questionIndex: Int, optionIndex: Int) {
        val current = _activeQuizState.value ?: return
        if (current.isFinished) return
        val newAnswers = current.selectedAnswers.toMutableMap()
        newAnswers[questionIndex] = optionIndex
        _activeQuizState.value = current.copy(selectedAnswers = newAnswers)
    }

    fun goToQuestion(index: Int) {
        val current = _activeQuizState.value ?: return
        if (index in 0 until current.quiz.questions.size) {
            _activeQuizState.value = current.copy(currentQuestionIndex = index)
        }
    }

    fun nextQuestion() {
        val current = _activeQuizState.value ?: return
        if (current.currentQuestionIndex < current.quiz.questions.size - 1) {
            _activeQuizState.value = current.copy(currentQuestionIndex = current.currentQuestionIndex + 1)
        } else {
            // Last question, submit
            submitActiveQuiz()
        }
    }

    fun previousQuestion() {
        val current = _activeQuizState.value ?: return
        if (current.currentQuestionIndex > 0) {
            _activeQuizState.value = current.copy(currentQuestionIndex = current.currentQuestionIndex - 1)
        }
    }

    fun submitActiveQuiz() {
        timerJob?.cancel()
        val current = _activeQuizState.value ?: return
        if (current.isFinished) return

        var score = 0
        val questions = current.quiz.questions
        questions.forEachIndexed { index, question ->
            val selected = current.selectedAnswers[index]
            if (selected == question.correctOptionIndex) {
                score++
            }
        }

        val totalTime = current.quiz.durationMinutes * 60 - current.secondsRemaining

        viewModelScope.launch {
            val outcome = repository.recordQuizResult(
                quiz = current.quiz,
                score = score,
                totalQuestions = questions.size,
                timeTakenSeconds = maxOf(1, totalTime)
            )
            _activeQuizState.value = current.copy(
                isFinished = true,
                resultOutcome = outcome
            )
        }
    }

    fun exitQuiz() {
        timerJob?.cancel()
        _activeQuizState.value = null
    }

    fun createCustomChallenge(
        title: String,
        topic: String,
        subject: SubjectCategory,
        board: BoardType,
        grade: GradeLevel
    ) {
        viewModelScope.launch {
            // Trigger repository custom challenge insertion
        }
    }
}

