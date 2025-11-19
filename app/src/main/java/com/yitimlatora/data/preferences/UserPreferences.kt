package com.yitimlatora.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences(private val context: Context) {
    
    companion object {
        val TEXT_SIZE = intPreferencesKey("text_size")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val JAVASCRIPT_ENABLED = booleanPreferencesKey("javascript_enabled")
        val HOME_PAGE = stringPreferencesKey("home_page")
    }
    
    val textSize: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[TEXT_SIZE] ?: 100
    }
    
    val darkMode: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[DARK_MODE] ?: false
    }
    
    val javascriptEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[JAVASCRIPT_ENABLED] ?: true
    }
    
    val homePage: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[HOME_PAGE] ?: "https://itim-latora.org"
    }
    
    suspend fun setTextSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[TEXT_SIZE] = size
        }
    }
    
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE] = enabled
        }
    }
    
    suspend fun setJavascriptEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[JAVASCRIPT_ENABLED] = enabled
        }
    }
    
    suspend fun setHomePage(url: String) {
        context.dataStore.edit { preferences ->
            preferences[HOME_PAGE] = url
        }
    }
}

