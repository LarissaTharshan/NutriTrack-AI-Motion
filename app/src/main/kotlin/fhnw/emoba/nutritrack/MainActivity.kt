package fhnw.emoba.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import fhnw.emoba.nutritrack.ui.navigation.AppNavigation
import fhnw.emoba.nutritrack.ui.theme.NutriTrackAIMotionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkMode by remember { mutableStateOf(false) }
            NutriTrackAIMotionTheme(darkTheme = darkMode) {
                AppNavigation(
                    darkMode = darkMode,
                    onToggleDarkMode = { darkMode = !darkMode }
                )
            }
        }
    }
}
