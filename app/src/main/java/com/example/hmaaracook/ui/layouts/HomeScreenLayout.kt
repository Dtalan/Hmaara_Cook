package com.example.hmaaracook.ui.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }
    var showRandomAlert by remember { mutableStateOf(false) }
    var showProfileAlert by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hmaara Cook", fontWeight = FontWeight.Bold) },
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
                                text = { Text("View all recipes") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate("main")
                                },
                                leadingIcon = { Icon(Icons.Default.AllInbox, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("FAQs") },
                                onClick = {
                                    showMenu = false
                                    showProfileAlert = true
                                },
                                leadingIcon = { Icon(Icons.Default.BorderColor, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Contact Us") },
                                onClick = {
                                    showMenu = false
                                    showProfileAlert = true
                                },
                                leadingIcon = { Icon(Icons.Default.Bolt, contentDescription = null) }
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = { showProfileAlert = true }) {
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Engaging Image Placeholder (using a Surface with an Emoji/Text for visual appeal)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(24.dp)),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🍳",
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Deliciousness Awaits",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Text(
                text = "Welcome to Hmaara Cook",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Discover hundreds of authentic recipes from around the world. Whether you're a beginner or a pro, find the perfect meal for any occasion with our curated collections.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.navigate("main") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Explore All Recipes", style = MaterialTheme.typography.titleMedium)
            }

            OutlinedButton(
                onClick = { navController.navigate("randomise_recipe") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Randomise Recipes", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    if (showRandomAlert) {
        AlertDialog(
            onDismissRequest = { showRandomAlert = false },
            title = { Text("Randomise Pressed") },
            text = { Text("The Randomise Recipes button has been pressed! This feature will be available soon.") },
            confirmButton = {
                TextButton(onClick = { showRandomAlert = false }) {
                    Text("Got it")
                }
            }
        )
    }

    if (showProfileAlert) {
        AlertDialog(
            onDismissRequest = { showProfileAlert = false },
            title = { Text("Sorry") },
            text = { Text("This Feature is coming in a future update.") },
            confirmButton = {
                TextButton(onClick = { showProfileAlert = false }) {
                    Text("OK")
                }
            }
        )
    }
}
