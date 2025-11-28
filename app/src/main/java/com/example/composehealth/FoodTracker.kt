package com.example.composehealth

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Relation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

@Entity(
    tableName = "food_tracker",
    foreignKeys = [
        ForeignKey(
            entity = FoodNutrition::class,
            parentColumns = arrayOf("name"),
            childColumns = arrayOf("foodName")
        )
    ]
)
data class FoodTracker(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val foodName: String,
    val foodWeight: Float,
    val date: String,
    val mealName: String,
)

data class FoodTrackerWithNutrition(
    @Embedded val foodTracker: FoodTracker,
    @Relation(
        parentColumn = "foodName",
        entityColumn = "name"
    )
    val nutrition: FoodNutrition
)

@Dao
interface FoodTrackerDao {
    @Query("""
        SELECT FT.*, FN.protein, FN.fat, FN.carbs
        FROM food_tracker FT
        LEFT JOIN food_nutrition_facts FN ON FT.foodName = FN.name
        WHERE (FT.date = :date) AND (FT.mealName = :mealName)
    """)
    fun getMealByDateWithNutrition(mealName: String, date: String): Flow<List<FoodTrackerWithNutrition>>

    @Query("""
        SELECT FT.*, FN.protein, FN.fat, FN.carbs
        FROM food_tracker FT
        LEFT JOIN food_nutrition_facts FN ON FT.foodName = FN.name
    """)
    fun getAllWithNutrition(): Flow<List<FoodTrackerWithNutrition>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: FoodTracker)

    @Delete
    suspend fun delete(entry: FoodTracker)
}