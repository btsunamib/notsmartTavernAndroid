package com.sillytavern.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sillytavern.android.data.model.WorldInfo
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
    indices = [androidx.room.Index(value = ["worldInfoId"])]
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
) {
    fun toWorldInfoEntry(): WorldInfoEntry {
        return WorldInfoEntry(
            keys = if (keys.isNotEmpty()) keys.split(",") else emptyList(),
            content = content,
            enabled = enabled,
            insertionOrder = insertionOrder,
            caseSensitive = caseSensitive,
            name = name,
            priority = priority,
            id = entryId,
            comment = comment,
            selective = selective,
            secondaryKeys = if (secondaryKeys.isNotEmpty()) secondaryKeys.split(",") else emptyList(),
            constant = constant,
            position = position,
            depth = depth,
            group = group,
            groupOverlap = groupOverlap,
            groupWeight = groupWeight,
            useProbability = useProbability,
            probability = probability,
            displayIndex = displayIndex,
            excludeRecursion = excludeRecursion,
            preventRecursion = preventRecursion,
            delayUntilRecursion = delayUntilRecursion,
            scanDepth = scanDepth,
            matchWholeWords = matchWholeWords,
            vectorized = vectorized,
            sticky = sticky,
            cooldown = cooldown,
            delay = delay
        )
    }
    
    companion object {
        fun fromWorldInfoEntry(worldInfoId: Long, entry: WorldInfoEntry): WorldInfoEntryEntity {
            return WorldInfoEntryEntity(
                worldInfoId = worldInfoId,
                entryId = entry.id,
                keys = entry.keys.joinToString(","),
                content = entry.content,
                enabled = entry.enabled,
                insertionOrder = entry.insertionOrder,
                caseSensitive = entry.caseSensitive,
                name = entry.name,
                priority = entry.priority,
                comment = entry.comment,
                selective = entry.selective,
                secondaryKeys = entry.secondaryKeys.joinToString(","),
                constant = entry.constant,
                position = entry.position,
                depth = entry.depth,
                group = entry.group,
                groupOverlap = entry.groupOverlap,
                groupWeight = entry.groupWeight,
                useProbability = entry.useProbability,
                probability = entry.probability,
                displayIndex = entry.displayIndex,
                excludeRecursion = entry.excludeRecursion,
                preventRecursion = entry.preventRecursion,
                delayUntilRecursion = entry.delayUntilRecursion,
                scanDepth = entry.scanDepth,
                matchWholeWords = entry.matchWholeWords,
                vectorized = entry.vectorized,
                sticky = entry.sticky,
                cooldown = entry.cooldown,
                delay = entry.delay
            )
        }
    }
}
