package fhnw.emoba.nutritrack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fhnw.emoba.nutritrack.ui.screens.dashboard.DashboardScreen
import fhnw.emoba.nutritrack.ui.screens.profile.ProfileScreen
import fhnw.emoba.nutritrack.ui.screens.search.SearchScreen
import fhnw.emoba.nutritrack.ui.screens.recipe.RecipeScreen

sealed class Screen(val route: String) {
    data object Profile : Screen("profile")
    data object Dashboard : Screen("dashboard")
    data object Search : Screen("search")
    data object Recipe : Screen("recipe")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Profile.route
    ) {
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(navController = navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController = navController)
        }
        composable(Screen.Recipe.route) {
            RecipeScreen(navController = navController)
        }
    }
}
