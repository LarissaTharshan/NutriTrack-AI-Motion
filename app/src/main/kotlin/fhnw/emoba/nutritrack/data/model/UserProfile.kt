package fhnw.emoba.nutritrack.data.model

data class UserProfile(
    val name: String = "",
    val avatarUri: String = "",
    val dailyCalorieGoal: Int = 2000,
    val dailyProteinGoal: Int = 150,
    val dailyCarbGoal: Int = 250,
    val dailyFatGoal: Int = 65
)
