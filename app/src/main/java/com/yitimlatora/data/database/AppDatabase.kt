package com.yitimlatora.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yitimlatora.data.dao.BookmarkDao
import com.yitimlatora.data.dao.HistoryDao
import com.yitimlatora.data.model.Bookmark
import com.yitimlatora.data.model.HistoryItem

@Database(
    entities = [Bookmark::class, HistoryItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun historyDao(): HistoryDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "yitim_latora_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
