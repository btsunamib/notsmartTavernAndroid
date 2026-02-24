import 'dart:convert';
import 'dart:io';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';
import '../models/models.dart';

class CharacterProvider extends ChangeNotifier {
  List<Character> _characters = [];
  Character? _selectedCharacter;

  List<Character> get characters => _characters;
  Character? get selectedCharacter => _selectedCharacter;

  CharacterProvider() {
    _loadCharacters();
  }

  Future<void> _loadCharacters() async {
    final prefs = await SharedPreferences.getInstance();
    final data = prefs.getString('characters');
    if (data != null) {
      final List<dynamic> jsonList = jsonDecode(data);
      _characters = jsonList.map((e) => Character.fromJson(e)).toList();
      notifyListeners();
    }
  }

  Future<void> _saveCharacters() async {
    final prefs = await SharedPreferences.getInstance();
    final data = jsonEncode(_characters.map((e) => e.toJson()).toList());
    await prefs.setString('characters', data);
  }

  void addCharacter(Character character) {
    _characters.add(character);
    _saveCharacters();
    notifyListeners();
  }

  void updateCharacter(Character character) {
    final index = _characters.indexWhere((c) => c.id == character.id);
    if (index != -1) {
      _characters[index] = character;
      _saveCharacters();
      notifyListeners();
    }
  }

  void deleteCharacter(String id) {
    _characters.removeWhere((c) => c.id == id);
    if (_selectedCharacter?.id == id) {
      _selectedCharacter = null;
    }
    _saveCharacters();
    notifyListeners();
  }

  void selectCharacter(Character? character) {
    _selectedCharacter = character;
    notifyListeners();
  }

  Future<Character?> importCharacter(File file) async {
    try {
      final content = await file.readAsString();
      final json = jsonDecode(content);
      
      final character = Character(
        id: const Uuid().v4(),
        name: json['name'] ?? 'Unknown',
        description: json['description'] ?? '',
        personality: json['personality'] ?? '',
        scenario: json['scenario'] ?? '',
        firstMes: json['first_mes'] ?? '',
        mesExample: json['mes_example'] ?? '',
        characterBook: json['character_book'],
        tags: json['tags'] != null ? List<String>.from(json['tags']) : null,
      );
      
      addCharacter(character);
      return character;
    } catch (e) {
      debugPrint('Error importing character: $e');
      return null;
    }
  }
}
