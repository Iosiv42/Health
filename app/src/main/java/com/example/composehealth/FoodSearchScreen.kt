package com.example.composehealth

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composehealth.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable;

import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import java.util.Locale

@Composable
fun FoodSearchScreen(
    mainViewModel: MainViewModel,
    mealName: String,
    onNavigateToNutrition: () -> Unit,
    onNavigateToAddNewFood: () -> Unit,
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val navController = rememberNavController()

    Column(
        modifier = Modifier.fillMaxSize().padding(10.dp)
    ) {
        PrimaryTabRow(selectedTabIndex) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = {
                    selectedTabIndex = 0
                    navController.navigate(FoodSearchRoutes.User)
                },
                text = {
                    Text("Пользовательские", overflow = TextOverflow.Ellipsis)
                }
            )

            Tab(
                selected = selectedTabIndex == 1,
                onClick = {
                    selectedTabIndex = 1
                    navController.navigate(FoodSearchRoutes.University)
                },
                text = {
                    Text("Университет", overflow = TextOverflow.Ellipsis)
                }
            )
        }

        NavHost(
            navController,
            startDestination = FoodSearchRoutes.User
        ) {
            composable<FoodSearchRoutes.User> {
                FoodSearchUser(
                    mainViewModel, mealName, onNavigateToNutrition, onNavigateToAddNewFood
                )
            }
            composable<FoodSearchRoutes.University> {
                FoodSearchUniversity()
            }
        }
    }
}

@Composable
fun FoodSearchUser(
    mainViewModel: MainViewModel,
    mealName: String,
    onNavigateToNutrition: () -> Unit,
    onNavigateToAddNewFood: () -> Unit,
) {
    val allFoodNutrition = mainViewModel.allFoodNutrition().collectAsState(emptyList())
    var searchResults by remember {
        mutableStateOf(emptyList<String>())
    }
    val textFieldState = rememberTextFieldState("")
    val foodName = remember { mutableStateOf("") }

    LaunchedEffect(textFieldState.text) {
        if (textFieldState.text.isBlank()) {
            searchResults = allFoodNutrition.value.map { it.name }
            return@LaunchedEffect
        }

        delay(300) // Debounce time
        searchResults = allFoodNutrition.value.map { it.name }
            .filter {
                FuzzySearch.partialRatio(
                    it.lowercase(),
                    textFieldState.text.toString().lowercase()
                ) >= 90
            }
    }

    searchResults = allFoodNutrition.value.map { it.name }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        SimpleSearchBar(
            textFieldState,
            foodName,
            {},
            onNavigateToAddNewFood,
            searchResults
        )

        HorizontalDivider()

        if (foodName.value.isBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentHeight()
                    .wrapContentWidth()
            ) {
                Text("Выберите продукт", style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
            }
        } else {
            val foodName = textFieldState.text.toString()
            val foodNutrition = mainViewModel.foodNutritionByName(foodName)
                .collectAsState(
                    initial = FoodNutrition("", 0f, 0f, 0f)
                )

            Da(
                foodName,
                Macronutrients.fromFoodNutrition(foodNutrition.value),
                onAddClick = {quantity ->
                    mainViewModel.addFoodTrack(FoodTracker(
                        0,
                        foodName,
                        quantity.toFloat(),
                        mainViewModel.currentDate.value.toString(),
                        mealName
                    ))

                    onNavigateToNutrition()
                }
            )
        }
    }
}

@Composable
fun FoodSearchUniversity() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {
        item {
            UniversityEateryEntry(
                UniversityEatery(
                    "Буфет №1", "13:00 - 15:00", "Корпус 8, этаж 2",
                    listOf(
                        MenuEntry("Блюдо №1", 111, 111, 111),
                        MenuEntry("Блюдо №2", 222, 222, 222),
                        MenuEntry("Блюдо №3", 333, 312, 123),
                        MenuEntry("Блюдо №4", 123, 321, 212),
                    )
                ),
                Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(vertical = 10.dp))

            UniversityEateryEntry(
                UniversityEatery(
                    "Буфет №2", "13:00 - 15:00", "Корпус 8, этаж 2",
                    listOf(
                        MenuEntry("Блюдо №1", 100, 42, 42),
                        MenuEntry("Блюдо №2", 228, 404, 141),
                        MenuEntry("Блюдо №7", 141, 52, 142),
                    )
                ),
                Modifier.fillMaxWidth()
            )
        }
    }
}

data class MenuEntry(
    val name: String,
    val calories: Int,
    val cost: Int,
    val weight: Int,
)

data class UniversityEatery(
    val name: String,
    val workingTime: String,
    val location: String,
    val menu: List<MenuEntry>
)

@Composable
fun UniversityEateryEntry(
    universityEatery: UniversityEatery,
    modifier: Modifier = Modifier
) {
    var expanded = rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier.clickable(onClick = { expanded.value = !expanded.value })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(universityEatery.name, style = MaterialTheme.typography.titleLarge)
                Icon(
                    Icons.Default.ArrowDropDown, "",
                    modifier = Modifier.rotate(if (expanded.value) 180f else 0f)
                )
            }

            if (expanded.value) {
                HorizontalDivider()
                Text(universityEatery.location)
                Text("(${universityEatery.workingTime})")
                HorizontalDivider()

                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    universityEatery.menu.forEach { menuEntry ->
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable(onClick = {}),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(menuEntry.name)
                            Text("${menuEntry.cost} руб.")
                            Text("${menuEntry.weight} г")
                            Text("${menuEntry.calories} ккал")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun UniversityEateryEntryPreview() {
    AppTheme {
        UniversityEateryEntry(
            UniversityEatery(
                "Буфет №1", "13:00 - 15:00", "Корпус 8, этаж 2",
                listOf(
                    MenuEntry("Блюдо №1", 111, 111, 111),
                    MenuEntry("Блюдо №2", 222, 222, 222),
                    MenuEntry("Блюдо №3", 333, 312, 123),
                    MenuEntry("Блюдо №4", 123, 321, 212),
                )
            ),
            Modifier.fillMaxWidth()
        )
    }
}

@Serializable
sealed class FoodSearchRoutes {
    @Serializable
    data object User: Routes()

    @Serializable
    data object University: Routes()
}

@Composable
fun Da(
    foodName: String,
    nutrition: Macronutrients,
    onAddClick: (quantity: Int) -> Unit,
) {
    var quantityText by remember {
        mutableStateOf("100")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(foodName, style = MaterialTheme.typography.displayMedium, textAlign = TextAlign.Center)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${nutrition.protein} г", fontWeight = FontWeight.Bold)
                Text("белков")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${nutrition.fat} г", fontWeight = FontWeight.Bold)
                Text("жиров")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${nutrition.carbs} г", fontWeight = FontWeight.Bold)
                Text("углев.")
            }

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("${nutrition.calories.toInt()} ккал\n")
                    }

                    append(" / 100г")
                }, textAlign = TextAlign.Center
            )
        }

        HorizontalDivider()

        OutlinedTextField(
            value = quantityText,
            onValueChange = {quantityText = it },
            label = { Text("Количество") },
            suffix = { Text("г") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        Button(
            onClick = {
                onAddClick(quantityText.toInt())
            }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Add, "")
                Text("Добавить")
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DaPreview() {
    AppTheme {
        Da(
            "Гречневая каша",
            Macronutrients(
                4.2f, 2f, 18.8f, 110f
            ),
            {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar(
    textFieldState: TextFieldState,
    foodName: MutableState<String>,
    onSearch: (String) -> Unit,
    onAddClick: () -> Unit,
    searchResults: List<String>,
    modifier: Modifier = Modifier
) {
    // Controls expansion state of the search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .semantics { var isTraversalGroup = true }
            .fillMaxWidth(),
    ) {
        SearchBar(
            modifier = Modifier
                .semantics { traversalIndex = 0f }
                .align(Alignment.TopCenter),
            inputField = {
                SearchBarDefaults.InputField(
                    query = textFieldState.text.toString(),
                    onQueryChange = { textFieldState.edit { replace(0, length, it) } },
                    onSearch = {
                        onSearch(textFieldState.text.toString())
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = { Text("Поиск продуктов") },
                    leadingIcon = { Icon(Icons.Default.Search, "") },
                    trailingIcon = {
                        IconButton(onClick = onAddClick) {
                            Icon(Icons.Default.Add, "")
                    }}
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Display search results in a scrollable column
            Column(Modifier.verticalScroll(rememberScrollState())) {
                searchResults.forEach { result ->
                    ListItem(
                        headlineContent = { Text(result) },
                        modifier = Modifier
                            .clickable {
                                textFieldState.edit { replace(0, length, result) }
                                foodName.value = result
                                expanded = false
                            }
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SimpleSearchBarPreview() {
    AppTheme {
        val da = remember { mutableStateOf("") }
        SimpleSearchBar(
            rememberTextFieldState(),
            da,
            {},
            {},
            emptyList()
        )
    }
}