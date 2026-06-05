package fhnw.emoba.nutritrack.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fhnw.emoba.nutritrack.ui.navigation.Screen
import fhnw.emoba.nutritrack.ui.theme.NutriTrackAIMotionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel()
) {
    val profile = viewModel.profile
    var darkMode by remember { mutableStateOf(false) }

    NutriTrackAIMotionTheme(darkTheme = darkMode) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (profile.name.isNotBlank()) "Hallo, ${profile.name}!"
                            else "NutriTrack",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = { darkMode = !darkMode }) {
                            Icon(
                                if (darkMode) Icons.Default.LightMode
                                else Icons.Default.DarkMode,
                                contentDescription = "Dark/Light Mode"
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Deine Tagesziele",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GoalCard("Kalorien", profile.dailyCalorieGoal, "kcal", Modifier.weight(1f))
                    GoalCard("Protein", profile.dailyProteinGoal, "g", Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GoalCard("Kohlenhydrate", profile.dailyCarbGoal, "g", Modifier.weight(1f))
                    GoalCard("Fett", profile.dailyFatGoal, "g", Modifier.weight(1f))
                }

                HorizontalDivider()

                Button(
                    onClick = { navController.navigate(Screen.Search.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Default.Search, null, modifier = Modifier.padding(end = 8.dp))
                    Text("Lebensmittel suchen", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { navController.navigate(Screen.Recipe.route) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Rezept-Skalierer", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { navController.navigate(Screen.Profile.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Person, null, modifier = Modifier.padding(end = 8.dp))
                    Text("Profil bearbeiten")
                }
            }
        }
    }
}

@Composable
private fun GoalCard(label: String, value: Int, unit: String, modifier: Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall)
            Text(
                "$value", fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(unit, style = MaterialTheme.typography.bodySmall)
        }
    }
}
