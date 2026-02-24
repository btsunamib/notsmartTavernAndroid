package com.sillytavern.android.data.local.dao

import androidx.room.*
import com.sillytavern.android.data.local.entity.CharacterEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters ORDER BY lastChatAt DESC, name ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>
    
    @Query("SELECT * FROM characters WHERE id = :id")
    suspend fun getCharacterById(id: Long): CharacterEntity?
    
    @Query("SELECT * FROM characters WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    fun searchCharacters(query: String): Flow<List<CharacterEntity>>
    
    @Query("SELECT * FROM characters WHERE isFavorite = 1 ORDER BY name ASC")
    fun getFavoriteCharacters(): Flow<List<CharacterEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity): Long
    
    @Update
    suspend fun updateCharacter(character: CharacterEntity)
    
    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)
    
    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteCharacterById(id: Long)
    
    @Query("UPDATE characters SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)
    
    @Query("UPDATE characters SET lastChatAt = :lastChatAt, chatCount = chatCount + 1 WHERE id = :id")
    suspend fun updateChatStats(id: Long, lastChatAt: Long = System.currentTimeMillis())
    
    @Query("SELECT COUNT(*) FROM characters")
    suspend fun getCharacterCount(): Int
    
    @Transaction
    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characters.forEach { insertCharacter(it) }
    }
}
