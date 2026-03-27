package com.example.hmaaracook.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CacheFlag::class], version = 1, exportSchema = false)
abstract class AppCacheDatabase : RoomDatabase() {
    abstract fun cacheFlagDao(): CacheFlagDao

    companion object {
        @Volatile
        private var INSTANCE: AppCacheDatabase? = null

        fun getDatabase(context: Context): AppCacheDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppCacheDatabase::class.java,
                    "hmaara_cook_cache"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
