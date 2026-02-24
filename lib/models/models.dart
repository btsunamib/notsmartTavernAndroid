class Character {
  final String id;
  String name;
  String description;
  String personality;
  String scenario;
  String firstMes;
  String mesExample;
  String? avatarPath;
  Map<String, dynamic>? characterBook;
  List<String>? tags;
  DateTime createdAt;

  Character({
    required this.id,
    required this.name,
    this.description = '',
    this.personality = '',
    this.scenario = '',
    this.firstMes = '',
    this.mesExample = '',
    this.avatarPath,
    this.characterBook,
    this.tags,
    DateTime? createdAt,
  }) : createdAt = createdAt ?? DateTime.now();

  Map<String, dynamic> toJson() => {
    'id': id,
    'name': name,
    'description': description,
    'personality': personality,
    'scenario': scenario,
    'first_mes': firstMes,
    'mes_example': mesExample,
    'avatar_path': avatarPath,
    'character_book': characterBook,
    'tags': tags,
    'created_at': createdAt.toIso8601String(),
  };

  factory Character.fromJson(Map<String, dynamic> json) => Character(
    id: json['id'] ?? '',
    name: json['name'] ?? '',
    description: json['description'] ?? '',
    personality: json['personality'] ?? '',
    scenario: json['scenario'] ?? '',
    firstMes: json['first_mes'] ?? '',
    mesExample: json['mes_example'] ?? '',
    avatarPath: json['avatar_path'],
    characterBook: json['character_book'],
    tags: json['tags'] != null ? List<String>.from(json['tags']) : null,
    createdAt: json['created_at'] != null 
      ? DateTime.parse(json['created_at']) 
      : DateTime.now(),
  );
}

class Chat {
  final String id;
  final String characterId;
  String name;
  DateTime createdAt;
  DateTime updatedAt;

  Chat({
    required this.id,
    required this.characterId,
    required this.name,
    DateTime? createdAt,
    DateTime? updatedAt,
  }) : createdAt = createdAt ?? DateTime.now(),
       updatedAt = updatedAt ?? DateTime.now();

  Map<String, dynamic> toJson() => {
    'id': id,
    'character_id': characterId,
    'name': name,
    'created_at': createdAt.toIso8601String(),
    'updated_at': updatedAt.toIso8601String(),
  };

  factory Chat.fromJson(Map<String, dynamic> json) => Chat(
    id: json['id'],
    characterId: json['character_id'],
    name: json['name'],
    createdAt: json['created_at'] != null 
      ? DateTime.parse(json['created_at']) 
      : DateTime.now(),
    updatedAt: json['updated_at'] != null 
      ? DateTime.parse(json['updated_at']) 
      : DateTime.now(),
  );
}

class Message {
  final String id;
  final String chatId;
  final String senderName;
  final bool isUser;
  String content;
  DateTime timestamp;

  Message({
    required this.id,
    required this.chatId,
    required this.senderName,
    required this.isUser,
    required this.content,
    DateTime? timestamp,
  }) : timestamp = timestamp ?? DateTime.now();

  Map<String, dynamic> toJson() => {
    'id': id,
    'chat_id': chatId,
    'sender_name': senderName,
    'is_user': isUser,
    'content': content,
    'timestamp': timestamp.toIso8601String(),
  };

  factory Message.fromJson(Map<String, dynamic> json) => Message(
    id: json['id'],
    chatId: json['chat_id'],
    senderName: json['sender_name'],
    isUser: json['is_user'],
    content: json['content'],
    timestamp: json['timestamp'] != null 
      ? DateTime.parse(json['timestamp']) 
      : DateTime.now(),
  );
}

class WorldInfoEntry {
  final String id;
  final String worldInfoId;
  String name;
  List<String> keys;
  String content;
  int priority;
  int depth;
  bool enabled;

  WorldInfoEntry({
    required this.id,
    required this.worldInfoId,
    this.name = '',
    this.keys = const [],
    this.content = '',
    this.priority = 0,
    this.depth = 0,
    this.enabled = true,
  });

  Map<String, dynamic> toJson() => {
    'id': id,
    'world_info_id': worldInfoId,
    'name': name,
    'keys': keys,
    'content': content,
    'priority': priority,
    'depth': depth,
    'enabled': enabled,
  };

  factory WorldInfoEntry.fromJson(Map<String, dynamic> json) => WorldInfoEntry(
    id: json['id'],
    worldInfoId: json['world_info_id'],
    name: json['name'] ?? '',
    keys: json['keys'] != null ? List<String>.from(json['keys']) : [],
    content: json['content'] ?? '',
    priority: json['priority'] ?? 0,
    depth: json['depth'] ?? 0,
    enabled: json['enabled'] ?? true,
  );
}

class Preset {
  final String id;
  String name;
  String content;
  String type;

  Preset({
    required this.id,
    required this.name,
    required this.content,
    this.type = 'default',
  });

  Map<String, dynamic> toJson() => {
    'id': id,
    'name': name,
    'content': content,
    'type': type,
  };

  factory Preset.fromJson(Map<String, dynamic> json) => Preset(
    id: json['id'],
    name: json['name'],
    content: json['content'],
    type: json['type'] ?? 'default',
  );
}

class ApiSettings {
  String apiType;
  String apiUrl;
  String apiKey;
  String model;
  double temperature;
  int maxTokens;
  double topP;
  bool streaming;

  ApiSettings({
    this.apiType = 'openai',
    this.apiUrl = 'https://api.openai.com/v1',
    this.apiKey = '',
    this.model = 'gpt-3.5-turbo',
    this.temperature = 0.8,
    this.maxTokens = 500,
    this.topP = 0.9,
    this.streaming = true,
  });

  Map<String, dynamic> toJson() => {
    'api_type': apiType,
    'api_url': apiUrl,
    'api_key': apiKey,
    'model': model,
    'temperature': temperature,
    'max_tokens': maxTokens,
    'top_p': topP,
    'streaming': streaming,
  };

  factory ApiSettings.fromJson(Map<String, dynamic> json) => ApiSettings(
    apiType: json['api_type'] ?? 'openai',
    apiUrl: json['api_url'] ?? 'https://api.openai.com/v1',
    apiKey: json['api_key'] ?? '',
    model: json['model'] ?? 'gpt-3.5-turbo',
    temperature: (json['temperature'] ?? 0.8).toDouble(),
    maxTokens: json['max_tokens'] ?? 500,
    topP: (json['top_p'] ?? 0.9).toDouble(),
    streaming: json['streaming'] ?? true,
  );
}
