package com.sillytavern.android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sillytavern.android.data.model.CharacterDataExtended
import com.sillytavern.android.data.model.CharacterBook

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val name: String,
    val description: String = "",
    val personality: String = "",
    val scenario: String = "",
    val firstMes: String = "",
    val mesExample: String = "",
    val creatorNotes: String = "",
    val systemPrompt: String = "",
    val postHistoryInstructions: String = "",
    val alternateGreetings: String = "",
    val tags: String = "",
    val creator: String = "",
    val characterVersion: String = "1.0",
    val extensions: String = "",
    val nickname: String? = null,
    val source: String = "",
    val avatarPath: String? = null,
    val avatarUrl: String? = null,
    val characterBookJson: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastUpdated: Long = System.currentTimeMillis(),
    val lastChatAt: Long = 0,
    val chatCount: Int = 0,
    val isFavorite: Boolean = false,
    val customAvatar: String? = null
) {
    fun toCharacterData(): CharacterDataExtended {
        return CharacterDataExtended(
            name = name,
            description = description,
            personality = personality,
            scenario = scenario,
            firstMes = firstMes,
            mesExample = mesExample,
            creatorNotes = creatorNotes,
            systemPrompt = systemPrompt,
            postHistoryInstructions = postHistoryInstructions,
            alternateGreetings = if (alternateGreetings.isNotEmpty()) alternateGreetings.split("|||") else emptyList(),
            tags = if (tags.isNotEmpty()) tags.split(",") else emptyList(),
            creator = creator,
            characterVersion = characterVersion,
            nickname = nickname,
            source = if (source.isNotEmpty()) source.split(",") else emptyList(),
            createdAt = createdAt,
            lastUpdated = lastUpdated
        )
    }
    
    companion object {
        fun fromCharacterData(data: CharacterDataExtended, avatarPath: String? = null): CharacterEntity {
            return CharacterEntity(
                name = data.name,
                description = data.description,
                personality = data.personality,
                scenario = data.scenario,
                firstMes = data.firstMes,
                mesExample = data.mesExample,
                creatorNotes = data.creatorNotes,
                systemPrompt = data.systemPrompt,
                postHistoryInstructions = data.postHistoryInstructions,
                alternateGreetings = data.alternateGreetings.joinToString("|||"),
                tags = data.tags.joinToString(","),
                creator = data.creator,
                characterVersion = data.characterVersion,
                nickname = data.nickname,
                source = data.source.joinToString(","),
                avatarPath = avatarPath,
                createdAt = data.createdAt,
                lastUpdated = data.lastUpdated
            )
        }
    }
}
