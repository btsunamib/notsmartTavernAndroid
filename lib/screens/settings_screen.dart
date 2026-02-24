import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/settings_provider.dart';

class SettingsScreen extends StatelessWidget {
  const SettingsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Settings'),
      ),
      body: Consumer<SettingsProvider>(
        builder: (context, provider, child) {
          final settings = provider.apiSettings;
          
          return ListView(
            padding: const EdgeInsets.all(16),
            children: [
              const Text(
                'API Settings',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 16),
              
              // API Type
              Card(
                child: ListTile(
                  title: const Text('API Type'),
                  subtitle: Text(settings.apiType),
                  trailing: const Icon(Icons.arrow_forward_ios, size: 16),
                  onTap: () => _showApiTypeDialog(context, provider),
                ),
              ),
              
              // API URL
              Card(
                child: ListTile(
                  title: const Text('API URL'),
                  subtitle: Text(settings.apiUrl),
                  trailing: const Icon(Icons.edit),
                  onTap: () => _showTextInputDialog(
                    context,
                    'API URL',
                    settings.apiUrl,
                    (value) => provider.setApiUrl(value),
                  ),
                ),
              ),
              
              // API Key
              Card(
                child: ListTile(
                  title: const Text('API Key'),
                  subtitle: Text(settings.apiKey.isEmpty ? 'Not set' : '******'),
                  trailing: const Icon(Icons.edit),
                  onTap: () => _showTextInputDialog(
                    context,
                    'API Key',
                    settings.apiKey,
                    (value) => provider.setApiKey(value),
                    obscure: true,
                  ),
                ),
              ),
              
              // Model
              Card(
                child: ListTile(
                  title: const Text('Model'),
                  subtitle: Text(settings.model),
                  trailing: const Icon(Icons.edit),
                  onTap: () => _showTextInputDialog(
                    context,
                    'Model',
                    settings.model,
                    (value) => provider.setModel(value),
                  ),
                ),
              ),
              
              const SizedBox(height: 24),
              
              // Generation Settings
              const Text(
                'Generation Settings',
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 16),
              
              // Temperature
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text('Temperature'),
                          Text(settings.temperature.toStringAsFixed(1)),
                        ],
                      ),
                      Slider(
                        value: settings.temperature,
                        min: 0.0,
                        max: 2.0,
                        divisions: 20,
                        onChanged: (value) => provider.setTemperature(value),
                      ),
                    ],
                  ),
                ),
              ),
              
              // Max Tokens
              Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          const Text('Max Tokens'),
                          Text('${settings.maxTokens}'),
                        ],
                      ),
                      Slider(
                        value: settings.maxTokens.toDouble(),
                        min: 100,
                        max: 4000,
                        divisions: 39,
                        onChanged: (value) => provider.setMaxTokens(value.toInt()),
                      ),
                    ],
                  ),
                ),
              ),
              
              // Streaming
              Card(
                child: SwitchListTile(
                  title: const Text('Streaming'),
                  subtitle: const Text('Stream AI responses'),
                  value: settings.streaming,
                  onChanged: (value) => provider.setStreamingEnabled(value),
                ),
              ),
              
              const SizedBox(height: 24),
              
              // Test Connection
              ElevatedButton.icon(
                icon: const Icon(Icons.wifi),
                label: const Text('Test Connection'),
                onPressed: () => _testConnection(context),
              ),
            ],
          );
        },
      ),
    );
  }

  void _showApiTypeDialog(BuildContext context, SettingsProvider provider) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Select API Type'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            ListTile(
              title: const Text('OpenAI'),
              onTap: () {
                provider.setApiType('openai');
                provider.setApiUrl('https://api.openai.com/v1');
                provider.setModel('gpt-3.5-turbo');
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text('KoboldAI'),
              onTap: () {
                provider.setApiType('kobold');
                provider.setApiUrl('http://localhost:5000');
                provider.setModel('default');
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text('Oobabooga'),
              onTap: () {
                provider.setApiType('oobabooga');
                provider.setApiUrl('http://localhost:5000/v1');
                provider.setModel('default');
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text('Claude (Anthropic)'),
              onTap: () {
                provider.setApiType('claude');
                provider.setApiUrl('https://api.anthropic.com');
                provider.setModel('claude-3-opus-20240229');
                Navigator.pop(context);
              },
            ),
            ListTile(
              title: const Text('Custom'),
              onTap: () {
                provider.setApiType('custom');
                Navigator.pop(context);
              },
            ),
          ],
        ),
      ),
    );
  }

  void _showTextInputDialog(
    BuildContext context,
    String title,
    String initialValue,
    Function(String) onSave, {
    bool obscure = false,
  }) {
    final controller = TextEditingController(text: initialValue);
    
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text(title),
        content: TextField(
          controller: controller,
          obscureText: obscure,
          decoration: InputDecoration(
            hintText: 'Enter $title',
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () {
              onSave(controller.text);
              Navigator.pop(context);
            },
            child: const Text('Save'),
          ),
        ],
      ),
    );
  }

  void _testConnection(BuildContext context) {
    showDialog(
      context: context,
      builder: (context) => const AlertDialog(
        content: Row(
          children: [
            CircularProgressIndicator(),
            SizedBox(width: 16),
            Text('Testing connection...'),
          ],
        ),
      ),
    );
    
    Future.delayed(const Duration(seconds: 2), () {
      Navigator.pop(context);
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Connection test not implemented yet')),
      );
    });
  }
}
