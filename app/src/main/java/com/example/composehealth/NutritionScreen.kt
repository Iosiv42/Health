package com.example.composehealth

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composehealth.ui.theme.AppTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.sql.Date
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun NutritionScreen(
    mainViewModel: MainViewModel,
    userDataStoreManager: UserDataStoreManager,
    onNavigateToFoodSearcher: (mealName: String) -> Unit,
    onNavigatetoAddActivityTrack: () -> Unit,
) {
    var caloriesBurned by remember {
        mutableFloatStateOf(0f)
    }

    var dailyProteinTarget by remember {
        mutableIntStateOf(100)
    }
    var dailyFatTarget by remember {
        mutableIntStateOf(100)
    }
    var dailyCarbsTarget by remember {
        mutableIntStateOf(100)
    }
    var dailyCaloriesTarget by remember {
        mutableIntStateOf(2000)
    }

    LaunchedEffect(true) {
        userDataStoreManager.getPersonalData().collect { personalData ->
            val da = calculateDailyMacronutrientIntake(personalData)

            dailyProteinTarget = da.protein.toInt()
            dailyFatTarget = da.fat.toInt()
            dailyCarbsTarget = da.carbs.toInt()
            dailyCaloriesTarget = da.calories.toInt()
        }
    }

    val breakfastList = mainViewModel.currentMealWithNutrition("breakfast").collectAsState(initial = emptyList())
    val lunchList = mainViewModel.currentMealWithNutrition("lunch").collectAsState(initial = emptyList())
    val dinnerList = mainViewModel.currentMealWithNutrition("dinner").collectAsState(initial = emptyList())
    val snacksList = mainViewModel.currentMealWithNutrition("snacks").collectAsState(initial = emptyList())
    val meals = listOf(
        breakfastList,
        lunchList,
        dinnerList,
        snacksList,
    )

    val currentDateProtein = meals.sumOf {meal ->
        meal.value.sumOf { (it.nutrition.protein * it.foodTracker.foodWeight/100f).toDouble() }
    }.toInt()

    val currentDateFat = meals.sumOf {meal ->
        meal.value.sumOf { (it.nutrition.fat * it.foodTracker.foodWeight/100f).toDouble() }
    }.toInt()

    val currentDateCarbs = meals.sumOf {meal ->
        meal.value.sumOf { (it.nutrition.carbs * it.foodTracker.foodWeight/100f).toDouble() }
    }.toInt()

    val caloriesSupplied = 4*(currentDateCarbs + currentDateProtein) + 9*currentDateFat

    Log.e("AAA", "Composable created")

    mainViewModel.addFoodNutrition(
        FoodNutrition(
            "Гречневая каша", 4.2f, 2f, 18.8f
        )
    )

    mainViewModel.addFoodNutrition(
        FoodNutrition(
            "Перловая каша", 3f, 1f, 22.5f
        )
    )

    mainViewModel.addFoodNutrition(
        FoodNutrition(
            "Греческий йогурт Neo (персик)", 6.8f, 1.7f, 10.5f
        )
    )

    mainViewModel.addFoodNutrition(
        FoodNutrition(
            "Зеленый чай Lipton", 0f, 0f, 4.6f
        )
    )

    mainViewModel.addFoodNutrition(
        FoodNutrition(
            "Арахисовая паста", 28f, 48f, 10f
        )
    )

    mainViewModel.addFoodNutrition(
        FoodNutrition(
            "Подсолнечное масло", 0f, 99.9f, 0f
        )
    )

    mainViewModel.addFoodNutrition(
        FoodNutrition(
            "Куриная грудка", 23.6f, 1.9f, 0.4f
        )
    )

    mainViewModel.addFoodTrack(
        FoodTracker(
            1, "Гречневая каша", 300f,
            LocalDate.of(2025, 11, 25).toString(),
            "lunch"
        )
    )

    mainViewModel.addFoodTrack(
        FoodTracker(
            2, "Куриная грудка", 125f,
            LocalDate.of(2025, 11, 25).toString(),
            "lunch"
        )
    )

    mainViewModel.addFoodTrack(
        FoodTracker(
            3, "Греческий йогурт Neo (персик)", 125f,
            LocalDate.of(2025, 11, 25).toString(),
            "breakfast"
        )
    )

    mainViewModel.addFoodTrack(
        FoodTracker(
            4, "Гречневая каша", 350f,
            LocalDate.of(2025, 11, 25).toString(),
            "dinner"
        )
    )

    mainViewModel.addFoodTrack(
        FoodTracker(
            5, "Подсолнечное масло", 10f,
            LocalDate.of(2025, 11, 25).toString(),
            "dinner"
        )
    )

    mainViewModel.addFoodTrack(
        FoodTracker(
            6, "Куриная грудка", 125f,
            LocalDate.of(2025, 11, 25).toString(),
            "dinner"
        )
    )

//    da.value.elementAtOrNull(0)?.let {trackWithNutrition ->
//        Log.e("AAA", trackWithNutrition.foodTracker.date.atStartOfDay(ZoneOffset.systemDefault()).toEpochSecond().toString())
//    }

    Log.e("AAA", dailyCaloriesTarget.toString())
    Log.e("AAA", dailyProteinTarget.toString())
    Log.e("AAA", dailyFatTarget.toString())
    Log.e("AAA", dailyCarbsTarget.toString())

    Scaffold(modifier = Modifier.fillMaxSize()) {padding ->
        LazyColumn(
            modifier = Modifier


                .padding(20.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
//                    colors = CardDefaults.cardColors(
//                        containerColor = MaterialTheme.colorScheme.primaryContainer,
//                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            val w = 0.5f
                            val wo = (1 - w) / 2

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.KeyboardArrowUp,
                                    "Calories Supplied"
                                )
                                Text(caloriesSupplied.toString())
                                Text("получено")
                            }

                            ArcProgressbar(
                                calories = caloriesSupplied - caloriesBurned,
                                modifier = Modifier.fillMaxWidth(.6f),
                                dailyCaloriesTarget = dailyCaloriesTarget
                            )

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.KeyboardArrowDown,
                                    "Calories Burned"
                                )
                                Text(caloriesBurned.toInt().toString())
                                Text("сожжено")
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            MacronutrientProgress(
                                macronutrientName = "белков",
                                dailyTarget = dailyProteinTarget,
                                supplied = currentDateProtein
                            )
                            MacronutrientProgress(
                                macronutrientName = "жиров",
                                dailyTarget = dailyFatTarget,
                                supplied = currentDateFat
                            )
                            MacronutrientProgress(
                                macronutrientName = "углев.",
                                dailyTarget = dailyCarbsTarget,
                                supplied = currentDateCarbs
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            mainViewModel.currentDate.value = mainViewModel.currentDate.value.minusDays(1)
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            "prev day"
                        )
                    }

                    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                    val formattedDate = mainViewModel.currentDate.collectAsState().value.format(formatter)
                    Text(formattedDate, style = MaterialTheme.typography.titleLarge)

                    IconButton(
                        onClick = {
                            mainViewModel.currentDate.value = mainViewModel.currentDate.value.plusDays(1)
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            "next day"
                        )
                    }
                }

                Text("Активность", style = MaterialTheme.typography.headlineLarge)

                if (mainViewModel.currentDate.collectAsState().value == LocalDate.of(2025, 11, 28)) {
                    caloriesBurned = 312f
                    val walkExpanded = rememberSaveable { mutableStateOf(false) }
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .clickable(onClick = { walkExpanded.value = !walkExpanded.value })
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 10.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Ходьба")
                                Row {
                                    Text("312 ккал")
                                    Icon(
                                        Icons.Default.ArrowDropDown, "",
                                        modifier = Modifier.rotate(if (walkExpanded.value) 180f else 0f)
                                    )
                                }
                            }

                            if (walkExpanded.value) {
                                HorizontalDivider()
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text("7537 шагов")
                                    Text("1 ч 19 мин")
                                }
                            }
                        }
                    }
                    Spacer(Modifier.padding(vertical = 2.dp))
                } else {
                    caloriesBurned = 0f
                }

                Card(
                    onClick = onNavigatetoAddActivityTrack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Add,
                        "add new entry",
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Text("Завтрак", style = MaterialTheme.typography.headlineLarge)
            }

            items(breakfastList.value) {trackWithNutrition ->
                NutritionEntry(
                    modifier = Modifier.padding(vertical = 3.dp),
                    trackWithNutrition = trackWithNutrition
                )
            }

            item {
                Card(
                    onClick = { onNavigateToFoodSearcher("breakfast") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Add,
                        "add new entry",
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Text("Обед", style = MaterialTheme.typography.headlineLarge)
            }

            items(lunchList.value) {trackWithNutrition ->
                NutritionEntry(
                    modifier = Modifier.padding(vertical = 3.dp),
                    trackWithNutrition = trackWithNutrition
                )
            }

            item {
                Card(
                    onClick = { onNavigateToFoodSearcher("lunch") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Add,
                        "add new entry",
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.padding(10.dp))

                Text("Ужин", style = MaterialTheme.typography.headlineLarge)
            }

            items(dinnerList.value) {trackWithNutrition ->
                NutritionEntry(
                    modifier = Modifier.padding(vertical = 3.dp),
                    trackWithNutrition = trackWithNutrition
                )
            }

            item {
                Card(
                    onClick = { onNavigateToFoodSearcher("dinner") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.Add,
                        "add new entry",
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun NutritionScreenPreview() {
//    AppTheme(darkTheme = true) {
//        NutritionScreen()
//    }
//}

@Composable
fun MacronutrientProgress(
    modifier: Modifier = Modifier,
    macronutrientName: String,
    dailyTarget: Int,
    supplied: Int
) {
    Row(modifier = modifier) {
        val progress = supplied.toFloat() / dailyTarget

        CircularProgressIndicator(
            progress = { progress },
            trackColor = Color.LightGray
        )

        Spacer(modifier = Modifier.padding(3.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val t = dailyTarget.toInt()
            val s = supplied.toInt()
            Text("$s/$t г", style = MaterialTheme.typography.bodyMedium)
            Text(macronutrientName, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun NutritionEntry(
    modifier: Modifier = Modifier,
    trackWithNutrition: FoodTrackerWithNutrition,
    isExpanded: Boolean = false,
) {
    var isExpanded = rememberSaveable { mutableStateOf(isExpanded) }

    val weight = trackWithNutrition.foodTracker.foodWeight
    val protein = trackWithNutrition.nutrition.protein * weight/100f
    val fat = trackWithNutrition.nutrition.fat * weight/100f
    val carbs = trackWithNutrition.nutrition.carbs * weight/100f

    Card(
        modifier = modifier.clickable(onClick = { isExpanded.value = !isExpanded.value }),
        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    trackWithNutrition.foodTracker.foodName,
                    modifier = Modifier.weight(.5f),
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.weight(.5f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    val calories = (4*(protein + carbs) + 9*fat).toInt()
                    Text("$calories ккал", style = MaterialTheme.typography.titleMedium)
                    Icon(
                        Icons.Default.ArrowDropDown, "drop down",
                        modifier = Modifier.rotate(if (isExpanded.value) 180f else 0f)
                    )
                }
            }

            if (isExpanded.value) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text("${protein.toInt()} г\nбелков", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                    Text("${fat.toInt()} г\nжиров", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                    Text("${carbs.toInt()} г\n углев.", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                    Text("${weight.toInt()} г\n общий вес", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

fun caloriesFromNutrition(protein: Float, fat: Float, carbs: Float, weight: Float): Float {
    return weight/100f * (4*(protein + carbs) + 9*fat)
}