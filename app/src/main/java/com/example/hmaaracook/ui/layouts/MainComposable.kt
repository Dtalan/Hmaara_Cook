package com.example.hmaaracook.ui.layouts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hmaaracook.data.model.Ingredient
import com.example.hmaaracook.data.model.Recipe
import com.example.hmaaracook.viewmodel.MainActivityViewModel

@Composable
fun MainLayout(viewmodel: MainActivityViewModel, navController: NavController) {
    val recipes by viewmodel.recipes.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    
    val filteredRecipes = if (searchQuery.isEmpty()) {
        recipes
    } else {
        recipes.filter { 
            it.name.contains(searchQuery, ignoreCase = true) || 
            it.tags.any { tag -> tag.contains(searchQuery, ignoreCase = true) } 
        }
    }

    MainLayoutContent(
        recipes = filteredRecipes,
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        onRecipeClick = { recipe ->
            viewmodel.selectRecipe(recipe)
            navController.navigate("recipe_detail")
        },
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayoutContent(
    recipes: List<Recipe>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    navController: NavController
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Recipes", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Home") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate("home")
                                }
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle profile */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
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
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                placeholder = { Text("Search recipes or tags...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Text(
                text = "Available Recipes (${recipes.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(recipes) { recipe ->
                    RecipeItem(recipe = recipe) {
                        onRecipeClick(recipe)
                    }
                }
            }
        }
    }
}

@Composable
fun RecipeItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${recipe.totalTime} min",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                recipe.tags.take(3).forEach { tag ->
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = tag,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = recipe.instructions.replace(Regex("\\{.*?\\}"), "..."),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RecipeDetailScreen(viewmodel: MainActivityViewModel, onBack: () -> Unit) {
    val recipe by viewmodel.selectedRecipe.collectAsState()
    val allRecipes by viewmodel.recipes.collectAsState()
    
    val companionRecipes = recipe?.companionIds?.mapNotNull { id ->
        allRecipes.find { it.id == id }
    } ?: emptyList()

    RecipeDetailScreenContent(
        recipe = recipe,
        companionRecipes = companionRecipes,
        onBack = onBack,
        onCompanionClick = { companion ->
            viewmodel.selectRecipe(companion)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RecipeDetailScreenContent(
    recipe: Recipe?,
    companionRecipes: List<Recipe>,
    onBack: () -> Unit,
    onCompanionClick: (Recipe) -> Unit
) {
    var servingCount by remember(recipe) { mutableIntStateOf(recipe?.serves ?: 4) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.name ?: "Recipe Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle profile */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        recipe?.let {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("Image Placeholder", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }

                item {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    FlowRow(
                        modifier = Modifier.padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        it.tags.forEach { tag ->
                            AssistChip(
                                onClick = { },
                                label = { Text(tag) }
                            )
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Servings",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Adjust for your group",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { if (servingCount > 1) servingCount-- }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                                }
                                Text(
                                    text = servingCount.toString(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                IconButton(onClick = { servingCount++ }) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase")
                                }
                            }
                        }
                    }
                }

                item {
                    SectionTitle("Ingredients")
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            it.getIngredientsForServings(servingCount).forEach { ingredient ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = ingredient.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.weight(1f)
                                    )
                                    val qtyString = if (ingredient.quantity % 1.0 == 0.0) {
                                        ingredient.quantity.toInt().toString()
                                    } else {
                                        "%.1f".format(ingredient.quantity)
                                    }
                                    Text(
                                        text = "$qtyString ${ingredient.unit}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                if (it.ingredientsList.last().name != ingredient.name) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        thickness = 0.5.dp,
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SectionTitle("Instructions")
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Est. Time: ${it.getEstimatedTime(servingCount)} min",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    }
                    Text(
                        text = it.getDynamicInstructions(servingCount),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                
                if (companionRecipes.isNotEmpty()) {
                    item {
                        SectionTitle("Pairs Well With")
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(companionRecipes) { companion ->
                                CompanionItem(companion) { onCompanionClick(companion) }
                            }
                        }
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No recipe selected")
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun CompanionItem(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = recipe.tags.firstOrNull() ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainLayoutPreview() {
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
            cuisine = "",
            course = "",
            serves = 2,
            totalTime = 35
        ),
        Recipe(
            id = 2,
            name = "Chicken Biryani",
            instructions = "1. Marinate chicken in yogurt and spices. 2. Parboil rice and layer.",
            tags = listOf("non-veg", "spicy", "rice"),
            companionIds = listOf(32),
            imageUrl = "",
            ingredientsList = listOf(
                Ingredient("Chicken", 500.0, "g"),
                Ingredient("Rice", 2.0, "cups")
            ),
            cuisine = "",
            course = "",
            serves = 2,
            totalTime = 35
        )
    )
    MaterialTheme {
        MainLayoutContent(
            recipes = sampleRecipes,
            searchQuery = "",
            onSearchQueryChange = {},
            onRecipeClick = {},
            navController = NavController(androidx.compose.ui.platform.LocalContext.current)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecipeDetailPreview() {
    val sampleRecipe = Recipe(
        id = 1,
        name = "Paneer Butter Masala",
        instructions = "1. Sauté tomatoes and cashews, then blend into a paste. 2. Cook paste with butter and spices. 3. Add paneer cubes and cream. 4. Garnish with Kasuri Methi.",
        tags = listOf("veg", "spicy", "creamy"),
        companionIds = listOf(2),
        imageUrl = "",
        ingredientsList = listOf(
            Ingredient("Paneer", 250.0, "g"),
            Ingredient("Tomato", 3.0, "pcs"),
            Ingredient("Butter", 50.0, "g"),
            Ingredient("Cream", 2.0, "tbsp")
        ),
        cuisine = "",
        course = "",
        serves = 2,
        totalTime = 35
    )
    val companion = Recipe(
        id = 2,
        name = "Garlic Naan",
        instructions = "Bake...",
        tags = listOf("veg", "bread"),
        companionIds = emptyList(),
        imageUrl = "",
        ingredientsList = listOf(),
        cuisine = "",
        course = "",
        serves = 2,
        totalTime = 35
    )
    MaterialTheme {
        RecipeDetailScreenContent(
            recipe = sampleRecipe,
            companionRecipes = listOf(companion),
            onBack = {},
            onCompanionClick = {}
        )
    }
}
