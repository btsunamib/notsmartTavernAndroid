package com.sillytavern.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillytavern.android.data.local.dao.WorldInfoDao
import com.sillytavern.android.data.local.dao.WorldInfoEntryDao
import com.sillytavern.android.data.local.entity.WorldInfoEntity
import com.sillytavern.android.data.local.entity.WorldInfoEntryEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorldInfoViewModel @Inject constructor(
    private val worldInfoDao: WorldInfoDao,
    private val worldInfoEntryDao: WorldInfoEntryDao
) : ViewModel() {
    
    private val _worldInfo = MutableStateFlow<WorldInfoEntity?>(null)
    val worldInfo: StateFlow<WorldInfoEntity?> = _worldInfo.asStateFlow()
    
    private var currentWorldInfoId: Long = 0
    
    val entries: StateFlow<List<WorldInfoEntryEntity>> = MutableStateFlow(emptyList())
        .also { flow ->
            viewModelScope.launch {
                worldInfoEntryDao.getEntriesForWorldInfo(currentWorldInfoId).collect { entries ->
                    flow.value = entries
                }
            }
        }
    
    fun loadWorldInfo(worldInfoId: Long) {
        currentWorldInfoId = worldInfoId
        viewModelScope.launch {
            _worldInfo.value = worldInfoDao.getWorldInfoById(worldInfoId)
        }
    }
    
    fun addEntry(entry: WorldInfoEntryEntity) {
        viewModelScope.launch {
            worldInfoEntryDao.insertEntry(entry.copy(worldInfoId = currentWorldInfoId))
        }
    }
    
    fun updateEntry(entry: WorldInfoEntryEntity) {
        viewModelScope.launch {
            worldInfoEntryDao.updateEntry(entry)
        }
    }
    
    fun deleteEntry(entry: WorldInfoEntryEntity) {
        viewModelScope.launch {
            worldInfoEntryDao.deleteEntry(entry)
        }
    }
    
    fun toggleEntryEnabled(entryId: Long, enabled: Boolean) {
        viewModelScope.launch {
            worldInfoEntryDao.updateEntryEnabled(entryId, enabled)
        }
    }
}
