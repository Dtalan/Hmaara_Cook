package com.example.hmaaracook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.hmaaracook.data.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): Flow<List<Recipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes WHERE mealTimes LIKE '%' || :mealType || '%' AND totalTime <= :maxTime ORDER BY RANDOM() LIMIT :limit")
    fun getRandomRecipes(mealType: String, maxTime: Int, limit: Int): List<Recipe>
}
