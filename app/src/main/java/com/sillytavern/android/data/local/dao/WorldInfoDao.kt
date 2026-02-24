package com.sillytavern.android.data.local.dao

import androidx.room.*
import com.sillytavern.android.data.local.entity.WorldInfoEntity
import com.sillytavern.android.data.local.entity.WorldInfoEntryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldInfoDao {
    @Query("SELECT * FROM world_info WHERE isGlobal = 1 ORDER BY name ASC")
    fun getGlobalWorldInfo(): Flow<List<WorldInfoEntity>>
    
    @Query("SELECT * FROM world_info WHERE characterId = :characterId ORDER BY name ASC")
    fun getWorldInfoForCharacter(characterId: Long): Flow<List<WorldInfoEntity>>
    
    @Query("SELECT * FROM world_info WHERE id = :id")
    suspend fun getWorldInfoById(id: Long): WorldInfoEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorldInfo(worldInfo: WorldInfoEntity): Long
    
    @Update
    suspend fun updateWorldInfo(worldInfo: WorldInfoEntity)
    
    @Delete
    suspend fun deleteWorldInfo(worldInfo: WorldInfoEntity)
    
    @Query("DELETE FROM world_info WHERE id = :id")
    suspend fun deleteWorldInfoById(id: Long)
    
    @Query("SELECT * FROM world_info WHERE id IN (:ids)")
    suspend fun getWorldInfoByIds(ids: List<Long>): List<WorldInfoEntity>
}

@Dao
interface WorldInfoEntryDao {
    @Query("SELECT * FROM world_info_entries WHERE worldInfoId = :worldInfoId ORDER BY displayIndex ASC, insertionOrder ASC")
    fun getEntriesForWorldInfo(worldInfoId: Long): Flow<List<WorldInfoEntryEntity>>
    
    @Query("SELECT * FROM world_info_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): WorldInfoEntryEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: WorldInfoEntryEntity): Long
    
    @Update
    suspend fun updateEntry(entry: WorldInfoEntryEntity)
    
    @Delete
    suspend fun deleteEntry(entry: WorldInfoEntryEntity)
    
    @Query("DELETE FROM world_info_entries WHERE id = :id")
    suspend fun deleteEntryById(id: Long)
    
    @Query("DELETE FROM world_info_entries WHERE worldInfoId = :worldInfoId")
    suspend fun deleteEntriesForWorldInfo(worldInfoId: Long)
    
    @Query("UPDATE world_info_entries SET enabled = :enabled WHERE id = :id")
    suspend fun updateEntryEnabled(id: Long, enabled: Boolean)
    
    @Transaction
    suspend fun insertEntries(entries: List<WorldInfoEntryEntity>) {
        entries.forEach { insertEntry(it) }
    }
}
