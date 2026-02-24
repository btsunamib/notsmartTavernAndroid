package com.sillytavern.android.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class CharacterCardV2(
    @SerialName("spec")
    val spec: String = "chara_card_v2",
    
    @SerialName("spec_version")
    val specVersion: String = "2.0",
    
    @SerialName("data")
    val data: CharacterData
) : Parcelable

@Serializable
@Parcelize
data class CharacterCardV3(
    @SerialName("spec")
    val spec: String = "chara_card_v3",
    
    @SerialName("spec_version")
    val specVersion: String = "3.0",
    
    @SerialName("data")
    val data: CharacterDataExtended
) : Parcelable

@Serializable
@Parcelize
data class CharacterData(
    @SerialName("name")
    val name: String = "",
    
    @SerialName("description")
    val description: String = "",
    
    @SerialName("personality")
    val personality: String = "",
    
    @SerialName("scenario")
    val scenario: String = "",
    
    @SerialName("first_mes")
    val firstMes: String = "",
    
    @SerialName("mes_example")
    val mesExample: String = "",
    
    @SerialName("creator_notes")
    val creatorNotes: String = "",
    
    @SerialName("system_prompt")
    val systemPrompt: String = "",
    
    @SerialName("post_history_instructions")
    val postHistoryInstructions: String = "",
    
    @SerialName("alternate_greetings")
    val alternateGreetings: List<String> = emptyList(),
    
    @SerialName("character_book")
    val characterBook: CharacterBook? = null,
    
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    
    @SerialName("creator")
    val creator: String = "",
    
    @SerialName("character_version")
    val characterVersion: String = "1.0",
    
    @SerialName("extensions")
    val extensions: Map<String, String> = emptyMap()
) : Parcelable

@Serializable
@Parcelize
data class CharacterDataExtended(
    @SerialName("name")
    val name: String = "",
    
    @SerialName("description")
    val description: String = "",
    
    @SerialName("personality")
    val personality: String = "",
    
    @SerialName("scenario")
    val scenario: String = "",
    
    @SerialName("first_mes")
    val firstMes: String = "",
    
    @SerialName("mes_example")
    val mesExample: String = "",
    
    @SerialName("creator_notes")
    val creatorNotes: String = "",
    
    @SerialName("system_prompt")
    val systemPrompt: String = "",
    
    @SerialName("post_history_instructions")
    val postHistoryInstructions: String = "",
    
    @SerialName("alternate_greetings")
    val alternateGreetings: List<String> = emptyList(),
    
    @SerialName("character_book")
    val characterBook: CharacterBook? = null,
    
    @SerialName("tags")
    val tags: List<String> = emptyList(),
    
    @SerialName("creator")
    val creator: String = "",
    
    @SerialName("character_version")
    val characterVersion: String = "1.0",
    
    @SerialName("extensions")
    val extensions: Map<String, String> = emptyMap(),
    
    @SerialName("nickname")
    val nickname: String? = null,
    
    @SerialName("source")
    val source: List<String> = emptyList(),
    
    @SerialName("avatar")
    val avatar: String = "none",
    
    @SerialName("created_at")
    val createdAt: Long = System.currentTimeMillis(),
    
    @SerialName("last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
) : Parcelable

@Serializable
@Parcelize
data class CharacterBook(
    @SerialName("name")
    val name: String = "",
    
    @SerialName("description")
    val description: String = "",
    
    @SerialName("scan_depth")
    val scanDepth: Int = 2,
    
    @SerialName("token_budget")
    val tokenBudget: Int = 2048,
    
    @SerialName("recursive_scanning")
    val recursiveScanning: Boolean = false,
    
    @SerialName("extensions")
    val extensions: Map<String, String> = emptyMap(),
    
    @SerialName("entries")
    val entries: List<WorldInfoEntry> = emptyList()
) : Parcelable

@Serializable
@Parcelize
data class WorldInfoEntry(
    @SerialName("keys")
    val keys: List<String> = emptyList(),
    
    @SerialName("content")
    val content: String = "",
    
    @SerialName("extensions")
    val extensions: Map<String, String> = emptyMap(),
    
    @SerialName("enabled")
    val enabled: Boolean = true,
    
    @SerialName("insertion_order")
    val insertionOrder: Int = 100,
    
    @SerialName("case_sensitive")
    val caseSensitive: Boolean = false,
    
    @SerialName("name")
    val name: String = "",
    
    @SerialName("priority")
    val priority: Int = 10,
    
    @SerialName("id")
    val id: Long = System.currentTimeMillis(),
    
    @SerialName("comment")
    val comment: String = "",
    
    @SerialName("selective")
    val selective: Boolean = false,
    
    @SerialName("secondary_keys")
    val secondaryKeys: List<String> = emptyList(),
    
    @SerialName("constant")
    val constant: Boolean = false,
    
    @SerialName("position")
    val position: String = "before_char",
    
    @SerialName("depth")
    val depth: Int = 4,
    
    @SerialName("group")
    val group: String = "",
    
    @SerialName("group_overlap")
    val groupOverlap: Boolean = false,
    
    @SerialName("group_weight")
    val groupWeight: Int = 100,
    
    @SerialName("useProbability")
    val useProbability: Boolean = false,
    
    @SerialName("probability")
    val probability: Int = 100,
    
    @SerialName("displayIndex")
    val displayIndex: Int = 0,
    
    @SerialName("excludeRecursion")
    val excludeRecursion: Boolean = false,
    
    @SerialName("preventRecursion")
    val preventRecursion: Boolean = false,
    
    @SerialName("delayUntilRecursion")
    val delayUntilRecursion: Boolean = false,
    
    @SerialName("scanDepth")
    val scanDepth: Int? = null,
    
    @SerialName("matchWholeWords")
    val matchWholeWords: Boolean = false,
    
    @SerialName("vectorized")
    val vectorized: Boolean = false,
    
    @SerialName("sticky")
    val sticky: Int = 0,
    
    @SerialName("cooldown")
    val cooldown: Int = 0,
    
    @SerialName("delay")
    val delay: Int = 0
) : Parcelable
