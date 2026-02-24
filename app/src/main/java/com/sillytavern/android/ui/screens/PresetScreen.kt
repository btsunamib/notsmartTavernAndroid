package com.sillytavern.android.ui.screens

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
import com.sillytavern.android.ui.viewmodel.PresetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PresetScreen(
    presetId: String,
    onBackClick: () -> Unit,
    viewModel: PresetViewModel = hiltViewModel()
) {
    val preset by viewModel.preset.collectAsStateWithLifecycle()
    var showSaveDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(presetId) {
        viewModel.loadPreset(presetId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(preset?.name ?: "Preset") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showSaveDialog = true }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Sampling Parameters",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        preset?.let { p ->
                            ParameterSlider(
                                label = "Temperature",
                                value = p.temperature.toFloat(),
                                valueRange = 0f..2f,
                                onValueChange = { viewModel.updateTemperature(it.toDouble()) }
                            )
                            
                            ParameterSlider(
                                label = "Top P",
                                value = p.topP.toFloat(),
                                valueRange = 0f..1f,
                                onValueChange = { viewModel.updateTopP(it.toDouble()) }
                            )
                            
                            ParameterSlider(
                                label = "Top K",
                                value = p.topK.toFloat(),
                                valueRange = 0f..100f,
                                onValueChange = { viewModel.updateTopK(it.toInt()) }
                            )
                            
                            ParameterSlider(
                                label = "Repetition Penalty",
                                value = p.repetitionPenalty.toFloat(),
                                valueRange = 1f..2f,
                                onValueChange = { viewModel.updateRepPen(it.toDouble()) }
                            )
                            
                            ParameterSlider(
                                label = "Rep Pen Range",
                                value = p.repetitionPenaltyRange.toFloat(),
                                valueRange = 0f..4096f,
                                onValueChange = { viewModel.updateRepPenRange(it.toInt()) }
                            )
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Mirostat",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        preset?.let { p ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Mirostat Mode")
                                Row {
                                    listOf(0, 1, 2).forEach { mode ->
                                        FilterChip(
                                            selected = p.mirostatMode == mode,
                                            onClick = { viewModel.updateMirostatMode(mode) },
                                            label = { Text(mode.toString()) },
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                    }
                                }
                            }
                            
                            if (p.mirostatMode != 0) {
                                ParameterSlider(
                                    label = "Mirostat Tau",
                                    value = p.mirostatTau.toFloat(),
                                    valueRange = 0f..10f,
                                    onValueChange = { viewModel.updateMirostatTau(it.toDouble()) }
                                )
                                
                                ParameterSlider(
                                    label = "Mirostat Eta",
                                    value = p.mirostatEta.toFloat(),
                                    valueRange = 0f..1f,
                                    onValueChange = { viewModel.updateMirostatEta(it.toDouble()) }
                                )
                            }
                        }
                    }
                }
            }
            
            item {
                Text(
                    text = "Other Settings",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                Card {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        preset?.let { p ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Streaming")
                                Switch(
                                    checked = p.streaming,
                                    onCheckedChange = { viewModel.updateStreaming(it) }
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Add BOS Token")
                                Switch(
                                    checked = p.addBosToken,
                                    onCheckedChange = { viewModel.updateAddBosToken(it) }
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Ban EOS token")
                                Switch(
                                    checked = p.banEosToken,
                                    onCheckedChange = { viewModel.updateBanEosToken(it) }
                                )
                            }
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Skip Special Tokens")
                                Switch(
                                    checked = p.skipSpecialTokens,
                                    onCheckedChange = { viewModel.updateSkipSpecialTokens(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Preset") },
            text = { Text("Save changes to this preset?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.savePreset()
                    showSaveDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ParameterSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label)
            Text(String.format("%.2f", value))
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange
        )
    }
}
