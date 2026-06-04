package fhnw.emoba.nutritrack

import fhnw.emoba.nutritrack.data.model.UserProfile
import fhnw.emoba.nutritrack.domain.RecipeScalerUseCase
import org.junit.Assert.*
import org.junit.Test

class NutriTrackUITest {

    @Test
    fun userProfile_defaultValues_areCorrect() {
        val profile = UserProfile()
        assertEquals(2000, profile.dailyCalorieGoal)
        assertEquals(150, profile.dailyProteinGoal)
        assertEquals(250, profile.dailyCarbGoal)
        assertEquals(65, profile.dailyFatGoal)
        assertEquals("", profile.name)
    }

    @Test
    fun userProfile_customValues_areSaved() {
        val profile = UserProfile(name = "Larissa", dailyProteinGoal = 120)
        assertEquals("Larissa", profile.name)
        assertEquals(120, profile.dailyProteinGoal)
    }

    @Test
    fun recipeScaler_allRecipesHaveIngredients() {
        RecipeScalerUseCase.HIGH_PROTEIN_RECIPES.forEach { (name, ingredients) ->
            assertTrue("$name has no ingredients", ingredients.isNotEmpty())
        }
    }

    @Test
    fun recipeScaler_threeRecipesAvailable() {
        assertEquals(3, RecipeScalerUseCase.HIGH_PROTEIN_RECIPES.size)
    }

    @Test
    fun recipeScaler_chickenBowlExists() {
        assertTrue(RecipeScalerUseCase.HIGH_PROTEIN_RECIPES.containsKey("Chicken Bowl"))
    }
}
