package com.example.hmaaracook.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hmaaracook.common.CookingConstants
import com.example.hmaaracook.data.Repository.LocalDBRecipeRepository
import com.example.hmaaracook.data.local.AppCacheDatabase
import com.example.hmaaracook.data.local.AppDatabase
import com.example.hmaaracook.data.local.CacheFlag
import com.example.hmaaracook.data.local.UserText
import com.example.hmaaracook.data.model.Recipe
import com.example.hmaaracook.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainActivityViewModel"
    private val database = AppDatabase.getDatabase(application)
    private val cacheDatabase = AppCacheDatabase.getDatabase(application)
    
    private val userTextDao = database.userTextDao()
    private val recipeDao = database.recipeDao()
    private val cacheFlagDao = cacheDatabase.cacheFlagDao()

    private val repository = LocalDBRecipeRepository(recipeDao)

    // Expose the list of user texts as a StateFlow for the UI to observe
    val allUserTexts: StateFlow<List<UserText>> = userTextDao.getAllText()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Observe recipes from the database
    val recipes: StateFlow<List<Recipe>> = recipeDao.getAllRecipes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _randomRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val randomRecipes: StateFlow<List<Recipe>> = _randomRecipes.asStateFlow()

    private val _selectedRecipe = MutableStateFlow<Recipe?>(null)
    val selectedRecipe: StateFlow<Recipe?> = _selectedRecipe.asStateFlow()

    /**
     * Loads recipes from assets and stores them in the database if not already initialized.
     */
    fun initializeDatabaseFromAssets() {
        viewModelScope.launch {
            try {
                val isInitialized = cacheFlagDao.isInitialized("recipes_initialized") ?: false
                if (!isInitialized) {
                    val inputStream = getApplication<Application>().assets.open("recipes.json")
                    val content = inputStream.bufferedReader().use { it.readText() }
                    val list = Json.decodeFromString<List<Recipe>>(content)
                    
                    recipeDao.insertRecipes(list)
                    cacheFlagDao.setInitialized(CacheFlag("recipes_initialized", true))
                    
                    Logger.i(TAG, "Database initialized with ${list.size} recipes from assets.")
                } else {
                    Logger.i(TAG, "Database already initialized, skipping asset loading.")
                }
            } catch (e: Exception) {
                Logger.e(TAG, "Error initializing database from assets", e)
            }
        }
    }

    fun getRandomRecipes(type: CookingConstants.MealType, mealCourseCount: Int, time: Int) {

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getDistinctRecipes(type, mealCourseCount, time)
            }
            _randomRecipes.value = result
        }
    }

    fun selectRecipe(recipe: Recipe?) {
        _selectedRecipe.value = recipe
    }

    // Function to store a value in the database
    fun storeValueinDB(value: String) {
        Logger.i(TAG, "storeValueinDB: $value")
        viewModelScope.launch {
            userTextDao.insert(UserText(text = value))
        }
    }
}
