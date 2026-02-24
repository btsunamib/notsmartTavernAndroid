import 'dart:io';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:image_picker/image_picker.dart';
import 'package:uuid/uuid.dart';
import '../providers/character_provider.dart';
import '../models/models.dart';

class CharactersScreen extends StatelessWidget {
  const CharactersScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Characters'),
      ),
      body: Consumer<CharacterProvider>(
        builder: (context, provider, child) {
          final characters = provider.characters;
          
          if (characters.isEmpty) {
            return const Center(
              child: Text('No characters imported yet'),
            );
          }

          return ListView.builder(
            padding: const EdgeInsets.all(16),
            itemCount: characters.length,
            itemBuilder: (context, index) {
              final character = characters[index];
              return Card(
                margin: const EdgeInsets.only(bottom: 12),
                child: ListTile(
                  leading: const CircleAvatar(
                    child: Icon(Icons.person),
                  ),
                  title: Text(character.name),
                  subtitle: Text(character.description),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      IconButton(
                        icon: const Icon(Icons.delete),
                        onPressed: () {
                          provider.deleteCharacter(character.id);
                        },
                      ),
                    ],
                  ),
                  onTap: () {
                    provider.selectCharacter(character);
                  },
                ),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showImportDialog(context),
        child: const Icon(Icons.add),
      ),
    );
  }

  void _showImportDialog(BuildContext context) {
    final nameController = TextEditingController();
    final descController = TextEditingController();
    final personaController = TextEditingController();
    final scenarioController = TextEditingController();
    final firstMesController = TextEditingController();

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Create Character'),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: nameController,
                decoration: const InputDecoration(labelText: 'Name'),
              ),
              const SizedBox(height: 8),
              TextField(
                controller: descController,
                decoration: const InputDecoration(labelText: 'Description'),
                maxLines: 3,
              ),
              const SizedBox(height: 8),
              TextField(
                controller: personaController,
                decoration: const InputDecoration(labelText: 'Personality'),
                maxLines: 2,
              ),
              const SizedBox(height: 8),
              TextField(
                controller: scenarioController,
                decoration: const InputDecoration(labelText: 'Scenario'),
                maxLines: 2,
              ),
              const SizedBox(height: 8),
              TextField(
                controller: firstMesController,
                decoration: const InputDecoration(labelText: 'First Message'),
                maxLines: 3,
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancel'),
          ),
          ElevatedButton(
            onPressed: () {
              if (nameController.text.isNotEmpty) {
                final character = Character(
                  id: const Uuid().v4(),
                  name: nameController.text,
                  description: descController.text,
                  personality: personaController.text,
                  scenario: scenarioController.text,
                  firstMes: firstMesController.text,
                );
                context.read<CharacterProvider>().addCharacter(character);
                Navigator.pop(context);
              }
            },
            child: const Text('Create'),
          ),
        ],
      ),
    );
  }
}
