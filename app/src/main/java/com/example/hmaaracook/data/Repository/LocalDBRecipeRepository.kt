package com.example.hmaaracook.data.Repository

import com.example.hmaaracook.common.CookingConstants
import com.example.hmaaracook.data.RecipeRepository
import com.example.hmaaracook.data.local.RecipeDao
import com.example.hmaaracook.data.model.Recipe

class LocalDBRecipeRepository(private val recipeDao: RecipeDao) : RecipeRepository {
    override fun getAllRecipes(): List<Recipe> {
        TODO("Not yet implemented")
    }

    override fun getAllBreakfastRecipes(): List<Recipe> {
        TODO("Not yet implemented")
    }

    override fun getAllLunchRecipes(): List<Recipe> {
        TODO("Not yet implemented")
    }

    override fun getAllDinnerRecipes(): List<Recipe> {
        TODO("Not yet implemented")
    }

    override fun getDistinctRecipes(
        type: CookingConstants.MealType,
        mealCourseCount: Int,
        time: Int
    ): List<Recipe> {
        return recipeDao.getRandomRecipes(
            mealType = type.name,
            maxTime = time,
            limit = mealCourseCount
        )
    }
}
