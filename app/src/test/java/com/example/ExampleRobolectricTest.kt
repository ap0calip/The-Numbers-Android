package com.example

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.ui.viewmodel.AppMode
import com.example.ui.viewmodel.MathOperator
import com.example.ui.viewmodel.MathViewModel
import com.example.ui.viewmodel.SelectedField
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("The Numbers", appName)
  }

  @Test
  fun `calculator mode does not show answer until enter is pressed`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = MathViewModel(app)

    // Initial state: Calculator mode, isEntered should be false
    assertEquals(AppMode.CALCULATOR, viewModel.uiState.value.mode)
    assertFalse(viewModel.uiState.value.isEntered)

    // Select Term1 and type 5
    viewModel.selectField(SelectedField.TERM1)
    viewModel.onDigitPressed(5)
    assertFalse(viewModel.uiState.value.isEntered)
    assertEquals("5", viewModel.uiState.value.term1)

    // Select Term2 and type 7
    viewModel.selectField(SelectedField.TERM2)
    viewModel.onDigitPressed(7)
    assertFalse(viewModel.uiState.value.isEntered)
    assertEquals("7", viewModel.uiState.value.term2)

    // Press Enter -> Answer should now be displayed
    viewModel.onEnterPressed()
    assertTrue(viewModel.uiState.value.isEntered)
    assertEquals(12, viewModel.uiState.value.calculatedSum)

    // Change operator to Multiply -> isEntered becomes false again
    viewModel.selectOperator(MathOperator.MULTIPLY)
    assertFalse(viewModel.uiState.value.isEntered)

    // Press Enter again -> isEntered is true, 5 * 7 = 35
    viewModel.onEnterPressed()
    assertTrue(viewModel.uiState.value.isEntered)
    assertEquals(35, viewModel.uiState.value.calculatedSum)

    // Pressing a new digit after enter resets isEntered to false
    viewModel.onDigitPressed(4)
    assertFalse(viewModel.uiState.value.isEntered)
  }

  @Test
  fun `free practice enter with blank answer triggers empty answer shake`() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val viewModel = MathViewModel(app)

    viewModel.setAppMode(AppMode.FREE_PRACTICE)
    assertEquals(AppMode.FREE_PRACTICE, viewModel.uiState.value.mode)
    assertEquals("", viewModel.uiState.value.quizUserAnswer)
    assertEquals(0L, viewModel.uiState.value.emptyAnswerShakeTrigger)

    // Press enter without entering any digits
    viewModel.onEnterPressed()

    // Shake trigger should be updated to a positive timestamp
    assertTrue(viewModel.uiState.value.emptyAnswerShakeTrigger > 0L)
    // Answer is still empty and no incorrect penalty or feedback
    assertEquals("", viewModel.uiState.value.quizUserAnswer)
  }
}

