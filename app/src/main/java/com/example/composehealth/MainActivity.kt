package com.example.composehealth

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.composehealth.ui.theme.AppTheme
import com.example.composehealth.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val userDataStoreManager = UserDataStoreManager(this)
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = viewModel(factory = MainViewModel.factory)
                var topBarTitle by remember {
                    mutableStateOf("")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) },
                    topBar = {
                        TopAppBar(
                            title = { Text(topBarTitle, style = MaterialTheme.typography.headlineLarge) },
                            modifier = Modifier.heightIn(max = 80.dp)
                        )
                    },
                ) {padding ->
                    NavHost(
                        navController,
                        startDestination = Routes.Nutrition,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable<Routes.Nutrition> {
                            topBarTitle = "Питание"
                            NutritionScreen(
                                mainViewModel,
                                userDataStoreManager,
                                { mealName ->
                                    navController.navigate(Routes.FoodSearch(mealName))
                                },
                                { navController.navigate(Routes.AddActivityTrack) }
                            )
                        }
                        composable<Routes.Sleep> {
                            topBarTitle = "Сон"
                            SleepScreen(
                                mainViewModel,
                                { navController.navigate(Routes.SleepCalculator) }
                            )
                        }
                        composable<Routes.Profile> {
                            topBarTitle = "Профиль"
                            ProfileScreen(userDataStoreManager)
                        }
                        composable<Routes.FoodSearch> {
                            topBarTitle = "Добавить запись"
                            val foodSearch: Routes.FoodSearch = it.toRoute()
                            FoodSearchScreen(
                                mainViewModel,
                                foodSearch.mealName,
                                { navController.navigate(Routes.Nutrition) },
                                { navController.navigate(Routes.AddNewFood) }
                            )
                        }
                        composable<Routes.AddNewFood> {
                            topBarTitle = "Добавить продукт"
                            AddNewFoodScreen(
                                mainViewModel,
                                { navController.popBackStack() }
                            )
                        }
                        composable<Routes.AddActivityTrack> {
                            topBarTitle = "Добавить активность"
                            AddActivityTrackScreen()
                        }
                        composable<Routes.SleepCalculator> {
                            topBarTitle = "Калькулятор сна"
                            SleepCalculatorScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(
    ) {

        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentDestination = navBackStackEntry?.destination

        // Bottom nav items we declared
        Constants.BottomNavItems.forEach { navItem ->

            // Place the bottom nav items
            NavigationBarItem(

                // it currentRoute is equal then its selected route
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(navItem.route::class)
                } ?: false,

                // navigate on click
                onClick = {
                    navController.navigate(navItem.route)
                },

                // Icon of navItem
                icon = {
                    var imageVector: ImageVector
                    imageVector = when {
                        navItem.icon != null -> {
                            navItem.icon
                        }

                        navItem.iconResId != null -> {
                            ImageVector.vectorResource(navItem.iconResId)
                        }

                        else -> {
                            Icons.Default.Warning
                        }
                    }

                    Icon(imageVector = imageVector, contentDescription = navItem.label)
                },

                // label
                label = {
                    Text(text = navItem.label)
                },
                alwaysShowLabel = false,
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
fun MainActivityPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController)
}