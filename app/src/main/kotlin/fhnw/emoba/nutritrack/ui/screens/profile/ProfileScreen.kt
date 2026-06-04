package fhnw.emoba.nutritrack.ui.screens.profile

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fhnw.emoba.nutritrack.ui.navigation.Screen
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current

    val avatarLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            val file = saveBitmapToFile(context, it)
            val uri = Uri.fromFile(file)
            viewModel.onAvatarSelected(uri)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) avatarLauncher.launch(null)
    }

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
                    .clickable {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    },
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
            Text(
                "Tippe für Kamera-Foto",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                "Oder wähle einen Avatar:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(viewModel.availableAvatars) { avatarName ->
                    val resId = context.resources.getIdentifier(
                        avatarName, "drawable", context.packageName
                    )
                    val isSelected = viewModel.profile.avatarUri.contains(avatarName)
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.outline,
                                shape = CircleShape
                            )
                            .clickable {
                                val uri = Uri.parse(
                                    "android.resource://${context.packageName}/$resId"
                                )
                                viewModel.onAvatarSelected(uri)
                            }
                    ) {
                        AsyncImage(
                            model = resId,
                            contentDescription = avatarName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

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
                Text(
                    "Gespeichert!", color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
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

private fun saveBitmapToFile(context: Context, bitmap: Bitmap): File {
    val file = File(context.cacheDir, "avatar_${System.currentTimeMillis()}.jpg")
    val out = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
    out.flush()
    out.close()
    return file
}
