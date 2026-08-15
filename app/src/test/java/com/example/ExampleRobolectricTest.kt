package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.QuizRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var database: QuizDatabase
  private lateinit var quizDao: QuizDao
  private lateinit var repository: QuizRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    database = Room.inMemoryDatabaseBuilder(context, QuizDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    quizDao = database.quizDao()
    repository = QuizRepository(quizDao)
  }

  @After
  fun teardown() {
    database.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("QuizWhiz", appName)
  }

  @Test
  fun `seed database and retrieve questions`() = runBlocking {
    repository.seedDatabaseIfNeeded()

    val targetQuiz = QuizPreloadData.defaultQuizzes.first()
    // Retrieve questions for quiz
    val mathQuestions = repository.getQuestionsForQuiz(targetQuiz.id)
    assertTrue("Should retrieve questions for math quiz", mathQuestions.isNotEmpty())
    assertEquals(targetQuiz.questions.size, mathQuestions.size)

    // Check first question options and correct answer
    val firstQ = mathQuestions[0]
    assertNotNull(firstQ.text)
    assertEquals(4, firstQ.options.size)
    assertTrue(firstQ.correctOptionIndex in 0..3)
  }

  @Test
  fun `record quiz result updates student profile and saves attempt`() = runBlocking {
    repository.seedDatabaseIfNeeded()

    val quiz = QuizPreloadData.defaultQuizzes.first()
    val outcome = repository.recordQuizResult(
      quiz = quiz,
      score = 5,
      totalQuestions = 5,
      timeTakenSeconds = 45
    )

    assertTrue("Outcome should be marked perfect", outcome.isPerfect)
    assertEquals(100, outcome.percentage)
    assertTrue("XP earned should be > 0", outcome.xpEarned > 0)

    val attemptList = quizDao.getAllAttempts().first()
    assertTrue("Attempts should be recorded in Room", attemptList.isNotEmpty())
    assertEquals(quiz.title, attemptList[0].quizTitle)

    val profile = quizDao.getUserProfile()
    assertNotNull(profile)
    assertEquals(7, profile?.streakDays) // 6 initial + 1
  }
}

