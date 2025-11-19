package com.yitimlatora

import android.app.Application
import com.yitimlatora.data.database.AppDatabase
import com.yitimlatora.data.preferences.UserPreferences
import com.yitimlatora.data.repository.YitimLatoraRepository

class YitimLatoraApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { 
        YitimLatoraRepository(
            database.bookmarkDao(),
            database.historyDao()
        ) 
    }
    val userPreferences by lazy { UserPreferences(this) }
}

