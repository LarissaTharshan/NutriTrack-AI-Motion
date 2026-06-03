package fhnw.emoba.nutritrack.ui.screens.dashboard

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fhnw.emoba.nutritrack.data.local.UserPreferencesRepository
import fhnw.emoba.nutritrack.data.model.UserProfile
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    var profile by mutableStateOf(UserProfile())
        private set

    init {
        viewModelScope.launch {
            repository.userProfile.collect { saved ->
                profile = saved
            }
        }
    }
}
