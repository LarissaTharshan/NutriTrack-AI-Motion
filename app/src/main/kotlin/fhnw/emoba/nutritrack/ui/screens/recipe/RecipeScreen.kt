package fhnw.emoba.nutritrack.ui.screens.recipe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fhnw.emoba.nutritrack.domain.RecipeScalerUseCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeScreen(
    navController: NavController,
    viewModel: RecipeViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rezept-Skalierer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
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
                "Basierend auf deinem Protein-Ziel: ${viewModel.profile.dailyProteinGoal}g",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text("Rezept wählen:", fontWeight = FontWeight.Bold)

            RecipeScalerUseCase.HIGH_PROTEIN_RECIPES.keys.forEach { recipeName ->
                FilterChip(
                    selected = viewModel.selectedRecipe == recipeName,
                    onClick = { viewModel.selectRecipe(recipeName) },
                    label = { Text(recipeName) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            viewModel.scaledRecipe?.let { result ->
                HorizontalDivider()
                Text(
                    "Skalierte Zutaten (Faktor: ${"%.2f".format(result.scaleFactor)}x)",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "Gesamt Protein: ${"%.1f".format(result.totalProteinGrams)}g",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(result.ingredients) { ingredient ->
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    ingredient.name, fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    ingredient.displayAmount,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
