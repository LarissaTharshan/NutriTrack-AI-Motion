package fhnw.emoba.nutritrack.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import fhnw.emoba.nutritrack.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "user_profile")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        val KEY_NAME = stringPreferencesKey("name")
        val KEY_AVATAR_URI = stringPreferencesKey("avatar_uri")
        val KEY_CALORIE_GOAL = intPreferencesKey("calorie_goal")
        val KEY_PROTEIN_GOAL = intPreferencesKey("protein_goal")
        val KEY_CARB_GOAL = intPreferencesKey("carb_goal")
        val KEY_FAT_GOAL = intPreferencesKey("fat_goal")
    }

    val userProfile: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        UserProfile(
            name = prefs[KEY_NAME] ?: "",
            avatarUri = prefs[KEY_AVATAR_URI] ?: "",
            dailyCalorieGoal = prefs[KEY_CALORIE_GOAL] ?: 2000,
            dailyProteinGoal = prefs[KEY_PROTEIN_GOAL] ?: 150,
            dailyCarbGoal = prefs[KEY_CARB_GOAL] ?: 250,
            dailyFatGoal = prefs[KEY_FAT_GOAL] ?: 65
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = profile.name
            prefs[KEY_AVATAR_URI] = profile.avatarUri
            prefs[KEY_CALORIE_GOAL] = profile.dailyCalorieGoal
            prefs[KEY_PROTEIN_GOAL] = profile.dailyProteinGoal
            prefs[KEY_CARB_GOAL] = profile.dailyCarbGoal
            prefs[KEY_FAT_GOAL] = profile.dailyFatGoal
        }
    }
}
