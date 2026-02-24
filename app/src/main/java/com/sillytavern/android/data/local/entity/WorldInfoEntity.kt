package com.sillytavern.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sillytavern.android.data.model.WorldInfoEntry

@Entity(tableName = "world_info")
data class WorldInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    val description: String = "",
    val scanDepth: Int = 2,
    val tokenBudget: Int = 2048,
    val recursiveScanning: Boolean = false,
    val caseSensitive: Boolean = false,
    val matchWholeWords: Boolean = true,
    val overflowAlert: Boolean = false,
    val includeNames: Boolean = true,
    val characterStrategy: Int = 1,
    val budgetCap: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis(),
    val isGlobal: Boolean = false,
    val characterId: Long? = null
)

@Entity(
    tableName = "world_info_entries",
    foreignKeys = [
        ForeignKey(
            entity = WorldInfoEntity::class,
            parentColumns = ["id"],
            childColumns = ["worldInfoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["worldInfoId"])]
)
data class WorldInfoEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val worldInfoId: Long,
    val entryId: Long = System.currentTimeMillis(),
    val keys: String,
    val content: String,
    val enabled: Boolean = true,
    val insertionOrder: Int = 100,
    val caseSensitive: Boolean = false,
    val name: String = "",
    val priority: Int = 10,
    val comment: String = "",
    val selective: Boolean = false,
    val secondaryKeys: String = "",
    val constant: Boolean = false,
    val position: String = "before_char",
    val depth: Int = 4,
    val group: String = "",
    val groupOverlap: Boolean = false,
    val groupWeight: Int = 100,
    val useProbability: Boolean = false,
    val probability: Int = 100,
    val displayIndex: Int = 0,
    val excludeRecursion: Boolean = false,
    val preventRecursion: Boolean = false,
    val delayUntilRecursion: Boolean = false,
    val scanDepth: Int? = null,
    val matchWholeWords: Boolean = false,
    val vectorized: Boolean = false,
    val sticky: Int = 0,
    val cooldown: Int = 0,
    val delay: Int = 0
)
