package com.example.hmaaracook.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.math.pow

@Serializable
data class Ingredient(
    val name: String,
    val quantity: Double,
    val unit: String
) {
    /**
     * Returns the quantity adjusted for the given number of people.
     */
    fun getQuantityForServings(originalServings: Int, targetServings: Int): Double {
        if (originalServings <= 0) return quantity
        return (quantity / originalServings) * targetServings
    }
}

@Serializable
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: Int,
    @SerialName("title") val name: String,
    val ingredientsList: List<Ingredient>,
    val instructions: String,
    val tags: List<String>,
    val companionIds: List<Int>,
    val imageUrl: String,
    val cuisine: String,
    val course: String,
    val serves: Int,
    val totalTime: Int,
    val mealTimes: List<String> = emptyList()
) {
    /**
     * Returns a list of ingredients with quantities adjusted for the target number of servings.
     */
    fun getIngredientsForServings(targetServings: Int): List<Ingredient> {
        return ingredientsList.map { ingredient ->
            ingredient.copy(quantity = ingredient.getQuantityForServings(serves, targetServings))
        }
    }

    /**
     * Estimates the total time for the target number of servings.
     * Uses a power-law heuristic: doubling servings increases time by ~23%.
     */
    fun getEstimatedTime(targetServings: Int): Int {
        if (serves <= 0 || targetServings <= 0) return totalTime
        val ratio = targetServings.toDouble() / serves
        // Power-law heuristic: time increases with ratio^0.3
        return (totalTime * ratio.pow(0.3)).toInt()
    }

    /**
     * Returns instructions with placeholders like "{time}" or "{qty:IngredientName}" 
     * replaced with scaled values.
     */
    fun getDynamicInstructions(targetServings: Int): String {
        var dynamicText = instructions
        
        // Replace {time} placeholder
        val estimatedTime = getEstimatedTime(targetServings)
        dynamicText = dynamicText.replace("{time}", estimatedTime.toString())
        
        // Replace {qty:IngredientName} placeholders
        getIngredientsForServings(targetServings).forEach { ingredient ->
            val qtyString = if (ingredient.quantity % 1.0 == 0.0) {
                ingredient.quantity.toInt().toString()
            } else {
                "%.1f".format(ingredient.quantity)
            }
            dynamicText = dynamicText.replace("{qty:${ingredient.name}}", "$qtyString ${ingredient.unit}")
        }
        
        return dynamicText
    }
}
