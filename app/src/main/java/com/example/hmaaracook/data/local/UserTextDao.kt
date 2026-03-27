package com.example.hmaaracook.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserTextDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userText: UserText)

    @Query("SELECT * FROM user_text_table ORDER BY id DESC")
    fun getAllText(): Flow<List<UserText>>
}
