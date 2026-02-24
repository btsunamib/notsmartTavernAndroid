import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'screens/home_screen.dart';
import 'screens/chat_screen.dart';
import 'screens/settings_screen.dart';
import 'screens/characters_screen.dart';
import 'screens/world_info_screen.dart';
import 'providers/character_provider.dart';
import 'providers/chat_provider.dart';
import 'providers/settings_provider.dart';
import 'services/api_service.dart';

void main() {
  runApp(const SillyTavernApp());
}

class SillyTavernApp extends StatelessWidget {
  const SillyTavernApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => SettingsProvider()),
        ChangeNotifierProvider(create: (_) => CharacterProvider()),
        ChangeNotifierProvider(create: (_) => ChatProvider()),
        Provider(create: (_) => ApiService()),
      ],
      child: MaterialApp(
        title: 'SillyTavern',
        debugShowCheckedModeBanner: false,
        theme: ThemeData.dark().copyWith(
          primaryColor: const Color(0xFF4A5D9E),
          scaffoldBackgroundColor: const Color(0xFF121212),
          colorScheme: const ColorScheme.dark(
            primary: Color(0xFF4A5D9E),
            secondary: Color(0xFF6B8DD6),
            surface: Color(0xFF1E1E1E),
          ),
        ),
        home: const HomeScreen(),
        routes: {
          '/chat': (context) => const ChatScreen(),
          '/settings': (context) => const SettingsScreen(),
          '/characters': (context) => const CharactersScreen(),
          '/worldinfo': (context) => const WorldInfoScreen(),
        },
      ),
    );
  }
}
