package fhnw.emoba.nutritrack

import fhnw.emoba.nutritrack.data.model.UserProfile
import fhnw.emoba.nutritrack.domain.RecipeIngredient
import fhnw.emoba.nutritrack.domain.RecipeScalerUseCase
import org.junit.Assert.*
import org.junit.Test

class RecipeScalerTest {

    private val scaler = RecipeScalerUseCase()

    @Test
    fun scaleFactor_isCalculatedCorrectly() {
        val ingredients = listOf(RecipeIngredient("Hühnerbrust", 100.0, 31.0))
        val profile = UserProfile(dailyProteinGoal = 62)
        val result = scaler.scale(ingredients, profile)
        assertEquals(2.0, result.scaleFactor, 0.01)
    }

    @Test
    fun scaledAmount_doublesWhenScaleFactorIsTwo() {
        val ingredients = listOf(RecipeIngredient("Hühnerbrust", 100.0, 31.0))
        val profile = UserProfile(dailyProteinGoal = 62)
        val result = scaler.scale(ingredients, profile)
        assertEquals(200.0, result.ingredients[0].scaledAmountGrams, 0.01)
    }

    @Test
    fun totalProtein_matchesUserGoal() {
        val ingredients = listOf(RecipeIngredient("Hühnerbrust", 100.0, 31.0))
        val profile = UserProfile(dailyProteinGoal = 150)
        val result = scaler.scale(ingredients, profile)
        assertEquals(150.0, result.totalProteinGrams, 0.01)
    }

    @Test
    fun formatAmount_showsKgForLargeValues() {
        val ingredients = listOf(RecipeIngredient("Reis", 100.0, 2.0))
        val profile = UserProfile(dailyProteinGoal = 300)
        val result = scaler.scale(ingredients, profile)
        assertTrue(result.ingredients[0].displayAmount.contains("kg"))
    }

    @Test
    fun allRecipes_areScalable() {
        val profile = UserProfile(dailyProteinGoal = 150)
        RecipeScalerUseCase.HIGH_PROTEIN_RECIPES.forEach { (_, ingredients) ->
            val result = scaler.scale(ingredients, profile)
            assertTrue(result.scaleFactor > 0)
            assertTrue(result.ingredients.isNotEmpty())
        }
    }

    @Test
    fun scaleFactorIsOne_whenGoalMatchesBaseProtein() {
        val ingredients = listOf(RecipeIngredient("Lachs", 100.0, 25.0))
        val profile = UserProfile(dailyProteinGoal = 25)
        val result = scaler.scale(ingredients, profile)
        assertEquals(1.0, result.scaleFactor, 0.01)
    }

    @Test
    fun emptyIngredients_returnsEmptyResult() {
        val profile = UserProfile(dailyProteinGoal = 150)
        val result = scaler.scale(emptyList(), profile)
        assertTrue(result.ingredients.isEmpty())
    }

    @Test
    fun displayAmount_showsGramsForNormalValues() {
        val ingredients = listOf(RecipeIngredient("Quark", 200.0, 12.0))
        val profile = UserProfile(dailyProteinGoal = 24)
        val result = scaler.scale(ingredients, profile)
        assertTrue(result.ingredients[0].displayAmount.contains("g"))
    }
}
