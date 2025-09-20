package com.dovoh.android_mvi.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    loading: Boolean = false,
    modifier: Modifier = Modifier,
    height: Dp = 52.dp,
    shape: Shape = MaterialTheme.shapes.small
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        AnimatedContent(targetState = loading, label = "primary-button-loading") { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
            } else {
                Text(text)
            }
        }
    }
}
