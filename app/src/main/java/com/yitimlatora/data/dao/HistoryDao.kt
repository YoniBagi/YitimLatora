package com.yitimlatora.data.dao

import androidx.room.*
import com.yitimlatora.data.model.HistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history ORDER BY visitTimestamp DESC LIMIT 50")
    fun getRecentHistory(): Flow<List<HistoryItem>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryItem)
    
    @Delete
    suspend fun deleteHistoryItem(item: HistoryItem)
    
    @Query("DELETE FROM history")
    suspend fun clearHistory()
}

