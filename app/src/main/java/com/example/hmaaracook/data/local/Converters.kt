package com.example.hmaaracook.data.local

import androidx.room.TypeConverter
import com.example.hmaaracook.data.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromIntList(value: List<Int>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIntList(value: String): List<Int> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromIngredientList(value: List<Ingredient>): String {
        return Json.encodeToString(value)
    }

    @TypeConverter
    fun toIngredientList(value: String): List<Ingredient> {
        return Json.decodeFromString(value)
    }
}
