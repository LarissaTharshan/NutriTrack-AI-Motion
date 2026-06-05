package fhnw.emoba.nutritrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fhnw.emoba.nutritrack.ui.navigation.AppNavigation
import fhnw.emoba.nutritrack.ui.theme.NutriTrackAIMotionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NutriTrackAIMotionTheme(darkTheme = false) {
                AppNavigation()
            }
        }
    }
}
