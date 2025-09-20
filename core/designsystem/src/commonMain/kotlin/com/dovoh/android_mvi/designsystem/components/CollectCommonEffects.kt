package com.dovoh.android_mvi.designsystem.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dovoh.android_mvi.core.mvi.CommonEffect
import com.dovoh.android_mvi.core.mvi.toUserMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

/**
 * Collects CommonEffect and calls [onMessage] with a user-friendly message.
 * Drop this into any screen to avoid repeating the same LaunchedEffect block.
 */
@Composable
fun CollectCommonEffects(
    effects: Flow<CommonEffect>,
    onMessage: (String) -> Unit
) {
    LaunchedEffect(effects) {
        effects
            .mapNotNull { it.toUserMessage() }
            .collect { msg -> onMessage(msg) }
    }
}
