package com.dovoh.android_mvi.designsystem.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun InlineErrorChip(
    message: String,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    AssistChip(
        onClick = { onClick?.invoke() ?: Unit },
        label = { Text(message) },
        colors = AssistChipDefaults.assistChipColors(
            labelColor = MaterialTheme.colorScheme.error
        ),
        modifier = modifier
    )
}
