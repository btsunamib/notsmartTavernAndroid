package com.sillytavern.android.data.repository

import com.sillytavern.android.data.local.dao.ChatDao
import com.sillytavern.android.data.local.dao.MessageDao
import com.sillytavern.android.data.local.entity.ChatEntity
import com.sillytavern.android.data.local.entity.MessageEntity
import com.sillytavern.android.data.model.ChatMessage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val messageDao: MessageDao
) {
    fun getChatsForCharacter(characterId: Long): Flow<List<ChatEntity>> {
        return chatDao.getChatsForCharacter(characterId)
    }
    
    suspend fun getChatById(id: Long): ChatEntity? {
        return chatDao.getChatById(id)
    }
    
    suspend fun createChat(characterId: Long, name: String): Long {
        val chat = ChatEntity(
            characterId = characterId,
            name = name,
            chatId = java.util.UUID.randomUUID().toString()
        )
        return chatDao.insertChat(chat)
    }
    
    suspend fun updateChat(chat: ChatEntity) {
        chatDao.updateChat(chat)
    }
    
    suspend fun deleteChat(chat: ChatEntity) {
        chatDao.deleteChat(chat)
    }
    
    suspend fun deleteChatById(id: Long) {
        chatDao.deleteChatById(id)
    }
    
    fun getMessagesForChat(chatId: Long): Flow<List<MessageEntity>> {
        return messageDao.getMessagesForChat(chatId)
    }
    
    suspend fun getLastMessage(chatId: Long): MessageEntity? {
        return messageDao.getLastMessage(chatId)
    }
    
    suspend fun addMessage(chatId: Long, message: ChatMessage): Long {
        val entity = MessageEntity.fromChatMessage(chatId, message)
        val id = messageDao.insertMessage(entity)
        chatDao.updateChatStats(chatId)
        return id
    }
    
    suspend fun updateMessage(message: MessageEntity) {
        messageDao.updateMessage(message)
    }
    
    suspend fun updateMessageContent(id: Long, content: String) {
        messageDao.updateMessageContent(id, content)
    }
    
    suspend fun deleteMessage(message: MessageEntity) {
        messageDao.deleteMessage(message)
    }
    
    suspend fun deleteMessageById(id: Long) {
        messageDao.deleteMessageById(id)
    }
    
    suspend fun getMessageCount(chatId: Long): Int {
        return messageDao.getMessageCount(chatId)
    }
}
