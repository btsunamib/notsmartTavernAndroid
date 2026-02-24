package com.sillytavern.android.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sillytavern.android.data.local.entity.WorldInfoEntryEntity
import com.sillytavern.android.ui.viewmodel.WorldInfoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldInfoScreen(
    worldInfoId: Long,
    onBackClick: () -> Unit,
    viewModel: WorldInfoViewModel = hiltViewModel()
) {
    val worldInfo by viewModel.worldInfo.collectAsStateWithLifecycle()
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<WorldInfoEntryEntity?>(null) }
    
    LaunchedEffect(worldInfoId) {
        viewModel.loadWorldInfo(worldInfoId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = worldInfo?.name ?: "World Info",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Entry")
                    }
                }
            )
        }
    ) { padding ->
        if (entries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "No entries yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Add entries to create your lorebook",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add Entry")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = entries,
                    key = { it.id }
                ) { entry ->
                    WorldInfoEntryCard(
                        entry = entry,
                        onEdit = { editingEntry = entry },
                        onDelete = { viewModel.deleteEntry(entry) },
                        onToggleEnabled = { viewModel.toggleEntryEnabled(entry.id, !entry.enabled) }
                    )
                }
            }
        }
    }
    
    if (showAddDialog || editingEntry != null) {
        WorldInfoEntryDialog(
            entry = editingEntry,
            onDismiss = { 
                showAddDialog = false
                editingEntry = null
            },
            onSave = { entry ->
                if (editingEntry != null) {
                    viewModel.updateEntry(entry)
                } else {
                    viewModel.addEntry(entry)
                }
                showAddDialog = false
                editingEntry = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorldInfoEntryCard(
    entry: WorldInfoEntryEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleEnabled: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = entry.name.ifEmpty { "Entry" },
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                
                Row {
                    Switch(
                        checked = entry.enabled,
                        onCheckedChange = { onToggleEnabled() }
                    )
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = {
                                    showMenu = false
                                    onEdit()
                                },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete") },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null) }
                            )
                        }
                    }
                }
            }
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = "Keys: ${entry.keys}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                text = entry.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun WorldInfoEntryDialog(
    entry: WorldInfoEntryEntity?,
    onDismiss: () -> Unit,
    onSave: (WorldInfoEntryEntity) -> Unit
) {
    var name by remember { mutableStateOf(entry?.name ?: "") }
    var keys by remember { mutableStateOf(entry?.keys ?: "") }
    var content by remember { mutableStateOf(entry?.content ?: "") }
    var priority by remember { mutableStateOf(entry?.priority ?: 10) }
    var position by remember { mutableStateOf(entry?.position ?: "before_char") }
    var depth by remember { mutableStateOf(entry?.depth ?: 4) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (entry == null) "Add Entry" else "Edit Entry") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                OutlinedTextField(
                    value = keys,
                    onValueChange = { keys = it },
                    label = { Text("Keys (comma separated)") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = { Text("Keywords that trigger this entry") }
                )
                
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 6
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = priority.toString(),
                        onValueChange = { priority = it.toIntOrNull() ?: priority },
                        label = { Text("Priority") },
                        modifier = Modifier.weight(1f)
                    )
                    
                    OutlinedTextField(
                        value = depth.toString(),
                        onValueChange = { depth = it.toIntOrNull() ?: depth },
                        label = { Text("Depth") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val newEntry = WorldInfoEntryEntity(
                        id = entry?.id ?: 0,
                        worldInfoId = entry?.worldInfoId ?: 0,
                        entryId = entry?.entryId ?: System.currentTimeMillis(),
                        name = name,
                        keys = keys,
                        content = content,
                        priority = priority,
                        depth = depth,
                        position = position,
                        enabled = entry?.enabled ?: true
                    )
                    onSave(newEntry)
                },
                enabled = keys.isNotBlank() && content.isNotBlank()
            ) {
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
