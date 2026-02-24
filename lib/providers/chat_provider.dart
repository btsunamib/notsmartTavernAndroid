import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';
import '../models/models.dart';
import '../services/api_service.dart';
import 'package:provider/provider.dart';

class ChatProvider extends ChangeNotifier {
  final List<Chat> _chats = [];
  final List<Message> _messages = [];
  Chat? _currentChat;
  bool _isGenerating = false;
  String _streamingContent = '';

  List<Chat> get chats => _chats;
  List<Message> get messages => _messages;
  Chat? get currentChat => _currentChat;
  bool get isGenerating => _isGenerating;
  String get streamingContent => _streamingContent;

  ChatProvider() {
    _loadChats();
  }

  Future<void> _loadChats() async {
    final prefs = await SharedPreferences.getInstance();
    final data = prefs.getString('chats');
    if (data != null) {
      final List<dynamic> jsonList = jsonDecode(data);
      _chats.clear();
      _chats.addAll(jsonList.map((e) => Chat.fromJson(e)));
      notifyListeners();
    }
  }

  Future<void> _saveChats() async {
    final prefs = await SharedPreferences.getInstance();
    final data = jsonEncode(_chats.map((e) => e.toJson()).toList());
    await prefs.setString('chats', data);
  }

  void createChat(String characterId, String name) {
    final chat = Chat(
      id: const Uuid().v4(),
      characterId: characterId,
      name: name,
    );
    _chats.add(chat);
    _currentChat = chat;
    _messages.clear();
    _saveChats();
    notifyListeners();
  }

  void selectChat(Chat chat) {
    _currentChat = chat;
    _loadMessages(chat.id);
    notifyListeners();
  }

  Future<void> _loadMessages(String chatId) async {
    final prefs = await SharedPreferences.getInstance();
    final key = 'messages_$chatId';
    final data = prefs.getString(key);
    _messages.clear();
    if (data != null) {
      final List<dynamic> jsonList = jsonDecode(data);
      _messages.addAll(jsonList.map((e) => Message.fromJson(e)));
    }
    notifyListeners();
  }

  Future<void> _saveMessages(String chatId) async {
    final prefs = await SharedPreferences.getInstance();
    final key = 'messages_$chatId';
    final data = jsonEncode(_messages.map((e) => e.toJson()).toList());
    await prefs.setString(key, data);
  }

  void addMessage(Message message) {
    _messages.add(message);
    if (_currentChat != null) {
      _saveMessages(_currentChat!.id);
    }
    notifyListeners();
  }

  Future<void> sendMessage(String content, Character character, ApiService api, String systemPrompt) async {
    if (_currentChat == null) return;

    final userMessage = Message(
      id: const Uuid().v4(),
      chatId: _currentChat!.id,
      senderName: 'You',
      isUser: true,
      content: content,
    );
    addMessage(userMessage);

    _isGenerating = true;
    _streamingContent = '';
    notifyListeners();

    final messages = <Map<String, String>>[];
    
    messages.add({
      'role': 'system',
      'content': 'You are ${character.name}. ${character.description}',
    });

    if (character.firstMes.isNotEmpty) {
      messages.add({
        'role': 'assistant',
        'content': character.firstMes,
      });
    }

    for (final msg in _messages) {
      messages.add({
        'role': msg.isUser ? 'user' : 'assistant',
        'content': '${msg.senderName}: ${msg.content}',
      });
    }

    try {
      final response = await api.sendMessage(messages);
      
      final assistantMessage = Message(
        id: const Uuid().v4(),
        chatId: _currentChat!.id,
        senderName: character.name,
        isUser: false,
        content: response,
      );
      addMessage(assistantMessage);
    } catch (e) {
      final errorMessage = Message(
        id: const Uuid().v4(),
        chatId: _currentChat!.id,
        senderName: 'System',
        isUser: false,
        content: 'Error: $e',
      );
      addMessage(errorMessage);
    }

    _isGenerating = false;
    _streamingContent = '';
    notifyListeners();
  }

  void deleteChat(String id) {
    _chats.removeWhere((c) => c.id == id);
    if (_currentChat?.id == id) {
      _currentChat = null;
      _messages.clear();
    }
    _saveChats();
    notifyListeners();
  }
}
