package com.example.composehealth

import android.content.Context
import android.health.connect.datatypes.ExercisePerformanceGoal
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_data")

class UserDataStoreManager(val context: Context) {
    suspend fun savePersonalData(personalData: PersonalData) {
        context.dataStore.edit { pref ->
            pref[floatPreferencesKey("weight")] = personalData.weight
            pref[intPreferencesKey("height")] = personalData.height
            pref[stringPreferencesKey("activity_level")] = personalData.activityLevel.name
            pref[stringPreferencesKey("weight_goal")] = personalData.weightGoal.name
            pref[intPreferencesKey("age")] = personalData.age
            pref[stringPreferencesKey("sex")] = personalData.sex.name
        }
    }

    suspend fun saveWeight(weight: Float) {
        context.dataStore.edit { pref ->
            pref[floatPreferencesKey("weight")] = weight
            Log.e("BBB", "saving weight $weight")
        }
    }

    suspend fun saveHeight(height: Int) {
        context.dataStore.edit { pref ->
            pref[intPreferencesKey("height")] = height
        }
    }

    suspend fun saveActivityLevel(activityLevel: ActivityLevel) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey("activity_level")] = activityLevel.name
        }
    }

    suspend fun saveWeightGoal(weightGoal: WeightGoal) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey("weight_goal")] = weightGoal.name
        }
    }

    suspend fun saveAge(age: Int) {
        context.dataStore.edit { pref ->
            pref[intPreferencesKey("age")] = age
        }
    }

    suspend fun saveSex(sex: Sex) {
        context.dataStore.edit { pref ->
            pref[stringPreferencesKey("sex")] = sex.name
        }
    }


    fun getPersonalData() = context.dataStore.data.map { pref ->
        val activityLevelName = pref[stringPreferencesKey("activity_level")] ?: ActivityLevel.Moderate.name
        val weightGoalName = pref[stringPreferencesKey("weight_goal")] ?: WeightGoal.Maintain.name
        val sexName = pref[stringPreferencesKey("sex")] ?: Sex.Male.name

        return@map PersonalData(
            pref[floatPreferencesKey("weight")] ?: 79f,
            pref[intPreferencesKey("height")] ?: 171,
            ActivityLevel.valueOf(activityLevelName),
            WeightGoal.valueOf(weightGoalName),
            pref[intPreferencesKey("age")] ?: 18,
            Sex.valueOf(sexName),
        )
    }
}

data class Macronutrients(
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val calories: Float,
) {
    companion object {
        fun fromFoodNutrition(foodNutrition: FoodNutrition): Macronutrients
        {
            return Macronutrients(
                foodNutrition.protein,
                foodNutrition.fat,
                foodNutrition.carbs,
                4 * (foodNutrition.protein + foodNutrition.carbs) + 9 * foodNutrition.fat
            )
        }
    }
}

fun calculateDailyMacronutrientIntake(personalData: PersonalData): Macronutrients {
    val bmr = calculateBMR(personalData)
    val tdee = bmr * personalData.activityLevel.multiplier

    // 1100 is approximate calorie surplus needed to gain 1 kg of weight per week
    val dailyCalories = tdee + personalData.weightGoal.perWeekGain * 1100

    return Macronutrients(
        dailyCalories * .25f / 4f,
        dailyCalories * .25f / 9f,
        dailyCalories * .50f / 4f,
        dailyCalories,
    )
}

fun calculateBMR(personalData: PersonalData): Float {
    val sexCorrection = if (personalData.sex == Sex.Male) 5f else -161f
    return 10f*personalData.weight + 6.25f*personalData.height + sexCorrection
}

data class PersonalData(
    val weight: Float,      // kilograms
    val height: Int,        // centimeters
    val activityLevel: ActivityLevel,
    val weightGoal: WeightGoal,
    val age: Int,
    val sex: Sex,
)

enum class ActivityLevel(val desc: String, val multiplier: Float) {
    BMR("Основной обмен веществ", 1f),
    Sedentary("Сидячий", 1.2f),
    Light("Низкий", 1.375f),
    Moderate("Умеренный", 1.55f),
    Active("Высокий", 1.725f),
    VeryActive("Очень высокий", 1.9f);
}

enum class WeightGoal(val desc: String, val perWeekGain: Float) {
    Maintain("Поддержание веса", 0f),

    MildLose("Потеря веса (0.25 кг / нед.)", -.25f),
    Lose("Потеря веса (0.5 кг / нед.)", -.5f),
    ExtremeLose("Потеря веса (1 кг / нед.)", -1f),

    MildGain("Набор веса (0.25 кг / нед.)", .25f),
    Gain("Набор веса (0.5 кг / нед.)", .5f),
    ExtremeGain("Набор веса (1 кг / нед.)", 1f);
}

enum class Sex(val desc: String) {
    Male("Мужской"), Female("Женский");
}