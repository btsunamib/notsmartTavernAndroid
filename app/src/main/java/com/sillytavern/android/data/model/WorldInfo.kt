package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class WorldInfo(
    @SerialName("name")
    val name: String = "",
    
    @SerialName("description")
    val description: String = "",
    
    @SerialName("entries")
    val entries: MutableList<WorldInfoEntry> = mutableListOf(),
    
    @SerialName("scan_depth")
    val scanDepth: Int = 2,
    
    @SerialName("token_budget")
    val tokenBudget: Int = 2048,
    
    @SerialName("recursive_scanning")
    val recursiveScanning: Boolean = false,
    
    @SerialName("case_sensitive")
    val caseSensitive: Boolean = false,
    
    @SerialName("match_whole_words")
    val matchWholeWords: Boolean = true,
    
    @SerialName("overflow_alert")
    val overflowAlert: Boolean = false,
    
    @SerialName("include_names")
    val includeNames: Boolean = true,
    
    @SerialName("character_strategy")
    val characterStrategy: Int = 1,
    
    @SerialName("budget_cap")
    val budgetCap: Int = 0,
    
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerialName("last_updated")
    val lastUpdated: Long = System.currentTimeMillis(),
    
    @SerialName("id")
    val id: Long = System.currentTimeMillis(),
    
    @SerialName("is_global")
    val isGlobal: Boolean = false,
    
    @SerialName("extensions")
    val extensions: Map<String, String> = emptyMap()
) : Parcelable {
    fun addEntry(entry: WorldInfoEntry): WorldInfo {
        entries.add(entry)
        return this.copy(
            entries = entries,
            lastUpdated = System.currentTimeMillis()
        )
    }
    
    fun removeEntry(entryId: Long): WorldInfo {
        entries.removeAll { it.id == entryId }
        return this.copy(
            entries = entries,
            lastUpdated = System.currentTimeMillis()
        )
    }
    
    fun updateEntry(entry: WorldInfoEntry): WorldInfo {
        val index = entries.indexOfFirst { it.id == entry.id }
        if (index >= 0) {
            entries[index] = entry
        }
        return this.copy(
            entries = entries,
            lastUpdated = System.currentTimeMillis()
        )
    }
}

@Serializable
@Parcelize
data class WorldInfoSettings(
    @SerialName("world_info")
    val worldInfo: WorldInfoGlobalSelect = WorldInfoGlobalSelect(),
    
    @SerialName("world_info_depth")
    val worldInfoDepth: Int = 2,
    
    @SerialName("world_info_budget")
    val worldInfoBudget: Int = 25,
    
    @SerialName("world_info_include_names")
    val worldInfoIncludeNames: Boolean = true,
    
    @SerialName("world_info_recursive")
    val worldInfoRecursive: Boolean = true,
    
    @SerialName("world_info_overflow_alert")
    val worldInfoOverflowAlert: Boolean = false,
    
    @SerialName("world_info_case_sensitive")
    val worldInfoCaseSensitive: Boolean = false,
    
    @SerialName("world_info_match_whole_words")
    val worldInfoMatchWholeWords: Boolean = true,
    
    @SerialName("world_info_character_strategy")
    val worldInfoCharacterStrategy: Int = 1,
    
    @SerialName("world_info_budget_cap")
    val worldInfoBudgetCap: Int = 0
) : Parcelable

@Serializable
@Parcelize
data class WorldInfoGlobalSelect(
    @SerialName("globalSelect")
    val globalSelect: List<String> = emptyList()
) : Parcelable
