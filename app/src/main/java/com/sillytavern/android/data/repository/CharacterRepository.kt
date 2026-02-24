package com.sillytavern.android.data.repository

import com.sillytavern.android.data.local.dao.CharacterDao
import com.sillytavern.android.data.local.entity.CharacterEntity
import com.sillytavern.android.data.model.CharacterDataExtended
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CharacterRepository @Inject constructor(
    private val characterDao: CharacterDao
) {
    fun getAllCharacters(): Flow<List<CharacterEntity>> {
        return characterDao.getAllCharacters()
    }
    
    suspend fun getCharacterById(id: Long): CharacterEntity? {
        return characterDao.getCharacterById(id)
    }
    
    fun searchCharacters(query: String): Flow<List<CharacterEntity>> {
        return characterDao.searchCharacters(query)
    }
    
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>> {
        return characterDao.getFavoriteCharacters()
    }
    
    suspend fun insertCharacter(character: CharacterEntity): Long {
        return characterDao.insertCharacter(character)
    }
    
    suspend fun insertCharacterFromData(data: CharacterDataExtended, avatarPath: String? = null): Long {
        val entity = CharacterEntity.fromCharacterData(data, avatarPath)
        return characterDao.insertCharacter(entity)
    }
    
    suspend fun updateCharacter(character: CharacterEntity) {
        characterDao.updateCharacter(character)
    }
    
    suspend fun deleteCharacter(character: CharacterEntity) {
        characterDao.deleteCharacter(character)
    }
    
    suspend fun deleteCharacterById(id: Long) {
        characterDao.deleteCharacterById(id)
    }
    
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        characterDao.updateFavorite(id, isFavorite)
    }
    
    suspend fun updateChatStats(id: Long) {
        characterDao.updateChatStats(id)
    }
}
