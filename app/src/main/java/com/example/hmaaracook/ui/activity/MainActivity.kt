package com.example.hmaaracook.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hmaaracook.ui.layouts.HomeScreen
import com.example.hmaaracook.ui.layouts.MainLayout
import com.example.hmaaracook.ui.layouts.RandomiseRecipeScreen
import com.example.hmaaracook.ui.layouts.RecipeDetailScreen
import com.example.hmaaracook.viewmodel.MainActivityViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.initializeDatabaseFromAssets()
        setContent {
            val navController = rememberNavController()
            
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(navController = navController)
                }
                composable("randomise_recipe") {
                    RandomiseRecipeScreen(
                        viewmodel = viewModel,
                        navController = navController
                    )
                }
                composable("main") {
                    MainLayout(
                        viewmodel = viewModel,
                        navController = navController
                    )
                }
                composable("recipe_detail") {
                    RecipeDetailScreen(
                        viewmodel = viewModel,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}
