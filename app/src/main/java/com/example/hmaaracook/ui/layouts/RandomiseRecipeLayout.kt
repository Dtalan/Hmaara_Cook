package com.example.hmaaracook.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hmaaracook.R
import com.example.hmaaracook.common.CookingConstants
import com.example.hmaaracook.data.model.Ingredient
import com.example.hmaaracook.data.model.Recipe
import com.example.hmaaracook.viewmodel.MainActivityViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RandomiseRecipeScreen(viewmodel: MainActivityViewModel, navController: NavController) {
    val suggestedRecipes by viewmodel.randomRecipes.collectAsState()
    
    RandomiseRecipeContent(
        suggestedRecipes = suggestedRecipes,
        onGetSuggestions = { type, mealCourseCount, time ->
            viewmodel.getRandomRecipes(type, mealCourseCount, time)
        },
        onRecipeClick = { recipe ->
            viewmodel.selectRecipe(recipe)
            navController.navigate("recipe_detail")
        },
        onBack = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RandomiseRecipeContent(
    suggestedRecipes: List<Recipe>,
    onGetSuggestions: (CookingConstants.MealType, Int, Int) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onBack: () -> Unit
) {
    var selectedMealType by remember { mutableStateOf(CookingConstants.MealType.BREAKFAST) }
    var selectedCuisineType by remember { mutableStateOf(CookingConstants.Cuisine.INDIAN) }
    var maxTime by remember { mutableFloatStateOf(60f) }
    var hasSearched by remember { mutableStateOf(false) }
    var mealCourseCount by remember { mutableIntStateOf(3) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Recipe Finder", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "What's on your mind?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Meal Type Preference
            PreferenceSection(title = "Meal Type") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CookingConstants.MealType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedMealType == type,
                            onClick = { selectedMealType = type },
                            label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }

            // Cuisine Type Preference
            PreferenceSection(title = "Cuisine Type") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CookingConstants.Cuisine.entries.forEach { cuisine ->
                        FilterChip(
                            selected = selectedCuisineType == cuisine,
                            onClick = { selectedCuisineType = cuisine },
                            label = { Text(cuisine.name.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }
            }

            // Max Preparation Time
            PreferenceSection(title = "Max Preparation Time: ${maxTime.roundToInt()} mins") {
                Slider(
                    value = maxTime,
                    onValueChange = { maxTime = it },
                    valueRange = 5f..120f,
                    steps = 23 // 5 min increments
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // Meal Course Count
                PreferenceSection(title = "Meal Course: $mealCourseCount") {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        FilledIconButton(
                            onClick = { if (mealCourseCount > 3) mealCourseCount-- },
                            enabled = mealCourseCount > 3
                        ) {
                            Text("-", style = MaterialTheme.typography.titleLarge)
                        }
                        Text(mealCourseCount.toString(), style = MaterialTheme.typography.titleMedium)
                        FilledIconButton(
                            onClick = { if (mealCourseCount < 5) mealCourseCount++ },
                            enabled = mealCourseCount < 5
                        ) {
                            Text("+", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }



            Button(
                onClick = {
                    onGetSuggestions(selectedMealType, mealCourseCount, maxTime.roundToInt())
                    hasSearched = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Surprise Me!", style = MaterialTheme.typography.titleMedium)
            }

            if (hasSearched) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                
                Text(
                    text = if (suggestedRecipes.isEmpty()) "No recipes found matching your criteria." else "Randomly picked for you:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                suggestedRecipes.forEach { recipe ->
                    RecipeItem(recipe = recipe) {
                        onRecipeClick(recipe)
                    }
                }
            }
        }
    }
}

@Composable
fun PreferenceSection(title: String, content: @Composable () -> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary
        )
        content()
    }
}

@Preview(showBackground = true)
@Composable
fun RandomiseRecipePreview() {
    val sampleRecipes = listOf(
        Recipe(
            id = 1,
            name = "Paneer Butter Masala",
            instructions = "1. Sauté tomatoes and cashews, then blend into a paste. 2. Cook paste with butter and spices.",
            tags = listOf("veg", "spicy", "creamy"),
            companionIds = listOf(49),
            imageUrl = "",
            ingredientsList = listOf(
                Ingredient("Paneer", 250.0, "g"),
                Ingredient("Tomato", 3.0, "pcs")
            ),
            cuisine = "Indian",
            course = "Main Course",
            serves = 2,
            totalTime = 35,
            mealTimes = listOf("LUNCH", "DINNER")
        )
    )
    MaterialTheme {
        RandomiseRecipeContent(
            suggestedRecipes = sampleRecipes,
            onGetSuggestions = { _, _, _ -> },
            onRecipeClick = {},
            onBack = {}
        )
    }
}
