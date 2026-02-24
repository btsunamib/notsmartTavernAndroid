import 'package:flutter/material.dart';

void main() {
  runApp(const SillyTavernApp());
}

class SillyTavernApp extends StatelessWidget {
  const SillyTavernApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SillyTavern',
      debugShowCheckedModeBanner: false,
      theme: ThemeData.dark().copyWith(
        primaryColor: const Color(0xFF4A5D9E),
        scaffoldBackgroundColor: const Color(0xFF121212),
      ),
      home: const HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('SillyTavern'),
      ),
      body: const Center(
        child: Text('Loading...'),
      ),
    );
  }
}
