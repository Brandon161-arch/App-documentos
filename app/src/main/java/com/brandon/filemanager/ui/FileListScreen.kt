package com.brandon.filemanager.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.brandon.filemanager.data.FileItem

@Composable
fun FileListScreen(
    viewModel: FileListViewModel,
    onFolderClick: (FileItem) -> Unit,
    onFileClick: (FileItem) -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        state.errorMessage != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(state.errorMessage ?: "")
        }

        state.items.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Esta carpeta está vacía")
        }

        else -> LazyColumn(Modifier.fillMaxSize()) {
            items(state.items) { item ->
                FileRow(
                    item = item,
                    onClick = { if (item.isDirectory) onFolderClick(item) else onFileClick(item) }
                )
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun FileRow(item: FileItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (item.isDirectory) Icons.Filled.Folder else Icons.Filled.InsertDriveFile,
            contentDescription = null
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(item.name, style = MaterialTheme.typography.bodyLarge)
            if (!item.isDirectory) {
                Text(
                    "${item.sizeBytes / 1024} KB",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}