package com.example.hmaaracook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CacheFlagDao {
    @Query("SELECT isInitialized FROM cache_flags WHERE `key` = :key LIMIT 1")
    suspend fun isInitialized(key: String): Boolean?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setInitialized(flag: CacheFlag)
}
