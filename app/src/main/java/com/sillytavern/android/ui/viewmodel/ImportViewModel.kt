package com.sillytavern.android.ui.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillytavern.android.data.local.entity.CharacterEntity
import com.sillytavern.android.data.local.entity.WorldInfoEntity
import com.sillytavern.android.data.local.entity.WorldInfoEntryEntity
import com.sillytavern.android.data.model.CharacterDataExtended
import com.sillytavern.android.data.model.Preset
import com.sillytavern.android.data.model.RegexScript
import com.sillytavern.android.data.model.WorldInfo
import com.sillytavern.android.data.repository.CharacterRepository
import com.sillytavern.android.util.CharacterCardParser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImportViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    
    sealed class ImportState {
        object Idle : ImportState()
        object Loading : ImportState()
        data class Success(val message: String) : ImportState()
        data class Error(val message: String) : ImportState()
    }
    
    private val _importState = MutableStateFlow<ImportState>(ImportState.Idle)
    val importState: StateFlow<ImportState> = _importState.asStateFlow()
    
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }
    
    fun importCharacters(context: Context, uris: List<Uri>) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading
            
            var successCount = 0
            var errorCount = 0
            
            uris.forEach { uri ->
                try {
                    val fileName = getFileName(context, uri) ?: "unknown"
                    
                    when {
                        fileName.endsWith(".png", ignoreCase = true) -> {
                            importPngCharacter(context, uri)
                            successCount++
                        }
                        fileName.endsWith(".json", ignoreCase = true) -> {
                            importJsonCharacter(context, uri)
                            successCount++
                        }
                        else -> {
                            errorCount++
                        }
                    }
                } catch (e: Exception) {
                    errorCount++
                    e.printStackTrace()
                }
            }
            
            _importState.value = if (successCount > 0) {
                ImportState.Success("Imported $successCount character(s)${if (errorCount > 0) ", $errorCount failed" else ""}")
            } else {
                ImportState.Error("Failed to import characters")
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
    
    fun importWorldInfo(context: Context, uri: Uri) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading
            
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().readText()
                    val worldInfo = json.decodeFromString<WorldInfo>(jsonString)
                    
                    // TODO: Save to database
                    
                    _importState.value = ImportState.Success("World info imported: ${worldInfo.name}")
                }
            } catch (e: Exception) {
                _importState.value = ImportState.Error("Failed to import world info: ${e.message}")
            }
        }
    }
    
    fun importPreset(context: Context, uri: Uri) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading
            
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().readText()
                    val preset = json.decodeFromString<Preset>(jsonString)
                    
                    // TODO: Save to database
                    
                    _importState.value = ImportState.Success("Preset imported: ${preset.name}")
                }
            } catch (e: Exception) {
                _importState.value = ImportState.Error("Failed to import preset: ${e.message}")
            }
        }
    }
    
    fun importRegex(context: Context, uri: Uri) {
        viewModelScope.launch {
            _importState.value = ImportState.Loading
            
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().readText()
                    val regexScripts = json.decodeFromString<List<RegexScript>>(jsonString)
                    
                    // TODO: Save to database
                    
                    _importState.value = ImportState.Success("Imported ${regexScripts.size} regex script(s)")
                }
            } catch (e: Exception) {
                _importState.value = ImportState.Error("Failed to import regex scripts: ${e.message}")
            }
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
