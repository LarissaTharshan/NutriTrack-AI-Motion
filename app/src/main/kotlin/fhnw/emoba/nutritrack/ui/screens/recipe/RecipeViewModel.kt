package fhnw.emoba.nutritrack.ui.screens.recipe

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fhnw.emoba.nutritrack.data.local.UserPreferencesRepository
import fhnw.emoba.nutritrack.data.model.UserProfile
import fhnw.emoba.nutritrack.domain.RecipeScalerUseCase
import fhnw.emoba.nutritrack.domain.ScaledRecipe
import kotlinx.coroutines.launch

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)
    private val scaler = RecipeScalerUseCase()

    var profile by mutableStateOf(UserProfile())
        private set
    var selectedRecipe by mutableStateOf<String?>(null)
    var scaledRecipe by mutableStateOf<ScaledRecipe?>(null)

    init {
        viewModelScope.launch {
            repository.userProfile.collect { saved ->
                profile = saved
                selectedRecipe?.let { selectRecipe(it) }
            }
        }
    }

    fun selectRecipe(name: String) {
        selectedRecipe = name
        val ingredients = RecipeScalerUseCase.HIGH_PROTEIN_RECIPES[name] ?: return
        scaledRecipe = scaler.scale(ingredients, profile)
    }
}
