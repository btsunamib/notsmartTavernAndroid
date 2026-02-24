package com.sillytavern.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sillytavern.android.ui.screens.CharacterDetailScreen
import com.sillytavern.android.ui.screens.CharacterListScreen
import com.sillytavern.android.ui.screens.ChatScreen
import com.sillytavern.android.ui.screens.SettingsScreen
import com.sillytavern.android.ui.screens.WorldInfoScreen
import com.sillytavern.android.ui.screens.PresetScreen
import com.sillytavern.android.ui.screens.ImportScreen

sealed class Screen(val route: String) {
    object CharacterList : Screen("character_list")
    object CharacterDetail : Screen("character_detail/{characterId}") {
        fun createRoute(characterId: Long) = "character_detail/$characterId"
    }
    object Chat : Screen("chat/{characterId}/{chatId}") {
        fun createRoute(characterId: Long, chatId: Long) = "chat/$characterId/$chatId"
        fun createNewChatRoute(characterId: Long) = "chat/$characterId/new"
    }
    object Settings : Screen("settings")
    object WorldInfo : Screen("world_info/{worldInfoId}") {
        fun createRoute(worldInfoId: Long) = "world_info/$worldInfoId"
    }
    object Preset : Screen("preset/{presetId}") {
        fun createRoute(presetId: String) = "preset/$presetId"
    }
    object Import : Screen("import")
}

@Composable
fun SillyTavernNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CharacterList.route
    ) {
        composable(Screen.CharacterList.route) {
            CharacterListScreen(
                onCharacterClick = { characterId ->
                    navController.navigate(Screen.CharacterDetail.createRoute(characterId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onImportClick = {
                    navController.navigate(Screen.Import.route)
                }
            )
        }
        
        composable(Screen.CharacterDetail.route) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")?.toLongOrNull() ?: 0
            CharacterDetailScreen(
                characterId = characterId,
                onBackClick = { navController.popBackStack() },
                onStartChat = { chatId ->
                    navController.navigate(Screen.Chat.createRoute(characterId, chatId))
                },
                onEditWorldInfo = { worldInfoId ->
                    navController.navigate(Screen.WorldInfo.createRoute(worldInfoId))
                }
            )
        }
        
        composable(Screen.Chat.route) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getString("characterId")?.toLongOrNull() ?: 0
            val chatIdStr = backStackEntry.arguments?.getString("chatId") ?: "new"
            val chatId = if (chatIdStr == "new") null else chatIdStr.toLongOrNull()
            
            ChatScreen(
                characterId = characterId,
                chatId = chatId,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() },
                onPresetClick = { presetId ->
                    navController.navigate(Screen.Preset.createRoute(presetId))
                }
            )
        }
        
        composable(Screen.WorldInfo.route) { backStackEntry ->
            val worldInfoId = backStackEntry.arguments?.getString("worldInfoId")?.toLongOrNull() ?: 0
            WorldInfoScreen(
                worldInfoId = worldInfoId,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Preset.route) { backStackEntry ->
            val presetId = backStackEntry.arguments?.getString("presetId") ?: ""
            PresetScreen(
                presetId = presetId,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Import.route) {
            ImportScreen(
                onBackClick = { navController.popBackStack() },
                onImportComplete = { navController.popBackStack() }
            )
        }
    }
}
