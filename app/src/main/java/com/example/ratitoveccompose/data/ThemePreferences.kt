package com.example.ratitoveccompose.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.preference.PreferenceDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferences(private val context: Context)
{
    private val Context.dataStore by preferencesDataStore(name = "themePreferences")

    companion object{
        val LAST_DARK_THEME_KEY = booleanPreferencesKey(name = "DarkTheme")
    }

    suspend fun saveDarkTheme(Darktheme: Boolean){
        context.dataStore.edit { prefs -> prefs[LAST_DARK_THEME_KEY] = Darktheme }
    }

    val DarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[LAST_DARK_THEME_KEY] ?: false
        }
}