import 'dart:convert';
import 'package:http/http.dart' as http;

class ApiService {
  String _baseUrl = 'http://localhost:8000';
  String _apiKey = '';
  String _apiType = 'openai';
  String _model = 'gpt-3.5-turbo';

  void updateSettings({
    String? baseUrl,
    String? apiKey,
    String? apiType,
    String? model,
  }) {
    if (baseUrl != null) _baseUrl = baseUrl;
    if (apiKey != null) _apiKey = apiKey;
    if (apiType != null) _apiType = apiType;
    if (model != null) _model = model;
  }

  Future<String> sendMessage(List<Map<String, String>> messages) async {
    try {
      final response = await http.post(
        Uri.parse('$_baseUrl/v1/chat/completions'),
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer $_apiKey',
        },
        body: jsonEncode({
          'model': _model,
          'messages': messages,
          'stream': false,
        }),
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        return data['choices'][0]['message']['content'];
      } else {
        throw Exception('API Error: ${response.statusCode} - ${response.body}');
      }
    } catch (e) {
      throw Exception('Connection failed: $e');
    }
  }

  Stream<String> sendMessageStream(List<Map<String, String>> messages) async* {
    try {
      final request = http.Request(
        'POST',
        Uri.parse('$_baseUrl/v1/chat/completions'),
      );
      request.headers['Content-Type'] = 'application/json';
      request.headers['Authorization'] = 'Bearer $_apiKey';
      request.body = jsonEncode({
        'model': _model,
        'messages': messages,
        'stream': true,
      });

      final streamedResponse = await http.Client().send(request);
      
      await for (final chunk in streamedResponse.stream.transform(utf8.decoder).transform(const LineSplitter())) {
        if (chunk.startsWith('data: ')) {
          final data = chunk.substring(6);
          if (data == '[DONE]') break;
          try {
            final json = jsonDecode(data);
            final content = json['choices'][0]['delta']['content'];
            if (content != null) {
              yield content;
            }
          } catch (_) {}
        }
      }
    } catch (e) {
      yield* Stream.error(Exception('Stream failed: $e'));
    }
  }

  Future<Map<String, dynamic>> getModels() async {
    try {
      final response = await http.get(
        Uri.parse('$_baseUrl/v1/models'),
        headers: {
          'Authorization': 'Bearer $_apiKey',
        },
      );
      
      if (response.statusCode == 200) {
        return jsonDecode(response.body);
      }
      return {};
    } catch (e) {
      return {};
    }
  }
}
