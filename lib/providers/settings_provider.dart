import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/models.dart';

class SettingsProvider extends ChangeNotifier {
  ApiSettings _apiSettings = ApiSettings();
  String _theme = 'dark';
  bool _streamingEnabled = true;

  ApiSettings get apiSettings => _apiSettings;
  String get theme => _theme;
  bool get streamingEnabled => _streamingEnabled;

  SettingsProvider() {
    _loadSettings();
  }

  Future<void> _loadSettings() async {
    final prefs = await SharedPreferences.getInstance();
    final apiData = prefs.getString('api_settings');
    if (apiData != null) {
      _apiSettings = ApiSettings.fromJson(jsonDecode(apiData));
    }
    _theme = prefs.getString('theme') ?? 'dark';
    _streamingEnabled = prefs.getBool('streaming_enabled') ?? true;
    notifyListeners();
  }

  Future<void> _saveSettings() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('api_settings', jsonEncode(_apiSettings.toJson()));
    await prefs.setString('theme', _theme);
    await prefs.setBool('streaming_enabled', _streamingEnabled);
  }

  void updateApiSettings(ApiSettings settings) {
    _apiSettings = settings;
    _saveSettings();
    notifyListeners();
  }

  void setApiType(String type) {
    _apiSettings.apiType = type;
    _saveSettings();
    notifyListeners();
  }

  void setApiUrl(String url) {
    _apiSettings.apiUrl = url;
    _saveSettings();
    notifyListeners();
  }

  void setApiKey(String key) {
    _apiSettings.apiKey = key;
    _saveSettings();
    notifyListeners();
  }

  void setModel(String model) {
    _apiSettings.model = model;
    _saveSettings();
    notifyListeners();
  }

  void setTemperature(double temp) {
    _apiSettings.temperature = temp;
    _saveSettings();
    notifyListeners();
  }

  void setMaxTokens(int tokens) {
    _apiSettings.maxTokens = tokens;
    _saveSettings();
    notifyListeners();
  }

  void setStreamingEnabled(bool enabled) {
    _streamingEnabled = enabled;
    _saveSettings();
    notifyListeners();
  }

  void setTheme(String theme) {
    _theme = theme;
    _saveSettings();
    notifyListeners();
  }
}
