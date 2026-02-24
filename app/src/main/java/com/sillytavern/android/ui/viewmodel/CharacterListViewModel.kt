package com.sillytavern.android.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillytavern.android.data.local.entity.CharacterEntity
import com.sillytavern.android.data.repository.CharacterRepository
import com.sillytavern.android.util.CharacterCardParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    val characters: StateFlow<List<CharacterEntity>> = searchQuery
        .map { query ->
            if (query.isBlank()) {
                characterRepository.getAllCharacters()
            } else {
                characterRepository.searchCharacters(query)
            }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList()).value
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun toggleFavorite(characterId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            characterRepository.toggleFavorite(characterId, isFavorite)
        }
    }
    
    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            characterRepository.deleteCharacter(character)
        }
    }
    
    fun importCharacters(context: Context, uris: List<Uri>) {
        viewModelScope.launch {
            uris.forEach { uri ->
                try {
                    importCharacter(context, uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    
    private suspend fun importCharacter(context: Context, uri: Uri) {
        val fileName = getFileName(context, uri) ?: "character_${System.currentTimeMillis()}"
        
        when {
            fileName.endsWith(".png", ignoreCase = true) -> {
                importPngCharacter(context, uri)
            }
            fileName.endsWith(".json", ignoreCase = true) -> {
                importJsonCharacter(context, uri)
            }
        }
    }
    
    private suspend fun importPngCharacter(context: Context, uri: Uri) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val result = CharacterCardParser.parseCharacterCard(inputStream)
            
            val avatarFile = saveAvatarImage(context, result.imageData)
            
            characterRepository.insertCharacterFromData(
                data = result.characterData,
                avatarPath = avatarFile.absolutePath
            )
        }
    }
    
    private suspend fun importJsonCharacter(context: Context, uri: Uri) {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val jsonString = inputStream.bufferedReader().readText()
            val characterData = CharacterCardParser.parseCharacterJson(jsonString)
            
            characterRepository.insertCharacterFromData(characterData)
        }
    }
    
    private fun saveAvatarImage(context: Context, imageData: ByteArray): File {
        val avatarDir = File(context.filesDir, "avatars").apply {
            if (!exists()) mkdirs()
        }
        
        val avatarFile = File(avatarDir, "avatar_${System.currentTimeMillis()}.png")
        FileOutputStream(avatarFile).use { it.write(imageData) }
        
        return avatarFile
    }
    
    private fun getFileName(context: Context, uri: Uri): String? {
        var name: String? = null
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst() && nameIndex >= 0) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }
}
