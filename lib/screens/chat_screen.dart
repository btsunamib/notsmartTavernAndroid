import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/chat_provider.dart';
import '../providers/settings_provider.dart';
import '../models/models.dart';
import '../services/api_service.dart';

class ChatScreen extends StatefulWidget {
  final Character character;

  const ChatScreen({super.key, required this.character});

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  final _messageController = TextEditingController();
  final _scrollController = ScrollController();

  @override
  void dispose() {
    _messageController.dispose();
    _scrollController.dispose();
    super.dispose();
  }

  void _sendMessage() {
    final content = _messageController.text.trim();
    if (content.isEmpty) return;

    final chatProvider = context.read<ChatProvider>();
    final settingsProvider = context.read<SettingsProvider>();
    final apiService = context.read<ApiService>();

    apiService.updateSettings(
      baseUrl: settingsProvider.apiSettings.apiUrl,
      apiKey: settingsProvider.apiSettings.apiKey,
      apiType: settingsProvider.apiSettings.apiType,
      model: settingsProvider.apiSettings.model,
    );

    final systemPrompt = '''
You are ${widget.character.name}.
${widget.character.description}
${widget.character.personality.isNotEmpty ? '\nPersonality: ${widget.character.personality}' : ''}
${widget.character.scenario.isNotEmpty ? '\nScenario: ${widget.character.scenario}' : ''}
Write ${widget.character.name}'s response in the conversation.
''';

    chatProvider.sendMessage(
      content,
      widget.character,
      apiService,
      systemPrompt,
    );

    _messageController.clear();
    _scrollToBottom();
  }

  void _scrollToBottom() {
    Future.delayed(const Duration(milliseconds: 100), () {
      if (_scrollController.hasClients) {
        _scrollController.animateTo(
          _scrollController.position.maxScrollExtent,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeOut,
        );
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.character.name),
        actions: [
          IconButton(
            icon: const Icon(Icons.info_outline),
            onPressed: () => _showCharacterInfo(context),
          ),
        ],
      ),
      body: Column(
        children: [
          Expanded(
            child: Consumer<ChatProvider>(
              builder: (context, provider, child) {
                final messages = provider.messages;
                
                if (messages.isEmpty) {
                  return Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        if (widget.character.firstMes.isNotEmpty) ...[
                          Container(
                            margin: const EdgeInsets.all(16),
                            padding: const EdgeInsets.all(16),
                            decoration: BoxDecoration(
                              color: Theme.of(context).colorScheme.surface,
                              borderRadius: BorderRadius.circular(12),
                            ),
                            child: Text(
                              widget.character.firstMes,
                              style: const TextStyle(fontSize: 16),
                            ),
                          ),
                          const SizedBox(height: 16),
                          const Text(
                            'Send a message to start the conversation!',
                            style: TextStyle(color: Colors.grey),
                          ),
                        ] else ...[
                          const Text(
                            'Send a message to start!',
                            style: TextStyle(color: Colors.grey),
                          ),
                        ],
                      ],
                    ),
                  );
                }

                return ListView.builder(
                  controller: _scrollController,
                  padding: const EdgeInsets.all(16),
                  itemCount: messages.length,
                  itemBuilder: (context, index) {
                    final message = messages[index];
                    final isUser = message.isUser;
                    
                    return Align(
                      alignment: isUser 
                        ? Alignment.centerRight 
                        : Alignment.centerLeft,
                      child: Container(
                        margin: const EdgeInsets.only(bottom: 8),
                        padding: const EdgeInsets.all(12),
                        constraints: BoxConstraints(
                          maxWidth: MediaQuery.of(context).size.width * 0.75,
                        ),
                        decoration: BoxDecoration(
                          color: isUser 
                            ? Theme.of(context).primaryColor 
                            : Theme.of(context).colorScheme.surface,
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              message.senderName,
                              style: TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 12,
                                color: isUser 
                                  ? Colors.white70 
                                  : Theme.of(context).primaryColor,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              message.content,
                              style: TextStyle(
                                color: isUser ? Colors.white : null,
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                );
              },
            ),
          ),
          Consumer<ChatProvider>(
            builder: (context, provider, child) {
              if (provider.isGenerating) {
                return Container(
                  padding: const EdgeInsets.all(16),
                  child: Row(
                    children: [
                      const SizedBox(
                        width: 20,
                        height: 20,
                        child: CircularProgressIndicator(strokeWidth: 2),
                      ),
                      const SizedBox(width: 12),
                      const Text('Generating...'),
                      const Spacer(),
                      TextButton(
                        onPressed: () {
                        },
                        child: const Text('Stop'),
                      ),
                    ],
                  ),
                );
              }
              return Container(
                padding: const EdgeInsets.all(8),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.surface,
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black.withOpacity(0.1),
                      blurRadius: 4,
                      offset: const Offset(0, -2),
                    ),
                  ],
                ),
                child: Row(
                  children: [
                    Expanded(
                      child: TextField(
                        controller: _messageController,
                        decoration: const InputDecoration(
                          hintText: 'Type a message...',
                          border: InputBorder.none,
                          contentPadding: EdgeInsets.symmetric(horizontal: 16),
                        ),
                        maxLines: null,
                        textInputAction: TextInputAction.send,
                        onSubmitted: (_) => _sendMessage(),
                      ),
                    ),
                    IconButton(
                      icon: const Icon(Icons.send),
                      onPressed: _sendMessage,
                    ),
                  ],
                ),
              );
            },
          ),
        ],
      ),
    );
  }

  void _showCharacterInfo(BuildContext context) {
    showModalBottomSheet(
      context: context,
      builder: (context) => Container(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          mainAxisSize: MainAxisSize.min,
          children: [
            Text(
              widget.character.name,
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            if (widget.character.description.isNotEmpty) ...[
              const Text('Description:', style: TextStyle(fontWeight: FontWeight.bold)),
              Text(widget.character.description),
              const SizedBox(height: 12),
            ],
            if (widget.character.personality.isNotEmpty) ...[
              const Text('Personality:', style: TextStyle(fontWeight: FontWeight.bold)),
              Text(widget.character.personality),
              const SizedBox(height: 12),
            ],
            if (widget.character.scenario.isNotEmpty) ...[
              const Text('Scenario:', style: TextStyle(fontWeight: FontWeight.bold)),
              Text(widget.character.scenario),
            ],
          ],
        ),
      ),
    );
  }
}
