package com.sillytavern.android.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.sillytavern.android.data.model.ChatMessage
import com.sillytavern.android.data.model.Swipe
import com.sillytavern.android.data.model.MessageExtra
import com.sillytavern.android.data.model.MessageMetadata

@Entity(
    tableName = "chats",
    foreignKeys = [
        ForeignKey(
            entity = CharacterEntity::class,
            parentColumns = ["id"],
            childColumns = ["characterId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["characterId"])]
)
data class ChatEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val characterId: Long,
    val name: String,
    val chatId: String,
    val createDate: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val messageCount: Int = 0,
    val isGroup: Boolean = false,
    val groupId: String? = null,
    val note: String = "",
    val notePosition: Int = 0,
    val noteDepth: Int = 4,
    val worldInfoIds: String = ""
)

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["id"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chatId"])]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    val chatId: Long,
    val messageId: Long = System.currentTimeMillis(),
    val name: String,
    val isUser: Boolean,
    val isName: Boolean = false,
    val sendDate: Long = System.currentTimeMillis(),
    val message: String,
    val swipeId: Int = 0,
    val swipesJson: String = "",
    val extraJson: String? = null,
    val metadataJson: String? = null,
    val displayIndex: Int = 0
) {
    fun toChatMessage(): ChatMessage {
        return ChatMessage(
            name = name,
            isUser = isUser,
            isName = isName,
            sendDate = sendDate,
            message = message,
            swipeId = swipeId,
            id = messageId
        )
    }
    
    companion object {
        fun fromChatMessage(chatId: Long, msg: ChatMessage): MessageEntity {
            return MessageEntity(
                chatId = chatId,
                messageId = msg.id,
                name = msg.name,
                isUser = msg.isUser,
                isName = msg.isName,
                sendDate = msg.sendDate,
                message = msg.message,
                swipeId = msg.swipeId
            )
        }
    }
}
