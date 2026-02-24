package com.sillytavern.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillytavern.android.data.model.Preset
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PresetViewModel @Inject constructor(
) : ViewModel() {
    
    private val _preset = MutableStateFlow<Preset?>(null)
    val preset: StateFlow<Preset?> = _preset.asStateFlow()
    
    fun loadPreset(presetId: String) {
        viewModelScope.launch {
            _preset.value = Preset(name = presetId)
        }
    }
    
    fun updateTemperature(value: Double) {
        _preset.value = _preset.value?.copy(temperature = value)
    }
    
    fun updateTopP(value: Double) {
        _preset.value = _preset.value?.copy(topP = value)
    }
    
    fun updateTopK(value: Int) {
        _preset.value = _preset.value?.copy(topK = value)
    }
    
    fun updateRepPen(value: Double) {
        _preset.value = _preset.value?.copy(repetitionPenalty = value)
    }
    
    fun updateRepPenRange(value: Int) {
        _preset.value = _preset.value?.copy(repetitionPenaltyRange = value)
    }
    
    fun updateMirostatMode(value: Int) {
        _preset.value = _preset.value?.copy(mirostatMode = value)
    }
    
    fun updateMirostatTau(value: Double) {
        _preset.value = _preset.value?.copy(mirostatTau = value)
    }
    
    fun updateMirostatEta(value: Double) {
        _preset.value = _preset.value?.copy(mirostatEta = value)
    }
    
    fun updateStreaming(value: Boolean) {
        _preset.value = _preset.value?.copy(streaming = value)
    }
    
    fun updateAddBosToken(value: Boolean) {
        _preset.value = _preset.value?.copy(addBosToken = value)
    }
    
    fun updateBanEosToken(value: Boolean) {
        _preset.value = _preset.value?.copy(banEosToken = value)
    }
    
    fun updateSkipSpecialTokens(value: Boolean) {
        _preset.value = _preset.value?.copy(skipSpecialTokens = value)
    }
    
    fun savePreset() {
        // TODO: Implement preset saving
    }
}
