package com.example.hmaaracook.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_flags")
data class CacheFlag(
    @PrimaryKey val key: String,
    val isInitialized: Boolean
)
