package com.sillytavern.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sillytavern.android.data.local.entity.CharacterEntity
import com.sillytavern.android.data.local.entity.ChatEntity
import com.sillytavern.android.data.repository.CharacterRepository
import com.sillytavern.android.data.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val chatRepository: ChatRepository
) : ViewModel() {
    
    private val _character = MutableStateFlow<CharacterEntity?>(null)
    val character: StateFlow<CharacterEntity?> = _character.asStateFlow()
    
    private val _chats = MutableStateFlow<List<ChatEntity>>(emptyList())
    val chats: StateFlow<List<ChatEntity>> = _chats.asStateFlow()
    
    private var currentCharacterId: Long = 0
    
    fun loadCharacter(characterId: Long) {
        currentCharacterId = characterId
        viewModelScope.launch {
            _character.value = characterRepository.getCharacterById(characterId)
            
            chatRepository.getChatsForCharacter(characterId).collect { chatList ->
                _chats.value = chatList
            }
        }
    }
    
    fun startNewChat() {
        viewModelScope.launch {
            val character = _character.value ?: return@launch
            val chatId = chatRepository.createChat(
                characterId = character.id,
                name = "New Chat"
            )
            characterRepository.updateChatStats(character.id)
        }
    }
    
    fun deleteCharacter() {
        viewModelScope.launch {
            _character.value?.let { character ->
                characterRepository.deleteCharacter(character)
            }
        }
    }
    
    fun exportCharacter() {
        // TODO: Implement character export
    }
}
