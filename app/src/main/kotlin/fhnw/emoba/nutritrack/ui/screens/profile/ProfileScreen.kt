package fhnw.emoba.nutritrack.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fhnw.emoba.nutritrack.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val avatarLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> uri?.let { viewModel.onAvatarSelected(it) } }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mein Profil", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { avatarLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.profile.avatarUri.isNotBlank()) {
                    AsyncImage(
                        model = viewModel.profile.avatarUri,
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(Icons.Default.Person, null, modifier = Modifier.size(48.dp))
                }
            }
            Text("Tippe um Bild zu waehlen", style = MaterialTheme.typography.bodySmall)

            OutlinedTextField(
                value = viewModel.nameInput,
                onValueChange = { if (it.length <= 30) viewModel.nameInput = it },
                label = { Text("Dein Name") },
                singleLine = true,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider()
            Text(
                "Tagesziele", fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            GoalTextField("Kalorien (kcal)", viewModel.calorieInput) { viewModel.calorieInput = it }
            GoalTextField("Protein (g)", viewModel.proteinInput) { viewModel.proteinInput = it }
            GoalTextField("Kohlenhydrate (g)", viewModel.carbInput) { viewModel.carbInput = it }
            GoalTextField("Fett (g)", viewModel.fatInput) { viewModel.fatInput = it }

            if (viewModel.saveSuccess) {
                Text("Gespeichert!", color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = { viewModel.saveSuccess = false; viewModel.saveProfile() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !viewModel.isSaving
            ) {
                if (viewModel.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Speichern & Weiter", fontWeight = FontWeight.Bold)
                }
            }

            OutlinedButton(
                onClick = { navController.navigate(Screen.Dashboard.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Zum Dashboard")
            }
        }
    }
}

@Composable
private fun GoalTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.length <= 5) onValueChange(it) },
        label = { Text(label) },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}
