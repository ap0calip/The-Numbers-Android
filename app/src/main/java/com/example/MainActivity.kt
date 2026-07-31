package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.screens.MainMathScreen
import com.example.ui.theme.AppOrange
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MathViewModel

class MainActivity : ComponentActivity() {
  private val viewModel: MathViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(
          modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
          color = AppOrange
        ) {
          MainMathScreen(viewModel = viewModel)
        }
      }
    }
  }
}

