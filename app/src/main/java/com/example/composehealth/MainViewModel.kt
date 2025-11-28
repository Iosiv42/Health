package com.example.composehealth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class MainViewModel(database: AppDatabase): ViewModel() {
    val currentDate = MutableStateFlow(LocalDate.now())
    private val foodTrackerDao = database.foodTrackerDao
    private val foodNutritionDao = database.foodNutritionDao

    @OptIn(ExperimentalCoroutinesApi::class)
    fun currentMealWithNutrition(mealName: String): Flow<List<FoodTrackerWithNutrition>> {
        return currentDate.flatMapLatest {date ->
            foodTrackerDao.getMealByDateWithNutrition(mealName, date.toString())
        }
    }

    fun allWithNutrition(): Flow<List<FoodTrackerWithNutrition>> {
        return foodTrackerDao.getAllWithNutrition()
    }

    fun addFoodNutrition(foodNutrition: FoodNutrition) {
        viewModelScope.launch {
            foodNutritionDao.insert(foodNutrition)
        }
    }

    fun addFoodTrack(foodTrack: FoodTracker) {
        viewModelScope.launch {
            foodTrackerDao.insert(foodTrack)
        }
    }

    fun allFoodNutrition(): Flow<List<FoodNutrition>> {
        return foodNutritionDao.getAllFoodNutrition()
    }

    fun foodNutritionByName(foodName: String): Flow<FoodNutrition> {
        return foodNutritionDao.getByName(foodName)
    }

    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                Log.e("AAA", "Main view model created")
                return MainViewModel(database) as T
            }
        }
    }
}