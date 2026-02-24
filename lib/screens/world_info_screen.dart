import 'dart:convert';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:uuid/uuid.dart';

class WorldInfoScreen extends StatefulWidget {
  const WorldInfoScreen({super.key});

  @override
  State<WorldInfoScreen> createState() => _WorldInfoScreenState();
}

class _WorldInfoScreenState extends State<WorldInfoScreen> {
  List<Map<String, dynamic>> _entries = [];

  @override
  void initState() {
    super.initState();
    _loadEntries();
  }

  Future<void> _loadEntries() async {
    final prefs = await SharedPreferences.getInstance();
    final data = prefs.getString('world_info_entries');
    if (data != null) {
      setState(() {
        _entries = List<Map<String, dynamic>>.from(jsonDecode(data));
      });
    }
  }

  Future<void> _saveEntries() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('world_info_entries', jsonEncode(_entries));
  }

  void _addEntry() {
    showDialog(
      context: context,
      builder: (context) {
        final nameController = TextEditingController();
        final keysController = TextEditingController();
        final contentController = TextEditingController();

        return AlertDialog(
          title: const Text('Add Entry'),
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
                  controller: keysController,
                  decoration: const InputDecoration(
                    labelText: 'Keys (comma separated)',
                  ),
                ),
                const SizedBox(height: 8),
                TextField(
                  controller: contentController,
                  decoration: const InputDecoration(labelText: 'Content'),
                  maxLines: 5,
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
                final entry = {
                  'id': const Uuid().v4(),
                  'name': nameController.text,
                  'keys': keysController.text.split(',').map((e) => e.trim()).toList(),
                  'content': contentController.text,
                  'priority': 0,
                  'depth': 1,
                  'enabled': true,
                };
                setState(() {
                  _entries.add(entry);
                });
                _saveEntries();
                Navigator.pop(context);
              },
              child: const Text('Add'),
            ),
          ],
        );
      },
    );
  }

  void _editEntry(int index) {
    final entry = _entries[index];
    final nameController = TextEditingController(text: entry['name']);
    final keysController = TextEditingController(text: (entry['keys'] as List).join(', '));
    final contentController = TextEditingController(text: entry['content']);

    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Edit Entry'),
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
                controller: keysController,
                decoration: const InputDecoration(
                  labelText: 'Keys (comma separated)',
                ),
              ),
              const SizedBox(height: 8),
              TextField(
                controller: contentController,
                decoration: const InputDecoration(labelText: 'Content'),
                maxLines: 5,
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
              setState(() {
                _entries[index] = {
                  'id': entry['id'],
                  'name': nameController.text,
                  'keys': keysController.text.split(',').map((e) => e.trim()).toList(),
                  'content': contentController.text,
                  'priority': entry['priority'],
                  'depth': entry['depth'],
                  'enabled': entry['enabled'],
                };
              });
              _saveEntries();
              Navigator.pop(context);
            },
            child: const Text('Save'),
          ),
        ],
      ),
    );
  }

  void _deleteEntry(int index) {
    setState(() {
      _entries.removeAt(index);
    });
    _saveEntries();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('World Info / Lorebook'),
      ),
      body: _entries.isEmpty
          ? const Center(
              child: Text('No entries yet. Add one to get started!'),
            )
          : ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: _entries.length,
              itemBuilder: (context, index) {
                final entry = _entries[index];
                return Card(
                  margin: const EdgeInsets.only(bottom: 12),
                  child: ListTile(
                    title: Text(entry['name'] ?? 'Unnamed'),
                    subtitle: Text(
                      (entry['keys'] as List).join(', '),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        Switch(
                          value: entry['enabled'],
                          onChanged: (value) {
                            setState(() {
                              _entries[index]['enabled'] = value;
                            });
                            _saveEntries();
                          },
                        ),
                        IconButton(
                          icon: const Icon(Icons.edit),
                          onPressed: () => _editEntry(index),
                        ),
                        IconButton(
                          icon: const Icon(Icons.delete),
                          onPressed: () => _deleteEntry(index),
                        ),
                      ],
                    ),
                    onTap: () => _editEntry(index),
                  ),
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: _addEntry,
        child: const Icon(Icons.add),
      ),
    );
  }
}
