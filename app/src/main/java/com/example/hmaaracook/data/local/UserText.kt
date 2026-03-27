package com.example.hmaaracook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_text_table")
data class UserText(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val text: String
)
