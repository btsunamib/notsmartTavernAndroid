package com.sillytavern.android.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "silly_tavern_prefs")

@Singleton
class PreferencesManager @Inject constructor(
    private val context: Context
) {
    object Keys {
        val API_KEY = stringPreferencesKey("api_key")
        val API_URL = stringPreferencesKey("api_url")
        val API_TYPE = stringPreferencesKey("api_type")
        val TEMPERATURE = doublePreferencesKey("temperature")
        val TOP_P = doublePreferencesKey("top_p")
        val TOP_K = intPreferencesKey("top_k")
        val MAX_TOKENS = intPreferencesKey("max_tokens")
        val REP_PEN = doublePreferencesKey("rep_pen")
        val STREAMING = booleanPreferencesKey("streaming")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val AUTO_SCROLL = booleanPreferencesKey("auto_scroll")
        val WORLD_INFO_DEPTH = intPreferencesKey("world_info_depth")
        val WORLD_INFO_BUDGET = intPreferencesKey("world_info_budget")
        val CURRENT_PRESET = stringPreferencesKey("current_preset")
        val CURRENT_PERSONA = longPreferencesKey("current_persona")
        val USERNAME = stringPreferencesKey("username")
        val FIRST_RUN = booleanPreferencesKey("first_run")
    }
    
    fun getString(key: Preferences.Key<String>, defaultValue: String = ""): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: defaultValue
        }
    }
    
    fun getInt(key: Preferences.Key<Int>, defaultValue: Int = 0): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: defaultValue
        }
    }
    
    fun getLong(key: Preferences.Key<Long>, defaultValue: Long = 0): Flow<Long> {
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: defaultValue
        }
    }
    
    fun getDouble(key: Preferences.Key<Double>, defaultValue: Double = 0.0): Flow<Double> {
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: defaultValue
        }
    }
    
    fun getBoolean(key: Preferences.Key<Boolean>, defaultValue: Boolean = false): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[key] ?: defaultValue
        }
    }
    
    suspend fun putString(key: Preferences.Key<String>, value: String) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    
    suspend fun putInt(key: Preferences.Key<Int>, value: Int) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    
    suspend fun putLong(key: Preferences.Key<Long>, value: Long) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    
    suspend fun putDouble(key: Preferences.Key<Double>, value: Double) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    
    suspend fun putBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }
    
    suspend fun remove(key: Preferences.Key<*>) {
        context.dataStore.edit { prefs ->
            prefs.remove(key)
        }
    }
    
    suspend fun clear() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
