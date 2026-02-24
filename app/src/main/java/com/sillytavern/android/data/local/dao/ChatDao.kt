package com.sillytavern.android.data.local.dao

import androidx.room.*
import com.sillytavern.android.data.local.entity.ChatEntity
import com.sillytavern.android.data.local.entity.MessageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chats WHERE characterId = :characterId ORDER BY lastModified DESC")
    fun getChatsForCharacter(characterId: Long): Flow<List<ChatEntity>>
    
    @Query("SELECT * FROM chats WHERE id = :id")
    suspend fun getChatById(id: Long): ChatEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatEntity): Long
    
    @Update
    suspend fun updateChat(chat: ChatEntity)
    
    @Delete
    suspend fun deleteChat(chat: ChatEntity)
    
    @Query("DELETE FROM chats WHERE id = :id")
    suspend fun deleteChatById(id: Long)
    
    @Query("DELETE FROM chats WHERE characterId = :characterId")
    suspend fun deleteChatsForCharacter(characterId: Long)
    
    @Query("UPDATE chats SET lastModified = :lastModified, messageCount = messageCount + 1 WHERE id = :id")
    suspend fun updateChatStats(id: Long, lastModified: Long = System.currentTimeMillis())
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY displayIndex ASC, sendDate ASC")
    fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>>
    
    @Query("SELECT * FROM messages WHERE id = :id")
    suspend fun getMessageById(id: Long): MessageEntity?
    
    @Query("SELECT * FROM messages WHERE chatId = :chatId ORDER BY displayIndex DESC, sendDate DESC LIMIT 1")
    suspend fun getLastMessage(chatId: Long): MessageEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity): Long
    
    @Update
    suspend fun updateMessage(message: MessageEntity)
    
    @Delete
    suspend fun deleteMessage(message: MessageEntity)
    
    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun deleteMessageById(id: Long)
    
    @Query("DELETE FROM messages WHERE chatId = :chatId")
    suspend fun deleteMessagesForChat(chatId: Long)
    
    @Query("SELECT COUNT(*) FROM messages WHERE chatId = :chatId")
    suspend fun getMessageCount(chatId: Long): Int
    
    @Query("UPDATE messages SET message = :message WHERE id = :id")
    suspend fun updateMessageContent(id: Long, message: String)
    
    @Transaction
    suspend fun insertMessages(messages: List<MessageEntity>) {
        messages.forEach { insertMessage(it) }
    }
}
