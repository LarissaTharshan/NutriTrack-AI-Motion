package fhnw.emoba.nutritrack

import fhnw.emoba.nutritrack.data.model.UserProfile
import fhnw.emoba.nutritrack.domain.RecipeScalerUseCase
import org.junit.Assert
import org.junit.Test

class NutriTrackUITest {

    @Test
    fun userProfile_defaultValues_areCorrect() {
        val profile = UserProfile()
        Assert.assertEquals(2000, profile.dailyCalorieGoal)
        Assert.assertEquals(150, profile.dailyProteinGoal)
        Assert.assertEquals(250, profile.dailyCarbGoal)
        Assert.assertEquals(65, profile.dailyFatGoal)
        Assert.assertEquals("", profile.name)
    }

    @Test
    fun userProfile_customValues_areSaved() {
        val profile = UserProfile(name = "Larissa", dailyProteinGoal = 120)
        Assert.assertEquals("Larissa", profile.name)
        Assert.assertEquals(120, profile.dailyProteinGoal)
    }

    @Test
    fun recipeScaler_allRecipesHaveIngredients() {
        RecipeScalerUseCase.Companion.HIGH_PROTEIN_RECIPES.forEach { (name, ingredients) ->
            Assert.assertTrue("$name has no ingredients", ingredients.isNotEmpty())
        }
    }

    @Test
    fun recipeScaler_threeRecipesAvailable() {
        Assert.assertEquals(3, RecipeScalerUseCase.Companion.HIGH_PROTEIN_RECIPES.size)
    }

    @Test
    fun recipeScaler_chickenBowlExists() {
        Assert.assertTrue(RecipeScalerUseCase.Companion.HIGH_PROTEIN_RECIPES.containsKey("Chicken Bowl"))
    }
}