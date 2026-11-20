package com.example.vulpineplayer.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SelectionTopBar(
    selectedCount: Int,
    onCancel: () -> Unit,
    isMenuOpen: Boolean,
    onMenuExpandedChange: (Boolean) -> Unit,
    dropdownMenu: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {
            onCancel()
        }) {
            Icon(Icons.Default.Close, contentDescription = "Cancel")
        }
        Text(
            text = "$selectedCount selected",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Box {
            IconButton(onClick = { onMenuExpandedChange(true) }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu")
            }

            dropdownMenu()
        }
    }
}