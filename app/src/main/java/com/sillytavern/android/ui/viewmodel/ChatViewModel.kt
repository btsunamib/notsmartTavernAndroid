package com.sillytavern.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillytavern.android.data.local.entity.CharacterEntity
import com.sillytavern.android.data.local.entity.MessageEntity
import com.sillytavern.android.data.model.ChatMessage
import com.sillytavern.android.data.model.OpenAIMessage
import com.sillytavern.android.data.model.OpenAIRequest
import com.sillytavern.android.data.repository.CharacterRepository
import com.sillytavern.android.data.repository.ChatRepository
import com.sillytavern.android.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val chatRepository: ChatRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _character = MutableStateFlow<CharacterEntity?>(null)
    val character: StateFlow<CharacterEntity?> = _character.asStateFlow()
    
    private val _messages = MutableStateFlow<List<MessageEntity>>(emptyList())
    val messages: StateFlow<List<MessageEntity>> = _messages.asStateFlow()
    
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()
    
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()
    
    private val _streamingText = MutableStateFlow("")
    val streamingText: StateFlow<String> = _streamingText.asStateFlow()
    
    private var currentChatId: Long? = null
    private var generationJob: Job? = null
    
    fun loadChat(characterId: Long, chatId: Long?) {
        viewModelScope.launch {
            _character.value = characterRepository.getCharacterById(characterId)
            
            currentChatId = chatId ?: run {
                characterRepository.getCharacterById(characterId)?.let { char ->
                    chatRepository.createChat(characterId, "New Chat")
                }
            }
            
            currentChatId?.let { id ->
                chatRepository.getMessagesForChat(id).collect { msgs ->
                    _messages.value = msgs
                }
            }
        }
    }
    
    fun setInputText(text: String) {
        _inputText.value = text
    }
    
    fun sendMessage() {
        val text = _inputText.value.trim()
        if (text.isEmpty() || _isGenerating.value) return
        
        val characterEntity = _character.value ?: return
        val chatId = currentChatId ?: return
        
        viewModelScope.launch {
            _inputText.value = ""
            
            val userMessage = ChatMessage(
                name = "You",
                isUser = true,
                message = text
            )
            chatRepository.addMessage(chatId, userMessage)
            
            generateResponse(characterEntity)
        }
    }
    
    private fun generateResponse(character: CharacterEntity) {
        if (_isGenerating.value) return
        
        _isGenerating.value = true
        _streamingText.value = ""
        
        generationJob = viewModelScope.launch {
            try {
                val messages = buildPromptMessages(character)
                
                val request = OpenAIRequest(
                    model = "gpt-4",
                    messages = messages,
                    stream = true,
                    maxTokens = 500
                )
                
                settingsRepository.generateResponse(
                    request = request,
                    onToken = { token ->
                        _streamingText.value += token
                    },
                    onComplete = {
                        viewModelScope.launch {
                            saveAssistantMessage(_streamingText.value, character.name)
                        }
                        _isGenerating.value = false
                        _streamingText.value = ""
                    },
                    onError = { error ->
                        _isGenerating.value = false
                        _streamingText.value = ""
                        error.printStackTrace()
                    }
                )
            } catch (e: Exception) {
                _isGenerating.value = false
                _streamingText.value = ""
                e.printStackTrace()
            }
        }
    }
    
    private fun buildPromptMessages(character: CharacterEntity): List<OpenAIMessage> {
        val systemPrompt = buildString {
            append("You are ${character.name}")
            if (character.description.isNotEmpty()) {
                append(". ${character.description}")
            }
            if (character.personality.isNotEmpty()) {
                append("\n\nPersonality: ${character.personality}")
            }
            if (character.scenario.isNotEmpty()) {
                append("\n\nScenario: ${character.scenario}")
            }
            append("\n\nWrite ${character.name}'s next reply in a fictional chat between ${character.name} and the user.")
        }
        
        val messages = mutableListOf<OpenAIMessage>()
        messages.add(OpenAIMessage(role = "system", content = systemPrompt))
        
        if (character.firstMes.isNotEmpty()) {
            messages.add(OpenAIMessage(
                role = "assistant",
                content = character.firstMes,
                name = character.name
            ))
        }
        
        _messages.value.takeLast(20).forEach { msg ->
            messages.add(OpenAIMessage(
                role = if (msg.isUser) "user" else "assistant",
                content = msg.message,
                name = if (msg.isUser) "User" else character.name
            ))
        }
        
        return messages
    }
    
    private suspend fun saveAssistantMessage(text: String, characterName: String) {
        val chatId = currentChatId ?: return
        
        val message = ChatMessage(
            name = characterName,
            isUser = false,
            message = text
        )
        chatRepository.addMessage(chatId, message)
    }
    
    fun stopGeneration() {
        generationJob?.cancel()
        generationJob = null
        
        if (_streamingText.value.isNotEmpty()) {
            viewModelScope.launch {
                saveAssistantMessage(_streamingText.value, _character.value?.name ?: "Assistant")
            }
        }
        
        _isGenerating.value = false
        _streamingText.value = ""
    }
    
    fun regenerateLast() {
        val lastAssistantMessage = _messages.value.lastOrNull { !it.isUser } ?: return
        
        viewModelScope.launch {
            chatRepository.deleteMessageById(lastAssistantMessage.id)
            _character.value?.let { generateResponse(it) }
        }
    }
    
    fun continueGeneration() {
        if (_isGenerating.value) return
        _character.value?.let { generateResponse(it) }
    }
    
    fun swipe(direction: Int) {
        // TODO: Implement swipe functionality for alternative responses
    }
    
    override fun onCleared() {
        super.onCleared()
        generationJob?.cancel()
    }
}
