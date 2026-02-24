package com.sillytavern.android.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sillytavern.android.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onPresetClick: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    var showApiDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "API Configuration",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showApiDialog = true }
                ) {
                    ListItem(
                        headlineContent = { Text("API Settings") },
                        supportingContent = { 
                            Text("Configure your LLM API endpoint and key")
                        },
                        leadingContent = {
                            Icon(Icons.Default.Api, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    )
                }
            }
            
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Generation Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onPresetClick("default") }
                ) {
                    ListItem(
                        headlineContent = { Text("Presets") },
                        supportingContent = { 
                            Text("Manage generation presets")
                        },
                        leadingContent = {
                            Icon(Icons.Default.Tune, contentDescription = null)
                        },
                        trailingContent = {
                            Icon(Icons.Default.ChevronRight, contentDescription = null)
                        }
                    )
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Temperature: ${settings.temperature}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = settings.temperature,
                            onValueChange = { viewModel.updateTemperature(it) },
                            valueRange = 0f..2f,
                            steps = 20
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Text(
                            text = "Top P: ${settings.topP}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = settings.topP,
                            onValueChange = { viewModel.updateTopP(it) },
                            valueRange = 0f..1f,
                            steps = 10
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Text(
                            text = "Max Tokens: ${settings.maxTokens}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = settings.maxTokens.toFloat(),
                            onValueChange = { viewModel.updateMaxTokens(it.toInt()) },
                            valueRange = 50f..4000f,
                            steps = 39
                        )
                    }
                }
            }
            
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "World Info",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "World Info Depth: ${settings.worldInfoDepth}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = settings.worldInfoDepth.toFloat(),
                            onValueChange = { viewModel.updateWorldInfoDepth(it.toInt()) },
                            valueRange = 1f..10f,
                            steps = 9
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        Text(
                            text = "World Info Budget: ${settings.worldInfoBudget} tokens",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Slider(
                            value = settings.worldInfoBudget.toFloat(),
                            onValueChange = { viewModel.updateWorldInfoBudget(it.toInt()) },
                            valueRange = 100f..8000f,
                            steps = 79
                        )
                    }
                }
            }
            
            item {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showAboutDialog = true }
                ) {
                    ListItem(
                        headlineContent = { Text("About SillyTavern Android") },
                        supportingContent = { 
                            Text("Version 1.0.0")
                        },
                        leadingContent = {
                            Icon(Icons.Default.Info, contentDescription = null)
                        }
                    )
                }
            }
        }
    }
    
    if (showApiDialog) {
        ApiSettingsDialog(
            currentSettings = settings,
            onDismiss = { showApiDialog = false },
            onSave = { apiKey, apiUrl, apiType ->
                viewModel.updateApiSettings(apiKey, apiUrl, apiType)
                showApiDialog = false
            }
        )
    }
    
    if (showAboutDialog) {
        AboutDialog(
            onDismiss = { showAboutDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiSettingsDialog(
    currentSettings: SettingsViewModel.AppSettings,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var apiKey by remember { mutableStateOf(currentSettings.apiKey) }
    var apiUrl by remember { mutableStateOf(currentSettings.apiUrl) }
    var apiType by remember { mutableStateOf(currentSettings.apiType) }
    var expanded by remember { mutableStateOf(false) }
    
    val apiTypes = listOf(
        "openai" to "OpenAI",
        "claude" to "Claude",
        "mistral" to "Mistral",
        "kobold" to "KoboldAI",
        "novelai" to "NovelAI",
        "openrouter" to "OpenRouter",
        "custom" to "Custom"
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("API Settings") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = apiTypes.find { it.first == apiType }?.second ?: "OpenAI",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("API Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        apiTypes.forEach { (type, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    apiType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                
                OutlinedTextField(
                    value = apiUrl,
                    onValueChange = { apiUrl = it },
                    label = { Text("API URL") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { apiKey = it },
                    label = { Text("API Key") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(apiKey, apiUrl, apiType) }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("About SillyTavern Android") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("SillyTavern Android v1.0.0")
                Spacer(Modifier.height(8.dp))
                Text("A mobile client for SillyTavern, providing a unified interface for LLM APIs.")
                Spacer(Modifier.height(8.dp))
                Text("Features:")
                Text("• Character card import (PNG/JSON)")
                Text("• World Info / Lorebook support")
                Text("• Preset management")
                Text("• Multiple API backends")
                Text("• Regex script support")
                Text("• Streaming responses")
                Spacer(Modifier.height(8.dp))
                Text("Based on SillyTavern by SillyTavern Team")
                Text("Licensed under AGPL-3.0")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}
