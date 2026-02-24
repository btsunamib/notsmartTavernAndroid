package com.sillytavern.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: com.sillytavern.android.data.repository.SettingsRepository
) : ViewModel() {
    
    data class AppSettings(
        val apiKey: String = "",
        val apiUrl: String = "https://api.openai.com/v1/chat/completions",
        val apiType: String = "openai",
        val temperature: Float = 1.0f,
        val topP: Float = 0.9f,
        val maxTokens: Int = 500,
        val worldInfoDepth: Int = 2,
        val worldInfoBudget: Int = 2048,
        val streamingEnabled: Boolean = true,
        val autoScroll: Boolean = true,
        val darkMode: Boolean = true
    )
    
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()
    
    fun updateApiSettings(apiKey: String, apiUrl: String, apiType: String) {
        _settings.value = _settings.value.copy(
            apiKey = apiKey,
            apiUrl = apiUrl,
            apiType = apiType
        )
        settingsRepository.setApiConfig(apiKey, apiUrl, apiType)
    }
    
    fun updateTemperature(value: Float) {
        _settings.value = _settings.value.copy(temperature = value)
    }
    
    fun updateTopP(value: Float) {
        _settings.value = _settings.value.copy(topP = value)
    }
    
    fun updateMaxTokens(value: Int) {
        _settings.value = _settings.value.copy(maxTokens = value)
    }
    
    fun updateWorldInfoDepth(value: Int) {
        _settings.value = _settings.value.copy(worldInfoDepth = value)
    }
    
    fun updateWorldInfoBudget(value: Int) {
        _settings.value = _settings.value.copy(worldInfoBudget = value)
    }
}
