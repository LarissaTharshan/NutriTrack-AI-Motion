package fhnw.emoba.nutritrack.domain

import fhnw.emoba.nutritrack.data.model.UserProfile

data class RecipeIngredient(
    val name: String,
    val baseAmountGrams: Double,
    val proteinPer100g: Double
)

data class ScaledIngredient(
    val name: String,
    val scaledAmountGrams: Double,
    val displayAmount: String
)

data class ScaledRecipe(
    val ingredients: List<ScaledIngredient>,
    val totalProteinGrams: Double,
    val scaleFactor: Double
)

class RecipeScalerUseCase {

    companion object {
        val HIGH_PROTEIN_RECIPES = mapOf(
            "Chicken Bowl" to listOf(
                RecipeIngredient("Hühnerbrust", 150.0, 31.0),
                RecipeIngredient("Quinoa (gekocht)", 100.0, 4.4),
                RecipeIngredient("Griechischer Joghurt", 80.0, 10.0),
                RecipeIngredient("Kichererbsen", 60.0, 9.0)
            ),
            "Protein Shake" to listOf(
                RecipeIngredient("Whey Protein", 30.0, 80.0),
                RecipeIngredient("Magerquark", 200.0, 12.0),
                RecipeIngredient("Haferflocken", 50.0, 13.0)
            ),
            "Lachs Bowl" to listOf(
                RecipeIngredient("Lachs", 130.0, 25.0),
                RecipeIngredient("Edamame", 80.0, 11.0),
                RecipeIngredient("Brauner Reis", 100.0, 2.6),
                RecipeIngredient("Eier", 60.0, 13.0)
            )
        )
    }

    fun scale(ingredients: List<RecipeIngredient>, profile: UserProfile): ScaledRecipe {
        val baseProtein = ingredients.sumOf { it.baseAmountGrams * it.proteinPer100g / 100.0 }
        val scaleFactor = if (baseProtein > 0) profile.dailyProteinGoal / baseProtein else 1.0

        val scaled = ingredients.map { ing ->
            val amount = ing.baseAmountGrams * scaleFactor
            ScaledIngredient(
                name = ing.name,
                scaledAmountGrams = amount,
                displayAmount = formatAmount(amount)
            )
        }

        return ScaledRecipe(
            ingredients = scaled,
            totalProteinGrams = profile.dailyProteinGoal.toDouble(),
            scaleFactor = scaleFactor
        )
    }

    private fun formatAmount(grams: Double): String = when {
        grams >= 1000 -> "%.2f kg".format(grams / 1000.0)
        grams < 5 -> "%.1f g".format(grams)
        else -> "%.0f g".format(grams)
    }
}
