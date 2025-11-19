package com.example.composehealth

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.composehealth.ui.theme.AppTheme
import com.example.composehealth.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController) }
                ) {padding ->
                    NavHost(
                        navController,
                        startDestination = Routes.Nutrition,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable<Routes.Nutrition> {
                            NutritionScreen()
                        }
                        composable<Routes.Sleep> {
                            SleepScreen()
                        }
                        composable<Routes.Activity> {
                            ActivityScreen()
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
                    Icon(imageVector = navItem.icon, contentDescription = navItem.label)
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