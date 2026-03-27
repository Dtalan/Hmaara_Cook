package com.example.hmaaracook.data

import com.example.hmaaracook.common.CookingConstants
import com.example.hmaaracook.data.model.Recipe

interface RecipeRepository {
    fun getAllRecipes() : List<Recipe>
    fun getAllBreakfastRecipes() : List<Recipe>
    fun getAllLunchRecipes() : List<Recipe>
    fun getAllDinnerRecipes() : List<Recipe>
    fun getDistinctRecipes(type : CookingConstants.MealType, mealCourseCount : Int, time : Int) : List<Recipe>
}