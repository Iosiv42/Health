package com.example.composehealth

import androidx.lifecycle.ViewModel
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "food_nutrition_facts")
data class FoodNutrition(
    @PrimaryKey()
    val name: String,
    val protein: Float,    /// per 100 grams
    val fat: Float,    /// per 100 grams
    val carbs: Float,    /// per 100 grams
)

@Dao
interface FoodNutritionDao {
    @Query("SELECT * FROM food_nutrition_facts")
    fun getAllFoodNutrition(): Flow<List<FoodNutrition>>

    @Query("SELECT * FROM food_nutrition_facts WHERE name = :name")
    fun getByName(name: String): Flow<FoodNutrition>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(food: FoodNutrition)

    @Delete
    suspend fun delete(food: FoodNutrition)
}