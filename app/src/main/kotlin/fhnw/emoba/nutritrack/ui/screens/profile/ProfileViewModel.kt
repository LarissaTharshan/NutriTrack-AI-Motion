package fhnw.emoba.nutritrack.ui.screens.profile

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fhnw.emoba.nutritrack.data.local.UserPreferencesRepository
import fhnw.emoba.nutritrack.data.model.UserProfile
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    var profile by mutableStateOf(UserProfile())
        private set

    var nameInput by mutableStateOf("")
    var calorieInput by mutableStateOf("2000")
    var proteinInput by mutableStateOf("150")
    var carbInput by mutableStateOf("250")
    var fatInput by mutableStateOf("65")
    var isSaving by mutableStateOf(false)
    var saveSuccess by mutableStateOf(false)

    init {
        viewModelScope.launch {
            repository.userProfile.collect { saved ->
                profile = saved
                nameInput = saved.name
                calorieInput = saved.dailyCalorieGoal.toString()
                proteinInput = saved.dailyProteinGoal.toString()
                carbInput = saved.dailyCarbGoal.toString()
                fatInput = saved.dailyFatGoal.toString()
            }
        }
    }

    fun saveProfile() {
        viewModelScope.launch {
            isSaving = true
            repository.saveProfile(
                UserProfile(
                    name = nameInput.trim(),
                    avatarUri = profile.avatarUri,
                    dailyCalorieGoal = calorieInput.toIntOrNull() ?: 2000,
                    dailyProteinGoal = proteinInput.toIntOrNull() ?: 150,
                    dailyCarbGoal = carbInput.toIntOrNull() ?: 250,
                    dailyFatGoal = fatInput.toIntOrNull() ?: 65
                )
            )
            isSaving = false
            saveSuccess = true
        }
    }

    fun onAvatarSelected(uri: Uri) {
        viewModelScope.launch {
            repository.saveProfile(profile.copy(avatarUri = uri.toString()))
        }
    }
}
